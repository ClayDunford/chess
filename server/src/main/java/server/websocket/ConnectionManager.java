package server.websocket;

import com.sun.nio.sctp.Notification;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ConnectionManager {
    public final HashMap<Integer, Set<Session>> connections = new HashMap<>();

    public void add(Integer gameID, Session session) {
        Set<Session> sessions = connections.get(gameID);
        if (sessions == null) {
            sessions = new HashSet<>();
            sessions.add(session);
            connections.put(gameID, sessions);
        } else {
            sessions.add(session);
        }
    }

    public void remove(Integer gameID, Session session) {
        Set<Session> sessions = connections.get(gameID);
        sessions.remove(session);
        connections.put(gameID, sessions);
    }

    public void broadcast(Integer gameID, Session excludeSession, ServerMessage serverMessage) throws IOException {
        String msg = serverMessage.toString();
        Set<Session> sessions = connections.get(gameID);
        for (Session c : sessions) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }

}
