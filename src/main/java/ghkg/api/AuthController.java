package ghkg.api;

import ghkg.application.UserService;
import ghkg.dto.auth.CreateUserRequest;
import ghkg.dto.auth.LoginRequest;
import ghkg.dto.auth.LoginResponse;
import ghkg.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                .orElseGet(() -> ResponseEntity.status(401).build());
    }

    @PostMapping("/admin/users")
    public ResponseEntity<Void> createUser(@RequestBody CreateUserRequest request) {
        userService.createUser(request.username(), request.password(), request.roles());
        return ResponseEntity.ok().build();
    }
}
