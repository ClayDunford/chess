package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.exceptions.BadRequestException;
import dataaccess.GameDAO;
import dataaccess.exceptions.UnauthorizedRequestException;
import model.*;
import server.requests.CreateGameRequest;

public class CreateGameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public CreateGameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public GameData createGame(String authToken, CreateGameRequest createGameRequest) throws BadRequestException, UnauthorizedRequestException {
        if (!createGameRequest.validate()) {
            throw new BadRequestException();
        }
        if (authDAO.getAuth(authToken) == null) {
            throw new UnauthorizedRequestException();
        }
        String gameName = createGameRequest.gameName();
        int gameID = gameDAO.getSize() + 1;
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        gameDAO.createGame(gameData);
        return gameData;
    }
}
