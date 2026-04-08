package client;

import chess.*;
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
    private final String authToken;
    private final int gameID;
    private ChessGame currentBoard = null;
    private ChessGame.TeamColor curColor;
    private final boolean observing;

    public GameplayClient(ServerFacade serverFacade, Repl passedRepl, String serverUrl, boolean observing) throws ResponseException{
        repl = passedRepl;
        server = serverFacade;
        this.observing = observing;
        authToken = repl.authToken;
        curColor = repl.curColor;
        gameID = repl.gameData.gameID();
        try {
            webSocket = new WebSocketFacade(serverUrl, this);
        } catch (ResponseException ex) {
            System.out.println(ex.getMessage());
        }
        connect();
    }

    public void connect() throws ResponseException{
        webSocket.connect(authToken, gameID);
    }

    public String eval (String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (!observing) {
                return switch (cmd) {
                    case "redraw" -> redraw();
                    case "move" -> makeMove();
                    case "resign" -> resign();
                    case "highlight" -> highlightLegalMoves(params);
                    case "leave" -> leave();
                    default -> help();
                };
            } else {
                return switch (cmd) {
                    case "redraw" -> redraw();
                    case "highlight" -> highlightLegalMoves(params);
                    case "leave" -> leave();
                    default -> help();
                };
            }

        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String makeMove(String... params) throws ResponseException{
        if(params.length < 4 && params.length >= 2 ) {
            String startPosString = params[0];
            ChessPosition startPos = positionParser(startPosString);
            pieceAtPosition(startPos);
            String endPosString = params[1];
            ChessPosition endPos = positionParser(endPosString);
            ChessPiece.PieceType promotionPiece;
            if (params.length == 3) {
                String promotion = params[2];
                promotionPiece = promotionConverter(promotion);
            } else {
                promotionPiece = null;
            }
            ChessMove move = new ChessMove(startPos, endPos, promotionPiece);
            validMoveChecker(move);

            return "Move made";
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected <Start Position> <End Position> <Promotion (If Relevant)>");
    }

    public void validMoveChecker(ChessMove move) throws ResponseException{
        try {
            ChessGame tempGame = new ChessGame();
            tempGame.setBoard(currentBoard.getBoard());
            tempGame.setTeamTurn(curColor);
            tempGame.makeMove(move);
        } catch (InvalidMoveException ex){
            throw new ResponseException(ResponseException.Code.ClientError, "Invalid Move!");
        }
    }

    public ChessPiece.PieceType promotionConverter(String input) throws ResponseException{
        return switch (input) {
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "queen" -> ChessPiece.PieceType.QUEEN;
            default -> throw new ResponseException(ResponseException.Code.ClientError, "Invalid Promotion Piece");
        };
    }

    public String help() {
        if (observing) {
            return """
                Observer Commands:
                - redraw - redraws the current board
                - highlight <Piece Position> - Highlights the legal moves on the board
                - leave - leaves the game
                - help - possible commands
                """;
        }
        return """
                Player Commands
                - redraw - redraws the current board
                - highlight <Piece Position> - Highlights the legal moves on the board
                - move <Starting Piece Position> <Final Piece Position> <Promotion (If Relevant)>- Make a move in the chess game
                - resign - lose the game L
                - leave - leaves the game
                - help - possible commands
                """;
    }

    public String resign() {
        return "Resigning...";
    }

    public String leave() {
        return "Leaving game...";
    }
    public String redraw() {
        new ChessBoardGenerator(currentBoard, curColor).drawBoard();
        return (RESET_TEXT_COLOR);
    }

    public String highlightLegalMoves(String... params) throws ResponseException{
        if (params.length == 1) {
            String input = params[0];
            ChessPosition position = positionParser(input);
            pieceAtPosition(position);
            Collection<ChessMove> chessMoves = currentBoard.validMoves(position);
            ChessBoardGenerator boardGenerator = new ChessBoardGenerator(currentBoard, curColor);
            boardGenerator.moveToArray(chessMoves);
            boardGenerator.drawBoard();
            return "Valid moves generated";
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
    private void pieceAtPosition(ChessPosition position) throws ResponseException {
        ChessBoard board = currentBoard.getBoard();
        if (board.getPiece(position) != null) {
            throw new ResponseException(ResponseException.Code.ClientError, "No piece at position!");
        }
    }

    @Override
    public void notify(ServerMessage serverMessage) {
        System.out.println("Debug: Notified");
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
