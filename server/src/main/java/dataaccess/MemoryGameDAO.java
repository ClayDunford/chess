package dataaccess;
import java.util.*;
import model.GameData;

public class MemoryGameDAO implements GameDAO{
    final private Map<Integer, GameData> gameDatabase = new HashMap<>();
    public int clearGame() {
        gameDatabase.clear();
        return 0;
    }

    public int getSize() {
        return gameDatabase.size();
    }

    public void createGame(GameData gameData) {
        int gameID = gameData.gameID();
        gameDatabase.put(gameID, gameData);
    }

    public List<GameData> listGames() {
        return new ArrayList<>(gameDatabase.values());
    }

    public GameData getGame(int gameID) {
        if (gameDatabase.get(gameID) != null) {
            return gameDatabase.get(gameID);
        }
        return null;
    }
}
