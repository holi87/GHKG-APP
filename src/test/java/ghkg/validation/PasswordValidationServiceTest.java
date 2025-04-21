package ghkg.validation;

import ghkg.api.exception.PasswordChangeException;
import ghkg.domain.account.User;
import ghkg.services.validation.PasswordValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PasswordValidationServiceTest {

    private PasswordEncoder passwordEncoder;
    private PasswordValidationService passwordValidationService;

    @BeforeEach
    void setUp() {
        passwordEncoder = mock(PasswordEncoder.class);
        passwordValidationService = new PasswordValidationService(passwordEncoder);
    }

    @Test
    void shouldThrowExceptionWhenNewPasswordIsSameAsCurrent() {
        User user = new User();
        user.setPassword("encoded-password");

        when(passwordEncoder.matches("new-password", "encoded-password")).thenReturn(true);

        PasswordChangeException exception = assertThrows(
                PasswordChangeException.class,
                () -> passwordValidationService.validatePasswordChange(user, "new-password")
        );

        assertEquals("New password must be different from the current one", exception.getMessage());
    }

    @Test
    void shouldNotThrowWhenNewPasswordIsDifferent() {
        User user = new User();
        user.setPassword("encoded-password");

        when(passwordEncoder.matches("new-password", "encoded-password")).thenReturn(false);

        assertDoesNotThrow(() -> passwordValidationService.validatePasswordChange(user, "new-password"));
    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordDoesNotMatch() {
        User user = new User();
        user.setPassword("encoded-password");

        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

        PasswordChangeException exception = assertThrows(
                PasswordChangeException.class,
                () -> passwordValidationService.validateCurrentPassword(user, "wrong-password")
        );

        assertEquals("Current password does not match", exception.getMessage());
    }

    @Test
    void shouldNotThrowWhenCurrentPasswordMatches() {
        User user = new User();
        user.setPassword("encoded-password");

        when(passwordEncoder.matches("correct-password", "encoded-password")).thenReturn(true);

        assertDoesNotThrow(() -> passwordValidationService.validateCurrentPassword(user, "correct-password"));
    }
}
