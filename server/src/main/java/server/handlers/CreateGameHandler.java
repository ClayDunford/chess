package server.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedRequestException;
import io.javalin.http.Context;
import model.*;
import server.requests.CreateGameRequest;
import server.results.CreateGameResult;
import service.CreateGameService;

public class CreateGameHandler {
    private final CreateGameService createGameService;

    public CreateGameHandler(CreateGameService createGameService) {
        this.createGameService = createGameService;
    }

    public void createGame (Context ctx) {
        String authToken = ctx.header("authorization");
        CreateGameRequest createGameRequest= new Gson().fromJson(ctx.body(), CreateGameRequest.class);
        try {
            GameData gameData = createGameService.createGame(authToken, createGameRequest);
            CreateGameResult createGameResult = new CreateGameResult(gameData.gameID());
            ctx.result(new Gson().toJson(createGameResult));
        } catch (UnauthorizedRequestException e) {
            ErrorMessage message = new ErrorMessage(e.getMessage());
            ctx.status(401).result(new Gson().toJson(message));
        } catch (BadRequestException e) {
            ErrorMessage message = new ErrorMessage(e.getMessage());
            ctx.status(400).result(new Gson().toJson(message));
        } catch (DataAccessException e) {
            ErrorMessage message = new ErrorMessage("Error: " + e.getMessage());
            ctx.status(500).result(new Gson().toJson(message));
        }
    }
}
