package server;

import com.google.gson.Gson;
import dataaccess.UnauthorizedRequestException;
import io.javalin.http.Context;
import model.ErrorMessage;
import service.LogoutService;

public class LogoutHandler {
    private final LogoutService logoutService;

    public LogoutHandler(LogoutService logoutService) {this.logoutService = logoutService;}

    public void logout (Context ctx) {
        String authToken = ctx.header("authorization");
        try {
            logoutService.logout(authToken);
        } catch (UnauthorizedRequestException e) {
            ErrorMessage message = new ErrorMessage(e.getMessage());
            ctx.status(401).result(new Gson().toJson(message));
        }
    }
}
