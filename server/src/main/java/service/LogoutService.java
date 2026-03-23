package service;

import dataaccess.AuthDAO;
import exception.DataAccessException;
import exception.UnauthorizedRequestException;

public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logout(String authToken) throws UnauthorizedRequestException, DataAccessException {
        if(authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedRequestException();
        }
        authDAO.deleteAuth(authToken);
    }
}
