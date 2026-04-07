package client;

import chess.ChessGame;
import chess.ChessPosition;
import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import com.google.gson.Gson;
import exception.ResponseException;
import serverfacade.ServerFacade;
import ui.ChessBoardGenerator;
import websocket.messages.ServerMessage;

import java.util.Arrays;

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
                case "highlight" -> highlightLegalMoves();
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
        new ChessBoardGenerator(currentBoard, curColor);
    }

    public void highlightLegalMoves(){

    }
    private ChessPosition positionParser(String input) throws ResponseException{

        char[] tokens = input.toLowerCase().toCharArray();
        if (tokens.length > 2) {
            throw new ResponseException(ResponseException.Code.ClientError, "Invalid Chess position");
        }
        char col = tokens[0];
        char row = tokens[1];
        if (Character.getNumericValue(col) < 9 | Character.getNumericValue(col) > 17) {
            throw new ResponseException(ResponseException.Code.ClientError, "Invalid Chess position");
        } if (Character.getNumericValue(row) < 1 | Character.getNumericValue(row) > 9) {
            throw new ResponseException(ResponseException.Code.ClientError, "Invalid Chess position");
        }

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
        new ChessBoardGenerator(currentBoard, curColor);
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
