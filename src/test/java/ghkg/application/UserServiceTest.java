package ghkg.application;

import ghkg.api.exception.CannotModifySuperAdminException;
import ghkg.application.validation.PasswordValidationService;
import ghkg.domain.account.Role;
import ghkg.domain.account.User;
import ghkg.dto.account.CreateUserResponse;
import ghkg.dto.account.UserSummaryResponse;
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
import java.util.List;
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

    @Mock
    private PasswordValidationService passwordValidationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateUser() {
        String username = "john";
        String rawPassword = "pass";
        Set<Role> roles = Set.of(Role.USER);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn("encoded");

        CreateUserResponse response = userService.createUser(username, rawPassword, roles);

        assertThat(response.message()).isEqualTo("Created user: john");
        assertThat(response.roles()).containsExactly(Role.USER);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowWhenCreatingDuplicateUser() {
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(mock(User.class)));
        assertThatThrownBy(() -> userService.createUser("john", "pw", Set.of(Role.USER)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldValidateUserWithCorrectPassword() {
        User user = User.builder().username("john").password("encoded").build();
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("raw", "encoded")).thenReturn(true);

        Optional<User> result = userService.validateUser("john", "raw");

        assertThat(result).isPresent().contains(user);
    }

    @Test
    void shouldReturnUserSummaries() {
        User user = User.builder().username("john").roles(Set.of(Role.ADMIN)).build();
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserSummaryResponse> list = userService.getAllUsers();

        assertThat(list).hasSize(1);
        assertThat(list.get(0).username()).isEqualTo("john");
        assertThat(list.get(0).roles()).containsExactly("ADMIN");
    }

    @Test
    void shouldAddRoleToUserIfNotPresent() {
        User user = User.builder().username("john").roles(new HashSet<>()).build();
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        userService.addRoleToUser("john", Role.WORKER);

        assertThat(user.getRoles()).contains(Role.WORKER);
        verify(userRepository).save(user);
    }

    @Test
    void shouldUpdateRolesUnlessAdminRemovedFromAdminUser() {
        User user = User.builder().username("super").roles(Set.of(Role.ADMIN)).build();
        when(userRepository.findByUsername("super")).thenReturn(Optional.of(user));

        Set<Role> roles = Set.of(Role.USER);
        assertThatThrownBy(() -> userService.updateUserRoles("super", roles))
                .isInstanceOf(CannotModifySuperAdminException.class);
    }

    @Test
    void shouldUpdateUserRoles() {
        User user = User.builder().username("john").roles(new HashSet<>()).build();
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        userService.updateUserRoles("john", Set.of(Role.USER, Role.WORKER));

        assertThat(user.getRoles()).containsExactlyInAnyOrder(Role.USER, Role.WORKER);
        verify(userRepository).save(user);
    }

    @Test
    void shouldDeleteUserIfExists() {
        User user = User.builder().username("john").build();
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        userService.deleteUserByUsername("john");

        verify(userRepository).delete(user);
    }

    @Test
    void shouldNotAllowDeletingAdmin() {
        assertThatThrownBy(() -> userService.deleteUserByUsername("super"))
                .isInstanceOf(CannotModifySuperAdminException.class);
    }

    @Test
    void shouldChangeOwnPassword() {
        User user = User.builder().username("john").password("old").build();

        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getName()).thenReturn("john");
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("new")).thenReturn("encoded");

        userService.changeOwnPassword("old", "new");

        verify(passwordValidationService).validateCurrentPassword(user, "old");
        verify(passwordValidationService).validatePasswordChange(user, "new");
        verify(userRepository).save(user);
    }

    @Test
    void shouldUpdatePasswordByAdmin() {
        User user = User.builder().username("john").build();
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("new")).thenReturn("encoded");

        userService.updateUserPasswordByAdmin("john", "new");

        verify(passwordValidationService).validatePasswordChange(user, "new");
        verify(userRepository).save(user);
    }
}
