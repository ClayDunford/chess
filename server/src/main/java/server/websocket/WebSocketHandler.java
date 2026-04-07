package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.BadRequestException;
import exception.DataAccessException;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private boolean resigned = false;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            String authToken = userGameCommand.getAuthToken();
            Integer gameID = userGameCommand.getGameID();
            if (authDAO.getAuth(authToken) == null | gameDAO.getGame(gameID) == null) {
                throw new BadRequestException();
            }
            String username = authDAO.getAuth(authToken).username();
            switch (userGameCommand.getCommandType()) {
                case CONNECT -> connect(gameID, username, ctx.session);
                case MAKE_MOVE -> makeMove(userGameCommand, username, ctx.session);
                case RESIGN -> resign(userGameCommand, username, ctx.session);
                case LEAVE -> leave(userGameCommand.getGameID(), username, ctx.session);
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (DataAccessException | BadRequestException | InvalidMoveException ex) {
            sendMessage(ctx.session, ServerMessage.ServerMessageType.ERROR, ex.getMessage());
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {

    }
    private void sendMessage(Session session, ServerMessage.ServerMessageType type, String message) {
        try {
            if (type == ServerMessage.ServerMessageType.ERROR) {
                message = "Error: " + message;
            }
            ServerMessage serverMessage = new ServerMessage(type, message);
            String messageString = new Gson().toJson(serverMessage);
            session.getRemote().sendString(messageString);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void connect(Integer gameID, String username, Session session) throws DataAccessException, IOException {
        connections.add(gameID, session);
        GameData gameData = gameDAO.getGame(gameID);
        String teamcolor = teamColorFinder(gameData, username);
        String message;
        if (teamcolor != null) {
            message = String.format("%s joined the game as %s", username, teamcolor);
        } else{
            message = String.format("%s is observing the game! ", username);
        }
        var gameServerMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(gameID, session, gameServerMessage);

        String chessGame = new Gson().toJson(gameData.game());
        sendMessage(session, ServerMessage.ServerMessageType.LOAD_GAME, chessGame);
    }

    private String teamColorFinder(GameData gameData, String username) {
        String teamcolor = null;
        if (gameData.blackUsername().equals(username)) {
            teamcolor = "black";
        } else if (gameData.whiteUsername().equals(username)) {
            teamcolor = "white";
        }
        return teamcolor;
    }

    private void makeMove(UserGameCommand command, String username, Session session) throws DataAccessException, InvalidMoveException, IOException {
        if (!resigned) {
            GameData gameData = gameDAO.getGame(command.getGameID());
            if (teamColorFinder(gameData, username) == null) {
                throw new InvalidMoveException();
            }
            ChessMove newMove = command.getMove();
            pieceChecker(gameData, username, newMove);
            ChessGame currentGame = gameData.game();
            currentGame.makeMove(newMove);
            gameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), currentGame, false);
            gameDAO.createGame(gameData);
            String message = String.format("%s made a move: %s", username, newMove);
            gameChecker(gameData, session);
            ServerMessage notificationMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            connections.broadcast(command.getGameID(), session, notificationMessage);
            String chessGame = new Gson().toJson(gameData.game());
            ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, chessGame);
            connections.broadcast(command.getGameID(), null, loadGameMessage);
        } else {
            sendMessage(session, ServerMessage.ServerMessageType.NOTIFICATION, "Game is over");
        }
    }

    private void pieceChecker(GameData gameData, String username, ChessMove move) throws InvalidMoveException{
        ChessGame.TeamColor currentColor = ChessGame.TeamColor.WHITE;
        if (username.equals(gameData.blackUsername())) {
            currentColor = ChessGame.TeamColor.BLACK;
        }
        ChessPosition startPos = move.getStartPosition();
        ChessBoard board = gameData.game().getBoard();
        ChessGame.TeamColor pieceColor = board.getPiece(startPos).getTeamColor();
        if (pieceColor != currentColor) {
            throw new InvalidMoveException();
        }

    }

    private void gameChecker(GameData gameData, Session session) throws IOException {
        ChessGame currentGame = gameData.game();
        String currentTeam = "White";
        String currentUser = gameData.whiteUsername();
        ChessGame.TeamColor currentColor = ChessGame.TeamColor.WHITE;

        String message = null;
        for (int i = 0; i < 2; i++) {
            if (currentGame.isInCheck(currentColor)) {
                if (currentGame.isInCheckmate(currentColor)) {
                    message = String.format("%s (%s) is in Checkmate", currentUser, currentTeam);

                } else {
                    message = String.format("%s (%s) is in Check", currentUser, currentTeam);
                }
            } else if (currentGame.isInStalemate(currentColor)) {
                message = String.format("%s (%s) is in Stalemate", currentUser, currentTeam);
            }
            if (message != null) {
                connections.broadcast(gameData.gameID(), null, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message));
            }
            message = null;
            currentTeam = "Black";
            currentUser = gameData.blackUsername();
            currentColor = ChessGame.TeamColor.BLACK;
        }
    }

    private void resign(UserGameCommand command, String username, Session session) throws DataAccessException, IOException, InvalidMoveException{
        if (resigned) {
            throw new InvalidMoveException();
        }
        GameData gameData = gameDAO.getGame(command.getGameID());
        String teamColor = teamColorFinder(gameData, username);
        if (teamColor == null) {
            throw new InvalidMoveException();
        }
        resigned = true;
        String message = String.format("%s (%s) has resigned!", username, teamColor);
        connections.broadcast(command.getGameID(), null, new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message));
    }

    private void leave(Integer gameID, String authToken, Session session) {


    }
}

