package dataaccess;

import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import model.UserData;

public interface UserDAO {
    UserData getUser(UserData userData);
    boolean checkPassword(UserData userData);
    void createUser(UserData userData) throws DataAccessException;
    void clearUser();
}
