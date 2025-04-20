package ghkg.api.dto.account;

import ghkg.api.domain.account.Role;

import java.util.Set;

public record CreateUserResponse(String message, Set<Role> roles) {
}
