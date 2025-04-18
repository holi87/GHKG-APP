package ghkg.dto.auth;

import ghkg.domain.auth.Role;

import java.util.Set;

public record CreateUserResponse(String message, Set<Role> roles) {
}
