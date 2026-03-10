//package service;
//
//import dataaccess.*;
//import dataaccess.exceptions.AlreadyTakenException;
//import dataaccess.exceptions.BadRequestException;
//import model.AuthData;
//import model.UserData;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//public class ServiceTests {
//    // DAOs
//    static final UserDAO userDAO = new MemoryUserDAO();
//    static final AuthDAO authDAO = new MemoryAuthDAO();
//    static final GameDAO gameDAO = new MemoryGameDAO();
//
//    // Services
//    static final RegisterService registerService = new RegisterService(userDAO, authDAO);
//    static final ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
//    static final LoginService loginService = new LoginService(userDAO, authDAO);
//    static final LogoutService logoutService = new LogoutService(authDAO);
//    static final CreateGameService createGameService = new CreateGameService(authDAO, gameDAO);
//    static final ListGamesService listGamesService = new ListGamesService(authDAO, gameDAO);
//    static final JoinGameService joinGameService = new JoinGameService(authDAO, gameDAO);
//
//    @BeforeEach
//    void clear() {
//        clearService.clear();
//    }
//
//    @Test
//
//}