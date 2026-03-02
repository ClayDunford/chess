package service;

import dataaccess.AlreadyTakenException;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

public class LoginService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    public LoginService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData login(UserData userData) {
        if (userDAO.getUser(userData) == null) {

        }


    }
}
