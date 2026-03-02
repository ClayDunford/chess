package server;

import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import model.*;
import service.Service;

public class Server {

    private final Javalin javalin;
    private final Service service;

    public Server() { this(new Service(new MemoryDataAccess())); }
    public Server(Service service) {
        this.service = service;

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", this::registerUser);

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
        AuthData authData = service.register(userData);
        ctx.result(new Gson().toJson(authData));
    }
}
