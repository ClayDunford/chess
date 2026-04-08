package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import com.google.gson.Gson;
import exception.ResponseException;
import serverfacade.ServerFacade;
import ui.ChessBoardGenerator;
import websocket.messages.ServerMessage;

import java.util.Arrays;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class GameplayClient implements NotificationHandler {
    private final Repl repl;
    private final ServerFacade server;
    private WebSocketFacade webSocket = null;
    private String authToken;
    private ChessGame currentBoard = null;
    private ChessGame.TeamColor curColor;

    public GameplayClient(ServerFacade serverFacade, Repl passedRepl, String serverUrl) {
        repl = passedRepl;
        server = serverFacade;
        authToken = repl.authToken;
        curColor = repl.curColor;
        try {
            webSocket = new WebSocketFacade(serverUrl, this);
        } catch (ResponseException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String eval (String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw();
                case "move" -> makeMove();
                case "resign" -> resign();
                case "highlight" -> highlightLegalMoves(params);
                case "leave" -> leave();
                default -> help();

            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String help() {
        return """
                - redraw - redraws the current board
                - highlight <Piece Position> - Highlights the legal moves on the board
                - move <Starting Piece Position> <Final Piece Position> - Make a move in the chess game
                - resign - lose the game
                - leave - leaves the game
                - help - possible commands
                """;
    }

    public void redraw() {
        new ChessBoardGenerator(currentBoard, curColor).drawBoard();
    }

    public void highlightLegalMoves(String... params) throws ResponseException{
        if (params.length == 1) {
            String input = params[0];
            ChessPosition position = positionParser(input);
            if (!pieceAtPosition(position)) {
                throw new ResponseException(ResponseException.Code.ClientError, "No piece at position!");
            }
            Collection<ChessMove> chessMoves = currentBoard.validMoves(position);
            ChessBoardGenerator boardGenerator = new ChessBoardGenerator(currentBoard, curColor);
            boardGenerator.moveToArray(chessMoves);
            boardGenerator.drawBoard();
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected <Chess Position>");
    }
    private ChessPosition positionParser(String input) throws ResponseException{
        char[] tokens = input.toLowerCase().toCharArray();
        if (tokens.length > 2) {
            throw new ResponseException(ResponseException.Code.ClientError, "Invalid Chess position");
        }
        int col = Character.getNumericValue(tokens[0]) - 9;
        int row = Character.getNumericValue(tokens[1]);
        if (col < 1 | Character.getNumericValue(col) > 9) {
            throw new ResponseException(ResponseException.Code.ClientError, "Invalid Chess position");
        } if (Character.getNumericValue(row) < 1 | Character.getNumericValue(row) > 9) {
            throw new ResponseException(ResponseException.Code.ClientError, "Invalid Chess position");
        }

        return new ChessPosition(row, col);
    }
    private boolean pieceAtPosition(ChessPosition position) {
        ChessBoard board = currentBoard.getBoard();
        return (board.getPiece(position) != null);
    }

    @Override
    public void notify(ServerMessage serverMessage) {
        switch (serverMessage.getServerMessageType()) {
            case ERROR -> printError(serverMessage);
            case LOAD_GAME -> printBoardUpdate(serverMessage);
            case NOTIFICATION -> printNotification(serverMessage);
        }
    }

    private void printBoardUpdate(ServerMessage serverMessage) {
        currentBoard = new Gson().fromJson(serverMessage.message, ChessGame.class);
        new ChessBoardGenerator(currentBoard, curColor).drawBoard();
    }

    private void printNotification(ServerMessage serverMessage) {
        System.out.println(SET_TEXT_COLOR_GREEN + serverMessage.message);
        repl.printPrompt();
    }

    private void printError(ServerMessage serverMessage) {
        System.out.println(SET_TEXT_COLOR_RED + serverMessage.errorMessage);
        repl.printPrompt();
    }
}
