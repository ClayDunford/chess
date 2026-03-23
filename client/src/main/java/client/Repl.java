package client;

import com.sun.nio.sctp.NotificationHandler;

import java.util.Scanner;

public class Repl implements NotificationHandler {
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

            } catch (Throwable e){
                var msg = e.toString();
                System.out.print(msg);
            }

        }
    }

    private void printPrompt() {
        System.out.print("\n" + currentState + ">>>");
    }
}
