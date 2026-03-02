package server;
import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import io.javalin.http.Context;
import model.AuthData;
import model.ErrorMessage;
import model.UserData;
import service.RegisterService;

public class RegisterHandler {
    private final RegisterService registerService;
    public RegisterHandler(RegisterService registerService) {
        this.registerService = registerService;
    }

    public void register(Context ctx) {
        UserData userData = new Gson().fromJson(ctx.body(), UserData.class);
        try {
            AuthData authData = registerService.register(userData);
            ctx.result(new Gson().toJson(authData));
        } catch (AlreadyTakenException e) {
            ErrorMessage message = new ErrorMessage(e.getMessage());
            ctx.status(403).result(new Gson().toJson(message));
        }
    }
}
