package model;

public record UserData(String username, String password, String email) {
    public boolean validate() {
        return username != null && password != null;
    }
}
