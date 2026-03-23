package client;

import com.sun.nio.sctp.Notification;
import com.sun.nio.sctp.NotificationHandler;
import model.GameData;
import server.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private final PreloginClient preloginClient;
    private final PostloginClient postloginClient;
    private final ServerFacade server;
    private State currentState;
    public String authToken = null;
    boolean inGame = false;
    GameData gameData = null;

    public Repl(String serverUrl) {
        server = new ServerFacade(serverUrl);
        currentState = State.SIGNEDOUT;

        preloginClient = new PreloginClient(server, this);
        postloginClient = new PostloginClient(server, this);

    }

    public void run() {
        System.out.println("\uD83D\uDC36 Chess Game:");
        System.out.print(preloginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            if (authToken != null) {
                currentState = State.SIGNEDIN;
                if (inGame) {
                    currentState = State.GAMEPLAY;
                }
            } else {
                currentState = State.SIGNEDOUT;
            }
            printPrompt();
            String line = scanner.nextLine();
            try {
                switch (currentState) {
                    case State.SIGNEDOUT -> result = preloginClient.eval(line);
                    case State.SIGNEDIN -> result = postloginClient.eval(line);
                    case State.GAMEPLAY -> result = postloginClient.eval(line);
                }
                System.out.print(result);

            } catch (Throwable e){
                var msg = e.toString();
                System.out.print(SET_TEXT_COLOR_RED + msg);
            }

        }
        authToken = null;
        System.out.println();
    }


    private void printPrompt() {
        System.out.print("\n" + currentState + ">>>");
    }
}
