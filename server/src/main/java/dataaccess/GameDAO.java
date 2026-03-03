package dataaccess;

import model.GameData;

public interface GameDAO {
    void clearGame();
    int getSize();
    void createGame(GameData gameData);
}
