package ghkg.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ghkg.config.ApiPaths;
import ghkg.config.SecurityConfig;
import ghkg.domain.account.User;
import ghkg.dto.account.ChangeOwnPasswordRequest;
import ghkg.dto.account.LoginRequest;
import ghkg.infrastructure.repository.UserRepository;
import ghkg.security.JwtService;
import ghkg.services.UserService;
import ghkg.services.validation.PasswordValidationService;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WithMockUser(username = "admin_test", roles = {"ADMIN"})
@SpringBootTest(properties = "spring.sql.init.mode=never")
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
class UserAccountControllerTest {

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

        mockMvc.perform(post(ApiPaths.ROOT + "/login")
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

        mockMvc.perform(post(ApiPaths.ROOT + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }


    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void shouldChangePasswordForSelf() throws Exception {
        ChangeOwnPasswordRequest request = new ChangeOwnPasswordRequest("oldPass", "newPass");

        mockMvc.perform(patch(ApiPaths.ROOT + "/me/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password changed. Please login again."));
    }


}
