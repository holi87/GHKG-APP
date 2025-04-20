package ghkg.api;

import ghkg.application.UserService;
import ghkg.config.ApiPaths;
import ghkg.dto.MessageResponse;
import ghkg.dto.account.*;
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
public class AdminAccountController {

    private final UserService userService;


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
    @DeleteMapping(ApiPaths.ADMIN_USERS + "/{username}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable String username) {
        userService.deleteUserByUsername(username);
        return ResponseEntity.ok(new MessageResponse("User '" + username + "' deleted successfully"));
    }

    @Operation(summary = "Admin updates user password", tags = {"Admin"})
    @PatchMapping(ApiPaths.ADMIN_USERS + "/{username}/password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> adminResetPassword(
            @PathVariable String username,
            @RequestBody @Valid ResetPasswordRequest request
    ) {
        userService.updateUserPasswordByAdmin(username, request.newPassword());
        return ResponseEntity.ok(new MessageResponse("Password reset for user: " + username));
    }
}