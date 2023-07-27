package cz.osu.authenticationservice.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank(message = "Username cannot be blank")
        @Size(min = 5, max = 25, message = "Minimum size of username is 5 and Maximum size of username is 25")
        String username,

        @NotBlank(message = "Email cannot be blank")
        @Size(max = 40, message = "Maximum size of email is 40")
        @Email(message = "Email of AppUser is not valid")
        String email,

        @NotBlank(message = "Password cannot be blank")
        @Size(min = 6, max = 40, message = "Minimum size of password is 6 and Maximum size is 40")
        String password
) {
}
