package ghkg.dto.auth;

import ghkg.domain.auth.Role;

import java.util.Set;

public record CurrentUserResponse(
        String username,
        Set<Role> roles
) {
}