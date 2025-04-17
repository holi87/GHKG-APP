package ghkg.dto.auth;

import ghkg.domain.Role;

import java.util.Set;

public record CreateUserRequest(String username, String password, Set<Role> roles) {
}
