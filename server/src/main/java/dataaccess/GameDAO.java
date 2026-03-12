package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.GameData;

import java.util.List;

public interface GameDAO {
    int clearGame() throws DataAccessException;
    int getSize() throws DataAccessException;
    void createGame(GameData gameData) throws DataAccessException;
    List<GameData> listGames() throws DataAccessException;
    GameData getGame (int gameID) throws DataAccessException;
    void deleteGame(int gameID) throws DataAccessException;
}
