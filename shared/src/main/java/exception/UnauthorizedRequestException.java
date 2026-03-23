package exception;

public class UnauthorizedRequestException extends RuntimeException {
    public UnauthorizedRequestException() {
        super("Error: unauthorized");
    }
}
