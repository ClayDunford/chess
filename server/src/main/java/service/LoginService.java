package service;

import dataaccess.AlreadyTakenException;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class LoginService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    public LoginService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public AuthData login(UserData userData) throws DataAccessException {
        if (userDAO.getUser(userData) == null){
            throw new DataAccessException("No username in database");
        } if (!userData.password().equals(userDAO.getPassword(userData))) {
            throw new DataAccessException("Passwords don't match");
        }
        String authToken = generateAuthToken();
        AuthData authData = new AuthData(userData.username(), authToken);
        authDAO.createAuth(authData);

        return authData;
    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

}
