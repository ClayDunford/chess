package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.AuthData;
import model.ErrorMessage;
import model.UserData;
import service.LoginService;

public class LoginHandler {
    private final LoginService loginService;

    public LoginHandler(LoginService loginService) {this.loginService = loginService;}

    public void login (Context ctx) {
        UserData userData = new Gson().fromJson(ctx.body(), UserData.class);
        try {
            AuthData authData =loginService.login(userData);
            ctx.result(new Gson().toJson(authData));
        } catch (DataAccessException e) {
            ErrorMessage message = new ErrorMessage(e.getMessage());
            ctx.status(403).result(new Gson().toJson(message));
        }
    }
}
