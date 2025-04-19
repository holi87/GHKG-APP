package ghkg.api.exception;

public class CannotModifySuperAdminException extends RuntimeException {
    public CannotModifySuperAdminException(String message) {
        super(message);
    }
}