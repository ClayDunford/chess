package server;

import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import com.google.gson.Gson;
import model.*;
import service.*;

public class Server {

    private final Javalin javalin;
    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public Server() {
        this(new MemoryAuthDAO(), new MemoryUserDAO(), new MemoryGameDAO());
    }
    public Server(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        // DAOS
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;

        // Services
        RegisterService registerService = new RegisterService(userDAO, authDAO);

        // Handlers
        RegisterHandler registerHandler = new RegisterHandler(registerService);
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", registerHandler::register);

        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

}
