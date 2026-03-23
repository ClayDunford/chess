package client;

import com.sun.nio.sctp.Notification;
import com.sun.nio.sctp.NotificationHandler;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    private final PreloginClient preloginClient;
    private final PostloginClient postloginClient;
    private final GameplayClient gameplayClient;
    private State currentState;

    public Repl(String serverUrl) {
        preloginClient = new PreloginClient(serverUrl, this);
        postloginClient = new PostloginClient(serverUrl, this);
        gameplayClient = new GameplayClient(serverUrl, this);
        currentState = State.SIGNEDOUT;
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Chess Game:");
        System.out.print(preloginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                switch (currentState) {
                    case State.SIGNEDOUT -> result = preloginClient.eval(line);
                    case State.SIGNEDIN -> result = postloginClient.eval(line);
                    case State.GAMEPLAY -> result = gameplayClient.eval(line);
                }

                System.out.print(SET_BG_COLOR_BLUE + result);

            } catch (Throwable e){
                var msg = e.toString();
                System.out.print(SET_TEXT_COLOR_RED + msg);
            }

        }
        System.out.println();
    }


    private void printPrompt() {
        System.out.print("\n" + currentState + ">>>");
    }
}
