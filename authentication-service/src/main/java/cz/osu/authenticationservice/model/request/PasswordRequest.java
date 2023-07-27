package cz.osu.authenticationservice.model.request;

import javax.validation.constraints.NotBlank;

public record PasswordRequest(
        @NotBlank(message = "Current password cannot be blank") String currentPassword,
        @NotBlank(message = "New password cannot be blank") String newPassword
) {

}
