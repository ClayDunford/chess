package client;

import client.websocket.NotificationHandler;
import client.websocket.WebSocketFacade;
import exception.ResponseException;
import serverfacade.ServerFacade;
import websocket.messages.ServerMessage;

public class GameplayClient implements NotificationHandler {
    private final Repl repl;
    private final ServerFacade server;
    private WebSocketFacade webSocket = null;
    private String authToken;

    public GameplayClient(ServerFacade serverFacade, Repl passedRepl, String serverUrl) {
        repl = passedRepl;
        server = serverFacade;
        authToken = repl.authToken;
        try {
            webSocket = new WebSocketFacade(serverUrl, this);
        } catch (ResponseException ex) {
            System.out.println(ex.getMessage());
        }
    }


    @Override
    public void notify(ServerMessage serverMessage) {

    }
}
