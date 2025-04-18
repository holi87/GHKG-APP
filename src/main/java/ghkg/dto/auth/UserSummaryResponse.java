package ghkg.dto.auth;

import java.util.Set;

public record UserSummaryResponse(
        String username,
        Set<String> roles
) {
}