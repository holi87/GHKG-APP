package ghkg.api.controllers.exception;

public class InvalidTripException extends RuntimeException {
    public InvalidTripException(String message) {
        super(message);
    }
}
