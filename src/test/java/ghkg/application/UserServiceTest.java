package ghkg.application;

import ghkg.domain.Role;
import ghkg.domain.User;
import ghkg.dto.auth.CreateUserResponse;
import ghkg.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldCreateUserSuccessfullyWhenUsernameDoesNotExist() {
        // Arrange
        String username = "testUser";
        String rawPassword = "password123";
        Set<Role> roles = Set.of(Role.USER);
        String encodedPassword = "encodedPassword";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CreateUserResponse response = userService.createUser(username, rawPassword, roles);

        // Assert
        assertNotNull(response);
        assertEquals("Created user: testUser", response.message());
        assertEquals(roles, response.roles());

        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {
        // Arrange
        String username = "existingUser";
        String rawPassword = "password123";
        Set<Role> roles = Set.of(Role.USER);
        User existingUser = User.builder().username(username).build();

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.createUser(username, rawPassword, roles)
        );

        assertEquals("User already exists", exception.getMessage());

        verify(userRepository, times(1)).findByUsername(username);
        verify(passwordEncoder, never()).encode(rawPassword);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldEncodePasswordWhenCreatingUser() {
        // Arrange
        String username = "encodeCheckUser";
        String rawPassword = "password123";
        Set<Role> roles = Set.of(Role.USER);
        String encodedPassword = "encodedPassword";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertEquals(encodedPassword, user.getPassword());
            return user;
        });

        // Act
        userService.createUser(username, rawPassword, roles);

        // Assert
        verify(passwordEncoder, times(1)).encode(rawPassword);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldAssignRolesToNewUser() {
        // Arrange
        String username = "roleCheckUser";
        String rawPassword = "password123";
        Set<Role> roles = Set.of(Role.ADMIN, Role.WORKER);
        String encodedPassword = "encodedPassword";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            assertEquals(roles, user.getRoles());
            return user;
        });

        // Act
        userService.createUser(username, rawPassword, roles);

        // Assert
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(any(User.class));
    }
}