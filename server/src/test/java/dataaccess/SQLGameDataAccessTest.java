package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGameDataAccessTest {
    private static SQLGameDAO gameDAO;

    @BeforeAll
    public static void startDAO() {gameDAO = new SQLGameDAO();}

    @BeforeEach
    public void setUp() throws DataAccessException {gameDAO.clearGame();}

    @Test
    @DisplayName("Create Game Test")
    @Order(1)
    public void createGameTest() throws DataAccessException {
        GameData createGameData = new GameData(1, null, null, "game", new ChessGame());
        gameDAO.createGame(createGameData);
        GameData returnCreateGameData = gameDAO.getGame(createGameData.gameID());
        assertEquals(returnCreateGameData, createGameData, "Game was not created");
    }


    @Test
    @DisplayName("Create Null Game test")
    @Order(2)
    public void createNullGameTest() {
        assertThrows(DataAccessException.class, () -> {
            gameDAO.createGame(null);
        });
    }

    @Test
    @DisplayName("Get Game test")
    @Order(3)
    public void getGameTest() throws DataAccessException{
        GameData getGameData = new GameData(1, null, null, "game", new ChessGame());
        gameDAO.createGame(getGameData);
        GameData returnGetGameData = gameDAO.getGame(getGameData.gameID());
        assertEquals(returnGetGameData, getGameData, "Game's do not match");
    }

    @Test
    @DisplayName("Get 0 Game Test")
    @Order(4)
    public void getNullGameTest() {
        assertThrows(DataAccessException.class, () -> {
            gameDAO.getGame(0);
        });
    }

    @Test
    @DisplayName("Delete Game Test")
    @Order(5)
    public void deleteGameTest() throws DataAccessException {
        GameData deleteGameData = new GameData(1, null, null, "game", new ChessGame());
        gameDAO.createGame(deleteGameData);
        gameDAO.deleteGame(deleteGameData.gameID());
        assertNull(gameDAO.getGame(deleteGameData.gameID()));
    }

    @Test
    @DisplayName("Delete 0 Game Test")
    @Order(6)
    public void deleteNullGameTest() {
        assertThrows(DataAccessException.class, () -> {
            gameDAO.deleteGame(0);
        });
    }

    @Test
    @DisplayName("List Games")
    @Order(7)
    public void listGames() throws DataAccessException{
        GameData listGameDataOne = new GameData(1, null, null, "game", new ChessGame());
        gameDAO.createGame(listGameDataOne);
        GameData listGameDataTwo = new GameData(2, null, null, "game", new ChessGame());
        gameDAO.createGame(listGameDataTwo);
        List<GameData> gameDataList = new ArrayList<>();
        gameDataList.add(listGameDataOne);
        gameDataList.add(listGameDataTwo);
        assertEquals(gameDataList, gameDAO.listGames(), "Lists don't match");
    }

    @Test
    @DisplayName("Empty List Games Test")
    @Order(8)
    public void emptyListGames() throws DataAccessException{
        assertTrue(gameDAO.listGames().isEmpty());
    }

    @Test
    @DisplayName("Get Game Number Test")
    @Order(9)
    public void getGameNum() throws DataAccessException{
        GameData numGameDataOne = new GameData(1, null, null, "game", new ChessGame());
        gameDAO.createGame(numGameDataOne);
        GameData numGameDataTwo = new GameData(2, null, null, "game", new ChessGame());
        gameDAO.createGame(numGameDataTwo);
        assertEquals(2, gameDAO.getSize());
    }

    @Test
    @DisplayName("Empty Game Number Test")
    @Order(10)
    public void getEmptyGameNum() throws DataAccessException{
        assertEquals(0, gameDAO.getSize());
    }

    @Test
    @DisplayName("Clear Test")
    @Order(11)
    public void clearTest() throws DataAccessException {
        GameData clearGameData = new GameData(1, null, null, "game", new ChessGame());
        gameDAO.createGame(clearGameData);
        int rowCount = gameDAO.clearGame();
        assertEquals(0, rowCount, "Too many rows");
    }








}
