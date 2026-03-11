package service;

import dataaccess.*;
import dataaccess.exceptions.AlreadyTakenException;
import dataaccess.exceptions.BadRequestException;
import dataaccess.exceptions.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class RegisterService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    public RegisterService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    public AuthData register(UserData userData) throws BadRequestException, AlreadyTakenException, DataAccessException {
        if (!userData.validate()) {
            throw new BadRequestException();
        }
        if (userDAO.getUser(userData) != null) {
            throw new AlreadyTakenException();
        }

        userDAO.createUser(userData);

        String authToken = generateAuthToken();
        AuthData authData = new AuthData(userData.username(), authToken);
        authDAO.createAuth(authData);
        return authData;
    }

    private String generateAuthToken() {
        return UUID.randomUUID().toString();
    }

}
