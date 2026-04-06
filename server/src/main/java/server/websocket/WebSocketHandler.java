package server.websocket;

import com.google.gson.Gson;
import io.javalin.websocket.*;
import websocket.commands.UserGameCommand;

import javax.swing.*;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {
    private final ConnectionManager connections = new ConnectionManager();

    @Override
    public void handleConnect(WsConnectContext ctx) {
        try {
            UserGameCommand userGameCommand = new Gson().fromJson(ctx.message(), Action.class);
            switch (userGameCommand.getCommandType()) {
                case CONNECT ->connect()
            }

        }
    }

    private void connect() {

    }

    private boid


}
