package client;

import exception.ResponseException;
import server.ServerFacade;

import java.util.Arrays;

public class PostloginClient {
    private final Repl repl;
    private final ServerFacade server;
    private final String authToken;

    public PostloginClient(ServerFacade serverFacade, Repl passedRepl) {
        repl = passedRepl;
        server = serverFacade;
        authToken = repl.authToken;
    }

    public String eval (String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "logout" -> logout(params);
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String logout(String... params) {

    }

    public String createGame(String... params) {

    }

    public String listGames(String... params) {

    }

    public String joinGame(String... params) {

    }

    public String observeGame(String... params) {

    }

    public String help();



}
