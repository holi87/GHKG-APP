package ghkg.dto.account;

import java.util.Set;

public record UserSummaryResponse(
        String username,
        Set<String> roles
) {
}