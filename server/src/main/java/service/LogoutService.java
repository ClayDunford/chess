package service;

import dataaccess.AuthDAO;
import dataaccess.UnauthorizedRequestException;

public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logout(String authToken) throws UnauthorizedRequestException {
        if(authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedRequestException();
        }
        authDAO.deleteAuth(authToken);
    }
}
