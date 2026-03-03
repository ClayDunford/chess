package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UnauthorizedRequestException;
import model.GameData;

import java.util.List;

public class ListGamesService {

    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ListGamesService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public List<GameData> listGames(String authToken) {
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedRequestException();
        }
        return gameDAO.listGames();
    }
}
