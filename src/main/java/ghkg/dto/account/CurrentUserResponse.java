package ghkg.dto.account;

import ghkg.domain.account.Role;

import java.util.Set;

public record CurrentUserResponse(
        String username,
        Set<Role> roles
) {
}