package server.requests;

public record JoinGameRequest(String playerColor, int gameID) {
    public boolean validate() {
        return playerColor != null && (playerColor.equals("BLACK") || playerColor.equals("WHITE")) && gameID > 0 ;
    }
}
