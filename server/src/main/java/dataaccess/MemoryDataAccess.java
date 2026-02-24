package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class MemoryDataAccess implements DataAccess {
    final private HashMap<Integer, UserData> UserDatabase = new HashMap<>();

    public UserData register(UserData userData) {
        return userData;
    }
}
