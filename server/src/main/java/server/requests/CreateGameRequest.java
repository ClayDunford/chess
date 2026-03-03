package server.requests;

public record CreateGameRequest(String gameName) {
    public boolean validate() {
        return gameName != null;
    }
}
