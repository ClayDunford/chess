package dataaccess;

import chess.ChessGame;
import dataaccess.exceptions.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.*;

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
}
