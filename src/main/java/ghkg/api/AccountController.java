package ghkg.api;

import ghkg.application.UserService;
import ghkg.config.ApiPaths;
import ghkg.dto.MessageResponse;
import ghkg.dto.account.*;
import ghkg.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final JwtService jwtService;


    @Operation(summary = "User login", tags = {"Public"})
    @PostMapping(ApiPaths.AUTH + "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return userService.validateUser(request.username(), request.password())
                .map(user -> ResponseEntity.ok(new LoginResponse(jwtService.generateToken(user.getUsername()))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @Operation(summary = "Create new user", tags = {"Admin"})
    @PostMapping(ApiPaths.ADMIN_USERS)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateUserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        CreateUserResponse response = userService.createUser(
                request.username(), request.password(), request.roles()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Get all users", tags = {"Admin"})
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(ApiPaths.ADMIN_USERS)
    public List<UserSummaryResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Get current user info", tags = {"User"})
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> getCurrentUser() {
        return userService.getCurrentUser()
                .map(user -> ResponseEntity.ok(new CurrentUserResponse(user.getUsername(), user.getRoles())))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @Operation(summary = "Add role to user", tags = {"Admin"})
    @PatchMapping(ApiPaths.ADMIN_USERS + "/{username}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> addRole(
            @PathVariable String username,
            @RequestBody @Valid AddRoleRequest request
    ) {
        userService.addRoleToUser(username, request.role());
        return ResponseEntity.ok(new MessageResponse("Added role " + request.role() + " to user: " + username));
    }

    @Operation(summary = "Edit roles for user", tags = {"Admin"})
    @PutMapping(ApiPaths.ADMIN_USERS + "/{username}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> updateRoles(
            @PathVariable String username,
            @RequestBody @Valid UpdateRolesRequest request
    ) {
        userService.updateUserRoles(username, request.roles());
        return ResponseEntity.ok(new MessageResponse("Updated roles for user: " + username));
    }

    @Operation(summary = "Delete an user", tags = {"Admin"})
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/users/{username}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok(new MessageResponse("User '" + username + "' deleted successfully"));
    }
}