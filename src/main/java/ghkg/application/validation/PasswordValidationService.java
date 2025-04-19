package ghkg.application.validation;

import ghkg.api.exception.PasswordChangeException;
import ghkg.domain.account.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordValidationService {

    private final PasswordEncoder passwordEncoder;

    public void validatePasswordChange(User user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new PasswordChangeException("New password must be different from the current one");

        }
    }

    public void validateCurrentPassword(User user, String currentPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new PasswordChangeException("Current password does not match");

        }
    }

}
