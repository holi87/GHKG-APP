package ghkg.api.controllers.exception;

public class CannotModifySuperAdminException extends RuntimeException {
    public CannotModifySuperAdminException(String message) {
        super(message);
    }
}