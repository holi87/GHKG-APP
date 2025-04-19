package ghkg.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleCarNotFound(CarNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Car not found", ex.getMessage());
    }

    @ExceptionHandler(InvalidCarDataException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidData(InvalidCarDataException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Invalid car data", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", errorMsg);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad request", ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Access denied", ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAll(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", ex.getMessage());
    }

    @ExceptionHandler(CannotModifySuperAdminException.class)
    public ResponseEntity<Map<String, Object>> handleSuperAdminModification(CannotModifySuperAdminException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, "Modification denied", ex.getMessage());
    }

    @ExceptionHandler(PasswordChangeException.class)
    public ResponseEntity<Map<String, Object>> handlePasswordChangeException(PasswordChangeException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Password change failed", ex.getMessage());
    }

    @ExceptionHandler(TripNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTripNotFound(TripNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Trip not found", ex.getMessage());
    }

    @ExceptionHandler(InvalidTripException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidTrip(InvalidTripException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, "Invalid trip", ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String error, String message) {
        return ResponseEntity.status(status).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", error,
                        "message", message
                )
        );
    }

}
