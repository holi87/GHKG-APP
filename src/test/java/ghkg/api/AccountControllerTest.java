package ghkg.api;

import ghkg.application.UserService;
import ghkg.domain.account.Role;
import ghkg.domain.account.User;
import ghkg.dto.MessageResponse;
import ghkg.dto.account.*;
import ghkg.security.JwtService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    private UserService userService;
    private JwtService jwtService;
    private AccountController accountController;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        jwtService = mock(JwtService.class);
        accountController = new AccountController(userService, jwtService);
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        String username = "admin";
        String password = "password";
        String token = "jwt.token";

        User user = User.builder().username(username).roles(Set.of(Role.ADMIN)).build();

        when(userService.validateUser(username, password)).thenReturn(Optional.of(user));
        when(jwtService.generateToken(username)).thenReturn(token);

        LoginRequest request = new LoginRequest(username, password);
        ResponseEntity<LoginResponse> response = accountController.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().token()).isEqualTo(token);
    }

    @Test
    void login_shouldReturnUnauthorized_whenInvalidCredentials() {
        String username = "admin";
        String password = "wrong";

        when(userService.validateUser(username, password)).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest(username, password);
        ResponseEntity<LoginResponse> response = accountController.login(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void createUser_shouldReturn201AndResponse() {
        CreateUserRequest request = new CreateUserRequest("user", "pass", Set.of(Role.USER));
        CreateUserResponse responseMock = new CreateUserResponse("Created user", Set.of(Role.USER));

        when(userService.createUser("user", "pass", Set.of(Role.USER))).thenReturn(responseMock);

        ResponseEntity<CreateUserResponse> response = accountController.createUser(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().message()).contains("Created");
    }

    @Test
    void getAllUsers_shouldReturnList() {
        List<UserSummaryResponse> expected = List.of(
                new UserSummaryResponse("admin", Set.of("ADMIN")),
                new UserSummaryResponse("user", Set.of("USER"))
        );

        when(userService.getAllUsers()).thenReturn(expected);

        List<UserSummaryResponse> result = accountController.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).username()).isEqualTo("admin");
    }

    @Test
    void getCurrentUser_shouldReturnUser_whenAuthenticated() {
        User user = User.builder().username("admin").roles(Set.of(Role.ADMIN)).build();
        when(userService.getCurrentUser()).thenReturn(Optional.of(user));

        ResponseEntity<CurrentUserResponse> response = accountController.getCurrentUser();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().username()).isEqualTo("admin");
    }

    @Test
    void getCurrentUser_shouldReturnUnauthorized_whenNotAuthenticated() {
        when(userService.getCurrentUser()).thenReturn(Optional.empty());

        ResponseEntity<CurrentUserResponse> response = accountController.getCurrentUser();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void addRole_shouldReturnOkMessage() {
        AddRoleRequest request = new AddRoleRequest(Role.WORKER);

        ResponseEntity<MessageResponse> response = accountController.addRole("user", request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().message()).contains("Added role");

        verify(userService).addRoleToUser("user", Role.WORKER);
    }

    @Test
    void updateRoles_shouldReturnOkMessage() {
        Set<Role> newRoles = Set.of(Role.USER, Role.WORKER);
        UpdateRolesRequest request = new UpdateRolesRequest(newRoles);

        ResponseEntity<MessageResponse> response = accountController.updateRoles("user", request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().message()).contains("Updated roles");

        verify(userService).updateUserRoles("user", newRoles);
    }

    @Test
    void deleteUser_shouldReturnOkMessage() {
        ResponseEntity<MessageResponse> response = accountController.deleteUser("user");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertNotNull(response.getBody());
        assertThat(response.getBody().message()).contains("deleted");

        verify(userService).deleteUserByUsername("user");
    }
}
