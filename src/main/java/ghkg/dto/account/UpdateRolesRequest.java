package ghkg.dto.account;

import ghkg.domain.account.Role;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record UpdateRolesRequest(
        @NotEmpty Set<Role> roles
) {
}
