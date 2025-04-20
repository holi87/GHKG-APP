package ghkg.api.dto.account;

import ghkg.api.domain.account.Role;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record UpdateRolesRequest(
        @NotEmpty Set<Role> roles
) {
}
