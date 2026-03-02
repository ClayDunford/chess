package server;

import dataaccess.AlreadyTakenException;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import model.*;
import service.*;

public class Server {

    private final Javalin javalin;
    private final Service service;
    private final RegisterService registerService;

    public Server() { this(new Service(new MemoryDataAccess())); }
    public Server(Service service) {
        this.service = service;
        this.registerService =

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", new RegisterHandler().register);

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void registerUser(Context ctx) throws DataAccessException {
        UserData userData = new Gson().fromJson(ctx.body(), UserData.class);
        try {
            AuthData authData = service.register(userData);
            ctx.result(new Gson().toJson(authData));
        } catch (AlreadyTakenException e) {
            ErrorMessage message = new ErrorMessage(e.getMessage());
            ctx.status(403).result(new Gson().toJson(message));
        }
    }
}
