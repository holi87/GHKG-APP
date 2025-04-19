package ghkg.dto.account;

import ghkg.domain.account.Role;
import jakarta.validation.constraints.NotNull;

public record AddRoleRequest(
        @NotNull Role role) {
}