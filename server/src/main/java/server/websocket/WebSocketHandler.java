package server.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exception.DataAccessException;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.swing.*;
import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();
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
            switch (userGameCommand.getCommandType()) {
                case CONNECT -> connect(userGameCommand.getGameID(), userGameCommand.getAuthToken(), ctx.session);
                case MAKE_MOVE -> makeMove(userGameCommand.getGameID(), userGameCommand.getAuthToken(), ctx.session);
                case RESIGN -> resign(userGameCommand.getGameID(), userGameCommand.getAuthToken(), ctx.session);
                case LEAVE -> leave(userGameCommand.getGameID(), userGameCommand.getAuthToken(), ctx.session);
            }


        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (DataAccessException ex) {

        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {

    }

    private void connect(Integer gameID, String authToken, Session session) throws DataAccessException, IOException {
        connections.add(gameID, session);
        String username = authDAO.getAuth(authToken).username();
        GameData gameData = gameDAO.getGame(gameID);
        String teamcolor = null;
        if (gameData.blackUsername().equals(username)) {
            teamcolor = "black";
        } else if (gameData.whiteUsername().equals(username)) {
            teamcolor = "white";
        }
        String message;
        if (teamcolor != null) {
            message = String.format("%s joined the game as %s", username, teamcolor);
        } else{
            message = String.format("%s is observing the game! ", username);
        }
        var serverMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(gameID, session, serverMessage);
    }

    private void makeMove(Integer gameID, String authToken, Session session) {

    }

    private void resign(Integer gameID, String authToken, Session session) {

    }

    private void leave(Integer gameID, String authToken, Session session) {


    }
}

