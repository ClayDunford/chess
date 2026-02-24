package dataaccess;

import model.UserData;

public interface DataAccess {
    UserData register(UserData userData) throws DataAccessException;
}
