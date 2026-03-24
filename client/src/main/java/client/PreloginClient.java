package client;

import java.util.Arrays;

import exception.ResponseException;
import model.AuthData;
import model.UserData;
import serverfacade.ServerFacade;

public class PreloginClient {
    private final Repl repl;
    private final ServerFacade server;


    public PreloginClient(ServerFacade serverFacade, Repl passedRepl) {
        repl = passedRepl;
        server = serverFacade;
    }

    public String eval (String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params)  throws ResponseException{
        if (params.length == 2) {
            String username = params[0];
            String password = params[1];

            UserData userData = new UserData(username, password, null);
            AuthData authData = server.login(userData);
            repl.authToken = authData.authToken();
            return String.format("Signed is as: %s", username);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <username> <password>");
    }

    public String register(String... params) throws ResponseException{
        if (params.length >= 2 && params.length < 4) {
            String username = params[0];
            String password = params[1];
            String email;
            if (params.length == 3) {
                email = params[2];
            } else {
                email = null;
            }
            UserData userData = new UserData(username, password, email);
            AuthData authData = server.register(userData);
            repl.authToken = authData.authToken();
            return String.format("Signed is as: %s", username);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <username> <password> <email>");
    }


    public String help() {
        return """
                - login <username> <password> - to login
                - register <username> <password> <email> - to create an account
                - quit - to exit
                - help - possible commands
                """;
    }
}
