package dataaccess;

import model.AuthData;
import model.UserData;

public interface UserDAO {
    UserData getUser(UserData userData);
    String getPassword(UserData userData);
    void createUser(UserData userData);
    void clearUser();
}
