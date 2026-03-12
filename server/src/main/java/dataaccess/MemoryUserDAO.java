package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO{
    final private Map<String, UserData> userDatabase = new HashMap<>();
    public UserData getUser(UserData userData){
        String username = userData.username();
        if (userDatabase.get(username) != null) {
            return userDatabase.get(username);
        }
        return null;

    }

    public boolean checkPassword(UserData userData) {
        UserData curUser = userDatabase.get(userData.username());
        return curUser.password().equals(userData.password());
    }

    public void createUser(UserData userData) {
        String username = userData.username();
        userDatabase.put(username, userData);
    }

    public int clearUser() {
       userDatabase.clear();
       return 0;
    }

}
