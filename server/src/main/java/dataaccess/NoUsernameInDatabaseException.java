package dataaccess;

public class NoUsernameInDatabaseException extends Exception {
    public NoUsernameInDatabaseException() {
        super("Error: No username in Database");
    }
}
