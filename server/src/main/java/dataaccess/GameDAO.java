package dataaccess;

import model.GameData;

import java.util.List;

public interface GameDAO {
    void clearGame();
    int getSize();
    void createGame(GameData gameData);
    List<GameData> listGames();
    GameData getGame (int gameID);
}
