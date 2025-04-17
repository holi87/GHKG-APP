package ghkg.api;

import ghkg.application.UserService;
import ghkg.dto.auth.CreateUserRequest;
import ghkg.dto.auth.CreateUserResponse;
import ghkg.dto.auth.LoginRequest;
import ghkg.dto.auth.LoginResponse;
import ghkg.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return userService.validateUser(request.username(), request.password())
                .map(user -> ResponseEntity.ok(new LoginResponse(jwtService.generateToken(user.getUsername()))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        CreateUserResponse response = userService.createUser(
                request.username(), request.password(), request.roles()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}