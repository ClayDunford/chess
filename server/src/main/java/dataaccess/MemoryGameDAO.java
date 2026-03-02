package dataaccess;
import java.util.*;
import model.GameData;

public class MemoryGameDAO implements GameDAO{
    final private Map<Integer, GameData> GameDatabase = new HashMap<>();
    public void clearGame() {
        GameDatabase.clear();
    }
}
