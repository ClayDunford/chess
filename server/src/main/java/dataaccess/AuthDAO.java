package dataaccess;

import exception.DataAccessException;
import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authData) throws DataAccessException;
    AuthData getAuth (String authToken) throws DataAccessException;
    void deleteAuth (String authToken) throws DataAccessException;
    int clearAuth() throws DataAccessException;
}
