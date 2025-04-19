package ghkg.dto.auth;

import ghkg.domain.auth.Role;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record UpdateRolesRequest(
        @NotEmpty Set<Role> roles
) {
}
