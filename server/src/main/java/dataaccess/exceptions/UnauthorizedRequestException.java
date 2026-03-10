package dataaccess.exceptions;

public class UnauthorizedRequestException extends RuntimeException {
    public UnauthorizedRequestException() {
        super("Error: unauthorized");
    }
}
