package service;

import dataaccess.DataAccess;
import dataaccess.DataAccessException;
import dataaccess.MemoryDataAccess;
import model.AuthData;
import model.UserData;

import java.util.*;

public class Service {
    private final DataAccess dataAccess;

    public Service(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    public AuthData register(UserData userData) throws DataAccessException {
        if (dataAccess.getUser(userData) != null) {
            throw new DataAccessException("Error: Username already taken");
        }
        dataAccess.createUser(userData);
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, userData.username());
        dataAccess.createAuth(authData);
        return authData;
    }
}
