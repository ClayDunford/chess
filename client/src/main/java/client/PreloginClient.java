package client;

import java.util.Arrays;
import passoff.exception.ResponseParseException;

public class PreloginClient {
    private final Repl repl;
    private final String serverUrl;


    public PreloginClient(String passedServerUrl, Repl passedRepl) {
        serverUrl = passedServerUrl;
        repl = passedRepl;
    }

    public String eval (String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> quit();
                default -> help();
            };
        } catch (ResponseParseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) {

    }

    public String register(String... params) {

    }

    public String quit() {

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
