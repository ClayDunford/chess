package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import model.UserData;

public interface UserDAO {
    UserData getUser(UserData userData) throws DataAccessException;
    boolean checkPassword(UserData userData) throws DataAccessException;
    void createUser(UserData userData) throws DataAccessException;
    int clearUser() throws DataAccessException;
}
