package ghkg.api.dto.account;

import jakarta.validation.constraints.NotBlank;

public record ChangeOwnPasswordRequest(@NotBlank String currentPassword, @NotBlank String newPassword) {
}
