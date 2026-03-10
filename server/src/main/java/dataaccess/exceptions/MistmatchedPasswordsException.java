package dataaccess.exceptions;

public class MistmatchedPasswordsException extends Exception {
    public MistmatchedPasswordsException() {
        super("Error: Mismatched Passwords");
    }
}
