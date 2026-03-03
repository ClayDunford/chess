package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO{
    final private Map<String, AuthData> AuthDatabase = new HashMap<>();
    public void createAuth(AuthData authData) {
        String authToken = authData.authToken();
        AuthDatabase.put(authToken, authData);
    }

    public void clearAuth() {
        AuthDatabase.clear();
    }

    public AuthData getAuth(String authToken) {
        if (AuthDatabase.get(authToken) != null) {
            return AuthDatabase.get(authToken);
        }
        return null;
    }

    public void deleteAuth(String authToken) {
        AuthDatabase.remove(authToken);
    }
}
