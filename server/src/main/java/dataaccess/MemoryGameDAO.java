package dataaccess;
import java.util.*;
import model.GameData;

public class MemoryGameDAO implements GameDAO{
    final private Map<Integer, GameData> GameDatabase = new HashMap<>();
    public void clearGame() {
        GameDatabase.clear();
    }

    public int getSize() {
        return GameDatabase.size();
    }

    public void createGame(GameData gameData) {
        int gameID = gameData.gameID();
        GameDatabase.put(gameID, gameData);
    }

    public List<GameData> listGames() {
        return new ArrayList<>(GameDatabase.values());
    }

    public GameData getGame(int gameID) {
        if (GameDatabase.get(gameID) != null) {
            return GameDatabase.get(gameID);
        }
        return null;
    }
}
