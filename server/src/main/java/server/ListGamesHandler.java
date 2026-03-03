package server;

import com.google.gson.Gson;
import dataaccess.UnauthorizedRequestException;
import io.javalin.http.Context;
import model.ErrorMessage;
import model.GameData;
import server.results.ListGamesResult;
import service.ListGamesService;

import java.util.List;

public class ListGamesHandler {
    private final ListGamesService listGamesService;

    public ListGamesHandler(ListGamesService listGamesService) {this.listGamesService = listGamesService;}

    public void listGames (Context ctx) {
        String authToken = ctx.header("authorization");
        try {
            List<GameData> gameList = listGamesService.listGames(authToken);
            ListGamesResult listGamesResult = new ListGamesResult(gameList);
            ctx.result(new Gson().toJson(listGamesResult));
        }catch (UnauthorizedRequestException e) {
            ErrorMessage message = new ErrorMessage(e.getMessage());
            ctx.status(401).result(new Gson().toJson(message));
        }
    }
}
