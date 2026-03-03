package dataaccess;

public class UnauthorizedRequestException extends RuntimeException {
    public UnauthorizedRequestException() {
        super("Error: unauthorized");
    }
}
