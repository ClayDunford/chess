package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO{
    final private Map<String, UserData> UserDatabase = new HashMap<>();
    final private Map<String, AuthData> AuthDatabase = new HashMap<>();
    public UserData getUser(UserData userData){
        String username = userData.username();
        if (UserDatabase.get(username) != null) {
            return UserDatabase.get(username);
        }
        return null;

    }

    public void createUser(UserData userData) {
        String username = userData.username();
        UserDatabase.put(username, userData);
    }

}
