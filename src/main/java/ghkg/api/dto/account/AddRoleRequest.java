package ghkg.api.dto.account;

import ghkg.api.domain.account.Role;
import jakarta.validation.constraints.NotNull;

public record AddRoleRequest(
        @NotNull Role role) {
}