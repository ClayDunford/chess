package server.handlers;

import com.google.gson.Gson;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import dataaccess.exceptions.UnauthorizedRequestException;
import io.javalin.http.Context;
import model.ErrorMessage;
import server.requests.JoinGameRequest;
import service.JoinGameService;

public class JoinGameHandler {
    private final JoinGameService joinGameService;

    public JoinGameHandler(JoinGameService joinGameService) {this.joinGameService = joinGameService;}

    public void joinGame (Context ctx) {
        String authToken = ctx.header("authorization");
        JoinGameRequest joinGameRequest = new Gson().fromJson(ctx.body(), JoinGameRequest.class);

        try {
            joinGameService.joinGame(authToken, joinGameRequest);
        } catch (AlreadyTakenException e) {
            ErrorMessage message = new ErrorMessage(e.getMessage());
            ctx.status(403).result(new Gson().toJson(message));
        } catch (UnauthorizedRequestException e) {
            ErrorMessage message = new ErrorMessage(e.getMessage());
            ctx.status(401).result(new Gson().toJson(message));
        } catch (BadRequestException | DataAccessException e) {
            ErrorMessage message = new ErrorMessage(e.getMessage());
            ctx.status(400).result(new Gson().toJson(message));
        }
    }
}
