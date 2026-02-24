package server;

import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import model.UserData;

public class Server {

    private final Javalin javalin;

    public Server() {
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

    private void registerUser(Context ctx) {
        UserData userData = new Gson().fromJson(ctx.body(), UserData.class);

    }
}
