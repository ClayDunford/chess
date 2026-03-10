package service;

import dataaccess.*;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.MistmatchedPasswordsException;
import dataaccess.exceptions.NoUsernameInDatabaseException;
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

    public AuthData login(UserData userData) throws BadRequestException, NoUsernameInDatabaseException, MistmatchedPasswordsException {
        if (!userData.validate()) {
            throw new BadRequestException();
        }
        if (userDAO.getUser(userData) == null){
            throw new NoUsernameInDatabaseException();
        } if (!(userDAO.checkPassword(userData))) {
            throw new MistmatchedPasswordsException();
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
