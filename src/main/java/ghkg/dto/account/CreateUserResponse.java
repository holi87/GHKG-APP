package ghkg.dto.account;

import ghkg.domain.account.Role;

import java.util.Set;

public record CreateUserResponse(String message, Set<Role> roles) {
}
