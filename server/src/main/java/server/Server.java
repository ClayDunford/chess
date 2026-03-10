package server;

import dataaccess.*;
import io.javalin.*;
import server.handlers.*;
import service.*;

public class Server {

    private final Javalin javalin;
    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;

    public Server() {
        this(new MemoryAuthDAO(), new SQLUserDAO(), new MemoryGameDAO());
    }
    public Server(AuthDAO authDAO, UserDAO userDAO, GameDAO gameDAO) {
        // DAOS
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;

        // Services
        RegisterService registerService = new RegisterService(userDAO, authDAO);
        ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
        LoginService loginService = new LoginService(userDAO, authDAO);
        LogoutService logoutService = new LogoutService(authDAO);
        CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
        ListGamesService listGamesService = new ListGamesService(authDAO, gameDAO);
        JoinGameService joinGameService = new JoinGameService(authDAO, gameDAO);

        // Handlers
        RegisterHandler registerHandler = new RegisterHandler(registerService);
        ClearHandler clearHandler = new ClearHandler(clearService);
        LoginHandler loginHandler = new LoginHandler(loginService);
        LogoutHandler logoutHandler = new LogoutHandler(logoutService);
        CreateGameHandler createGameHandler = new CreateGameHandler(createGameService);
        ListGamesHandler listGamesHandler = new ListGamesHandler(listGamesService);
        JoinGameHandler joinGameHandler = new JoinGameHandler(joinGameService);

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .post("/user", registerHandler::register)
                .post("/session", loginHandler::login)
                .delete("/session", logoutHandler::logout)
                .post("/game", createGameHandler::createGame)
                .get("/game", listGamesHandler::listGames)
                .put("/game", joinGameHandler::joinGame)
                .delete("/db", clearHandler::clear);

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
