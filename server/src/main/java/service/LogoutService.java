package service;

import dataaccess.AuthDAO;
import dataaccess.UnauthorizedRequest;
import model.AuthData;

public class LogoutService {
    private final AuthDAO authDAO;

    public LogoutService(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    public void logout(String authToken) throws UnauthorizedRequest{
        if(authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedRequest();
        }
        authDAO.deleteAuth(authToken);
    }
}
