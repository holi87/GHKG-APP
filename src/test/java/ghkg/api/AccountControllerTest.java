package ghkg.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ghkg.application.UserService;
import ghkg.application.validation.PasswordValidationService;
import ghkg.config.SecurityConfig;
import ghkg.domain.account.Role;
import ghkg.domain.account.User;
import ghkg.dto.account.*;
import ghkg.infrastructure.repository.UserRepository;
import ghkg.security.JwtService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WithMockUser(username = "admin_test", roles = {"ADMIN"})
@SpringBootTest(properties = "spring.sql.init.mode=never")
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class AccountControllerTest {

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private PasswordValidationService passwordValidationService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldLoginSuccessfully() throws Exception {
        LoginRequest request = new LoginRequest("user", "pass");
        User mockUser = new User();
        mockUser.setUsername("user");

        Mockito.when(userService.validateUser(eq("user"), eq("pass")))
                .thenReturn(Optional.of(mockUser));


        Mockito.when(jwtService.generateToken(eq("user"))).thenReturn("token");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @Test
    void shouldReturnUnauthorizedWhenLoginFails() throws Exception {
        LoginRequest request = new LoginRequest("user", "wrongpass");

        Mockito.when(userService.validateUser(eq("user"), eq("wrongpass")))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldCreateUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest("newuser", "password", Set.of(Role.USER));

        CreateUserResponse response = new CreateUserResponse("Created user: newuser", Set.of(Role.USER));

        Mockito.when(userService.createUser(any(), any(), any())).thenReturn(response);

        mockMvc.perform(post("/admin/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Created user: newuser"));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        UserSummaryResponse user1 = new UserSummaryResponse("admin", Set.of("ADMIN"));
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(user1));

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin"));
    }

    @Test
    void shouldAddRoleToUser() throws Exception {
        AddRoleRequest request = new AddRoleRequest(Role.WORKER);

        mockMvc.perform(patch("/admin/users/user123/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Added role WORKER to user: user123")));
    }

    @Test
    void shouldUpdateUserRoles() throws Exception {
        UpdateRolesRequest request = new UpdateRolesRequest(Set.of(Role.USER, Role.WORKER));

        mockMvc.perform(put("/admin/users/user123/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Updated roles for user: user123")));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/admin/users/user123"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User 'user123' deleted successfully")));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void shouldChangePasswordForSelf() throws Exception {
        ChangeOwnPasswordRequest request = new ChangeOwnPasswordRequest("oldPass", "newPass");

        mockMvc.perform(patch("/me/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password changed. Please login again."));
    }

    @Test
    void shouldAdminResetPassword() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest("newSecurePassword");

        mockMvc.perform(patch("/admin/users/admin/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password reset for user: admin"));
    }
}
