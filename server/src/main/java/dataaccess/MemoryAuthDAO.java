package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO{
    final private Map<String, AuthData> authDatabase = new HashMap<>();
    public void createAuth(AuthData authData) {
        String authToken = authData.authToken();
        authDatabase.put(authToken, authData);
    }

    public void clearAuth() {
        authDatabase.clear();
    }

    public AuthData getAuth(String authToken) {
        if (authDatabase.get(authToken) != null) {
            return authDatabase.get(authToken);
        }
        return null;
    }

    public void deleteAuth(String authToken) {
        authDatabase.remove(authToken);
    }
}
