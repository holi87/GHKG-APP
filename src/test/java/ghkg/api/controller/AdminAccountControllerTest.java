package ghkg.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import ghkg.api.config.SecurityConfig;
import ghkg.api.domain.account.Role;
import ghkg.api.domain.account.User;
import ghkg.api.dto.account.*;
import ghkg.api.infrastructure.repository.UserRepository;
import ghkg.api.security.JwtService;
import ghkg.api.services.UserService;
import ghkg.api.services.validation.PasswordValidationService;
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

import static ghkg.api.config.ApiPaths.ADMIN_USERS;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WithMockUser(username = "admin_test", roles = {"ADMIN"})
@SpringBootTest(properties = "spring.sql.init.mode=never")
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class AdminAccountControllerTest {

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
    void shouldCreateUser() throws Exception {
        CreateUserRequest request = new CreateUserRequest("newuser", "password", Set.of(Role.USER));

        CreateUserResponse response = new CreateUserResponse("Created user: newuser", Set.of(Role.USER));

        Mockito.when(userService.createUser(any(), any(), any())).thenReturn(response);

        mockMvc.perform(post(ADMIN_USERS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Created user: newuser"));
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        UserSummaryResponse user1 = new UserSummaryResponse("admin_test", Set.of("ADMIN"));
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(user1));

        mockMvc.perform(get(ADMIN_USERS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("admin_test"));
    }

    @Test
    void shouldAddRoleToUser() throws Exception {
        AddRoleRequest request = new AddRoleRequest(Role.WORKER);

        mockMvc.perform(patch(ADMIN_USERS + "/user123/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Added role WORKER to user: user123")));
    }

    @Test
    void shouldUpdateUserRoles() throws Exception {
        UpdateRolesRequest request = new UpdateRolesRequest(Set.of(Role.USER, Role.WORKER));

        mockMvc.perform(put(ADMIN_USERS + "/user123/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Updated roles for user: user123")));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        User user = new User();
        user.setUsername("user123");

        Mockito.when(userRepository.findByUsername("user123"))
                .thenReturn(Optional.of(user));

        mockMvc.perform(delete(ADMIN_USERS + "/user123"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("User 'user123' deleted successfully")));
    }

    @Test
    void shouldAdminResetPassword() throws Exception {
        ResetPasswordRequest request = new ResetPasswordRequest("newSecurePassword");

        mockMvc.perform(patch(ADMIN_USERS + "/admin/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password reset for user: admin"));
    }
}
