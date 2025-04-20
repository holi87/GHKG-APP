package ghkg.services;

import ghkg.api.exception.CannotModifySuperAdminException;
import ghkg.api.exception.UsernameNotFoundException;
import ghkg.domain.account.Role;
import ghkg.domain.account.User;
import ghkg.dto.account.CreateUserResponse;
import ghkg.dto.account.UserSummaryResponse;
import ghkg.infrastructure.repository.UserRepository;
import ghkg.services.validation.PasswordValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final static String SUPER_ADMIN_USERNAME = "super";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PasswordValidationService passwordValidationService;

    public CreateUserResponse createUser(String username, String rawPassword, Set<Role> roles) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(rawPassword))
                .roles(roles)
                .build();

        userRepository.save(user);

        return new CreateUserResponse(
                "Created user: " + user.getUsername(),
                user.getRoles()
        );
    }


    public Optional<User> validateUser(String username, String rawPassword) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(rawPassword, user.getPassword()));
    }

    public List<UserSummaryResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserSummaryResponse(
                        user.getUsername(),
                        user.getRoles().stream()
                                .map(Enum::name)
                                .collect(Collectors.toSet())
                ))
                .toList();
    }

    public Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void addRoleToUser(String username, Role role) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
            userRepository.save(user);
        }
    }

    @Transactional
    public void updateUserRoles(String username, Set<Role> newRoles) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        if (SUPER_ADMIN_USERNAME.equalsIgnoreCase(user.getUsername()) && !newRoles.contains(Role.ADMIN)) {
            throw new CannotModifySuperAdminException("Cannot remove ADMIN role from the 'super admin' user.");
        }

        user.setRoles(newRoles);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserByUsername(String username) {
        if (SUPER_ADMIN_USERNAME.equalsIgnoreCase(username)) {
            throw new CannotModifySuperAdminException("Cannot delete the 'super admin' user.");
        }
        log.info("Deleting user: {}", username);
        userRepository.findByUsername(username)
                .ifPresentOrElse(
                        userRepository::delete,
                        () -> {
                            throw new UsernameNotFoundException("User '" + username + "' not found");
                        }
                );
    }

    @Transactional
    public void changeOwnPassword(String currentPassword, String newPassword) {
        User user = getAuthenticatedUser();

        passwordValidationService.validateCurrentPassword(user, currentPassword);
        passwordValidationService.validatePasswordChange(user, newPassword);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void updateUserPasswordByAdmin(String username, String newPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        passwordValidationService.validatePasswordChange(user, newPassword);

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No authenticated user found");
        }

        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
