package client;

import exception.ResponseException;
import model.GameData;
import model.requests.CreateGameRequest;
import model.results.CreateGameResult;
import model.results.ListGamesResult;
import server.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostloginClient {
    private final Repl repl;
    private final ServerFacade server;
    private String authToken;
    private List<GameData> currentGameList = new ArrayList<>();

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
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String logout() throws ResponseException{
        server.logout(authToken);
        authToken = null;
        repl.authToken = null;
        return "Logged out!";
    }

    public String createGame(String... params) throws ResponseException{
        if (params.length == 1) {
           String gameName = params[0];
            CreateGameRequest gameRequest = new CreateGameRequest(gameName);
            server.createGame(authToken, gameRequest);
            return String.format("Successfully created game: %s", gameName);
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected <name>");
    }

    public String listGames() throws ResponseException{
        ListGamesResult gameList = server.listGames(authToken);
        currentGameList = gameList.games();
        return chessListBuilder(gameList);
    }

    private String chessListBuilder(ListGamesResult gameList) {
        var result = new StringBuilder();
        result.append("Current Games\n");
        int i = 1;
        for (GameData gameData : gameList.games()) {
            String gameName = gameData.gameName();
            String white = gameData.whiteUsername();
            String black = gameData.blackUsername();
            if (white == null) {
                white = "empty";
            }
            if (black == null) {
                black = "empty";
            }

            result.append(i).append(". Game Name: ").append(gameName)
                    .append(" White: ").append(white)
                    .append(" Black: ").append(black)
                    .append("\n");
        }

        return result.toString();
    }

    public String joinGame(String... params) {

    }

    public String observeGame(String... params) {

    }

    public String help() {
        return """
                - create <name> - create a game
                - list - lists available games
                - join <ID> [WHITE|BLACK] - join a preexisting game
                - observe <ID> - observe a preexisting game
                - quit - to exit
                - help - possible commands
                """;
    }



}
