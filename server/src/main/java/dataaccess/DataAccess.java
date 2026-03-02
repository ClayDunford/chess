package dataaccess;

import model.AuthData;
import model.UserData;

public interface DataAccess {
    UserData getUser(UserData userData);
    void createUser(UserData userData);
    void createAuth(AuthData authData);
}
