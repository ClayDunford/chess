package dataaccess;

import model.AuthData;
import model.UserData;

public interface UserDAO {
    UserData getUser(UserData userData);
    void createUser(UserData userData);
}
