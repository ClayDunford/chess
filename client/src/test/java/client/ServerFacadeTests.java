package client;

import chess.ChessGame;
import exception.ResponseException;
import model.AuthData;
import model.GameData;
import model.UserData;
import model.requests.CreateGameRequest;
import model.requests.JoinGameRequest;
import model.results.CreateGameResult;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ServerFacade;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
;


public class ServerFacadeTests {
    private static ServerFacade facade;
    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        var url = "http://localhost:" + port;
        facade = new ServerFacade(url);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void setup() throws ResponseException {
        facade.clear();
    }


    @AfterAll
    static void stopServer() throws ResponseException{
        facade.clear();
        server.stop();
    }


    @Test
    @DisplayName("Register Test (Positive)")
    @Order(1)
    public void registerTest() throws ResponseException {
        UserData fakeRegisterUser = new UserData("username", "password", "email");
        AuthData fakeRegisterAuth = facade.register(fakeRegisterUser);
        assertEquals(fakeRegisterUser.username(), fakeRegisterAuth.username(), "Users do not match");
    }

    @Test
    @DisplayName("Register Test (Negative)")
    @Order(2)
    public void registerTestNegative() throws ResponseException {
        UserData fakeRegisterUserOne = new UserData("username", "password", "email");
        UserData fakeRegisterUserTwo = new UserData("username", "password", "email");
        facade.register(fakeRegisterUserOne);
        assertThrows(ResponseException.class, () -> {
            facade.register(fakeRegisterUserTwo);
        });

    }

    @Test
    @DisplayName("Login Test (Positive)")
    @Order(3)
    public void loginTest() throws ResponseException {
        UserData fakeLoginUser = new UserData("username", "password", "email");
        AuthData fakeLoginAuthOne = facade.register(fakeLoginUser);
        AuthData fakeLoginAuthTwo = facade.login(fakeLoginUser);
        assertEquals(fakeLoginAuthOne.username(), fakeLoginAuthTwo.username(), "Usernames don't match");
    }

    @Test
    @DisplayName("Login Test (Negative)")
    @Order(4)
    public void loginTestNegative() throws ResponseException {
        UserData fakeRegisterUserOne = new UserData("username", "password", "email");
        assertThrows(ResponseException.class, () -> {
            facade.login(fakeRegisterUserOne);
        });

    }

    @Test
    @DisplayName("Logout Test (Positive)")
    @Order(5)
    public void logoutTest() throws ResponseException {
        UserData fakeLogoutUser = new UserData("username", "password", "email");
        facade.register(fakeLogoutUser);
        AuthData fakeLogoutAuth = facade.login(fakeLogoutUser);
        facade.logout(fakeLogoutAuth.authToken());
        assertEquals(fakeLogoutAuth.username(), facade.login(fakeLogoutUser).username());
    }

    @Test
    @DisplayName("Logout Test (Negative)")
    @Order(6)
    public void logoutTestNegative() throws ResponseException {
        String authToken = "fakeAuth";
        assertThrows(ResponseException.class, () -> {
            facade.logout(authToken);
        });
    }

    @Test
    @DisplayName("Create Game Test (Positive)")
    @Order(7)
    public void createGameTest() throws ResponseException {
        UserData fakeCreateGameUser = new UserData("username", "password", "email");
        AuthData fakeCreateGameAuth = facade.register(fakeCreateGameUser);
        String authToken = fakeCreateGameAuth.authToken();
        CreateGameRequest createGameRequest = new CreateGameRequest("game");
        CreateGameResult createGameResult = facade.createGame(authToken, createGameRequest);
        assertEquals(1, createGameResult.gameID());
    }

    @Test
    @DisplayName("Create Game Test (Negative)")
    @Order(8)
    public void createGameTestNegative() throws ResponseException {
        CreateGameRequest createGameRequest = new CreateGameRequest("game");
        String authToken = "fakeAuth";
        assertThrows(ResponseException.class, () -> {
            facade.createGame(authToken, createGameRequest);
        });
    }

    @Test
    @DisplayName("List Games (Positive)")
    @Order(9)
    public void listGamesTest() throws ResponseException {
        UserData fakeListGamesUser = new UserData("username", "password", "email");
        AuthData fakeListGamesAuth = facade.register(fakeListGamesUser);
        String authToken = fakeListGamesAuth.authToken();

        GameData listGameDataOne = new GameData(1, null, null, "game1", new ChessGame(), false);
        GameData listGameDataTwo = new GameData(2, null, null, "game2", new ChessGame(), false);

        facade.createGame(authToken, new CreateGameRequest(listGameDataOne.gameName()));
        facade.createGame(authToken, new CreateGameRequest(listGameDataTwo.gameName()));

        List<GameData> gameDataList = new ArrayList<>();
        gameDataList.add(listGameDataOne);
        gameDataList.add(listGameDataTwo);

        assertEquals(gameDataList, facade.listGames(authToken).games());
    }

    @Test
    @DisplayName("List Games (Negative)")
    @Order(10)
    public void listGamesTestNegative() {
        String authToken = "fakeAuth";
        assertThrows(ResponseException.class, () -> {
            facade.listGames(authToken);
        });
    }

    @Test
    @DisplayName("Join Game (Positive)")
    @Order(11)
    public void joinGameTest() throws ResponseException {
        UserData fakeJoinGamesUser = new UserData("username", "password", "email");
        AuthData fakeJoinGamesAuth = facade.register(fakeJoinGamesUser);
        String authToken = fakeJoinGamesAuth.authToken();

        GameData listGameDataOne = new GameData(1, fakeJoinGamesAuth.username(), null, "game1", new ChessGame(), false);

        facade.createGame(authToken, new CreateGameRequest(listGameDataOne.gameName()));
        facade.joinGame(authToken, new JoinGameRequest("WHITE", 1));

        List<GameData> gameDataList = new ArrayList<>();
        gameDataList.add(listGameDataOne);

        assertEquals(gameDataList, facade.listGames(authToken).games());
    }

    @Test
    @DisplayName("Join Game (Negative)")
    @Order(10)
    public void joinGameTestNegative() {
        String authToken = "fakeAuth";
        assertThrows(ResponseException.class, () -> {
            facade.joinGame(authToken, new JoinGameRequest("WHITE", 1));
        });
    }

    @Test
    @DisplayName("Clear (Positive)")
    @Order(11)
    public void clearTest() throws ResponseException {
        UserData fakeClearUser = new UserData("username", "password", "email");
        facade.register(fakeClearUser);
        facade.clear();
        AuthData fakeClearAuth = facade.register(fakeClearUser);
        assertEquals("username", fakeClearAuth.username());
    }








}
