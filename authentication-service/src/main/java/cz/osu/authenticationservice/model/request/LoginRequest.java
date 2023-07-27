package cz.osu.authenticationservice.model.request;

import javax.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Username cannot be blank") String username,
        @NotBlank(message = "Password cannot be blank") String password) {
}
