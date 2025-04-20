package ghkg.api;

import ghkg.application.UserService;
import ghkg.config.ApiPaths;
import ghkg.dto.MessageResponse;
import ghkg.dto.account.ChangeOwnPasswordRequest;
import ghkg.dto.account.CurrentUserResponse;
import ghkg.dto.account.LoginRequest;
import ghkg.dto.account.LoginResponse;
import ghkg.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(ApiPaths.ROOT)
@RequiredArgsConstructor
public class PublicAccountController {

    private final UserService userService;
    private final JwtService jwtService;


    @Operation(summary = "User login", tags = {"Public"})
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return userService.validateUser(request.username(), request.password())
                .map(user -> ResponseEntity.ok(new LoginResponse(jwtService.generateToken(user.getUsername()))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @Operation(summary = "Get current user info", tags = {"User"})
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> getCurrentUser() {
        return userService.getCurrentUser()
                .map(user -> ResponseEntity.ok(new CurrentUserResponse(user.getUsername(), user.getRoles())))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @Operation(summary = "Change own password", tags = {"User"})
    @PatchMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponse> changeOwnPassword(@RequestBody @Valid ChangeOwnPasswordRequest request) {
        userService.changeOwnPassword(request.currentPassword(), request.newPassword());
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new MessageResponse("Password changed. Please login again."));
    }
}