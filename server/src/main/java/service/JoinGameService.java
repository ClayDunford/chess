package service;

import dataaccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.DataAccessException;
import exception.UnauthorizedRequestException;
import model.GameData;
import model.requests.JoinGameRequest;

public class JoinGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public JoinGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void joinGame(String authToken, JoinGameRequest joinGameRequest) throws AlreadyTakenException, DataAccessException {
        if (!joinGameRequest.validate()) {
            throw new BadRequestException();
        }
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedRequestException();
        }
        String username = authDAO.getAuth(authToken).username();
        int gameID = joinGameRequest.gameID();
        if (gameDAO.getGame(gameID) == null) {
            throw new BadRequestException();
        }
        GameData gameData = gameDAO.getGame(gameID);
        String teamColor = joinGameRequest.playerColor();
        if (teamColor.equals("BLACK")) {
            if (gameData.blackUsername() != null && !gameData.blackUsername().equals(username)) {
                throw new AlreadyTakenException();
            }
            gameDAO.deleteGame(gameID);
            gameData = new GameData(gameID, gameData.whiteUsername(), username, gameData.gameName(), gameData.game());
        } else {
            if (gameData.whiteUsername() != null && !gameData.whiteUsername().equals(username)) {
                throw new AlreadyTakenException();
            }
            gameDAO.deleteGame(gameID);
            gameData = new GameData(gameID, username, gameData.blackUsername(), gameData.gameName(), gameData.game());
        }
        gameDAO.createGame(gameData);

    }
}
