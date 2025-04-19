package ghkg.dto.auth;

import ghkg.domain.auth.Role;
import jakarta.validation.constraints.NotNull;

public record AddRoleRequest(
        @NotNull Role role) {
}