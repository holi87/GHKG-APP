package ghkg.application;

import ghkg.api.exception.CannotModifySuperAdminException;
import ghkg.domain.auth.Role;
import ghkg.domain.auth.User;
import ghkg.dto.auth.CreateUserResponse;
import ghkg.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUser() {
        String username = "test";
        String rawPassword = "pass";
        Set<Role> roles = Set.of(Role.USER);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn("hashed");

        CreateUserResponse response = userService.createUser(username, rawPassword, roles);

        assertThat(response.message()).isEqualTo("Created user: test");
        assertThat(response.roles()).contains(Role.USER);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldValidateUser() {
        String username = "user";
        String password = "pass";
        String hashed = "hashed";

        User user = User.builder().username(username).password(hashed).roles(Set.of(Role.USER)).build();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, hashed)).thenReturn(true);

        Optional<User> result = userService.validateUser(username, password);

        assertThat(result).isPresent();
    }

    @Test
    void shouldGetCurrentUser() {
        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("user");

        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userRepository.findByUsername("user"))
                .thenReturn(Optional.of(new User()));

        assertThat(userService.getCurrentUser()).isPresent();
    }

    @Test
    void shouldThrowWhenRemovingAdminRoleFromSuperAdmin() {
        User admin = User.builder()
                .username("admin")
                .roles(new HashSet<>(Set.of(Role.ADMIN)))
                .build();

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));

        assertThatThrownBy(() ->
                userService.updateUserRoles("admin", Set.of(Role.USER))
        ).isInstanceOf(CannotModifySuperAdminException.class);
    }

    @Test
    void shouldThrowWhenDeletingSuperAdmin() {
        assertThatThrownBy(() ->
                userService.deleteUserByUsername("admin")
        ).isInstanceOf(CannotModifySuperAdminException.class);
    }
}
