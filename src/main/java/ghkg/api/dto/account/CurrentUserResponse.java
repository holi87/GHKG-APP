package ghkg.api.dto.account;

import ghkg.api.domain.account.Role;

import java.util.Set;

public record CurrentUserResponse(
        String username,
        Set<Role> roles
) {
}