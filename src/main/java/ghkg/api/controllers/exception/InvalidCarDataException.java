package ghkg.api.controllers.exception;

public class InvalidCarDataException extends RuntimeException {
    public InvalidCarDataException(String message) {
        super(message);
    }
}