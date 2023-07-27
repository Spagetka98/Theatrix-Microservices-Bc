package cz.osu.authenticationservice.service;

import cz.osu.authenticationservice.error.exception.IncorrectRegistrationInformation;
import cz.osu.authenticationservice.model.database.AppUser;
import cz.osu.authenticationservice.model.enums.EStatusErrors;
import cz.osu.authenticationservice.model.response.GatewayValidationResponse;
import cz.osu.authenticationservice.utility.ExceptionUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {
    private final AppUserService appUserServiceImpl;

    /**
     * @see ValidationService#sendServiceJWT()
     */
    @Override
    public GatewayValidationResponse sendServiceJWT() {
        AppUser authUser = appUserServiceImpl.getAuthenticatedUser();

        return new GatewayValidationResponse(
                authUser.getUsername(),
                authUser.getId(),
                authUser.getRoles().stream().map((role -> role.getName().name())).collect(Collectors.toSet())
        );
    }

    @Override
    public void usernameCheck(String username) {
        ExceptionUtility.checkInput(username,"Parameter username in ValidationServiceImpl.isUsernameValid cannot be null or empty!");

        if (!username.replaceAll("\\s+","").equals(username))
            throw new IncorrectRegistrationInformation(EStatusErrors.NAME_EMPTY_CHARS.getValue());

        if (username.length() < 5 || username.length() > 25)
            throw new IncorrectRegistrationInformation(EStatusErrors.NAME_INSUFFICIENT_LENGTH.getValue());

        Pattern p = Pattern.compile("[a-zA-Z]+");
        if (!p.matcher(username).matches())
            throw new IncorrectRegistrationInformation(EStatusErrors.NAME_INVALID_CHARS.getValue());
    }

    @Override
    public void emailCheck(String email) {
        ExceptionUtility.checkInput(email,"Parameter email in ValidationServiceImpl.isEmailValid cannot be null or empty!");

        Pattern p = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
        if (!p.matcher(email).matches())
            throw new IncorrectRegistrationInformation(EStatusErrors.EMAIL_INVALID_CHARS.getValue());
    }

    @Override
    public void passwordCheck(String password) {
        ExceptionUtility.checkInput(password,"Parameter password in ValidationServiceImpl.isPasswordValid cannot be null or empty!");

        if (!password.replaceAll("\\s+","").equals(password))
            throw new IncorrectRegistrationInformation(EStatusErrors.PASSWORD_EMPTY_CHARS.getValue());

        if (password.length() < 6 || password.length() > 45)
            throw new IncorrectRegistrationInformation(EStatusErrors.PASSWORD_INSUFFICIENT_LENGTH.getValue());

        Pattern p = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,45}$");
        if (!p.matcher(password).matches())
            throw new IncorrectRegistrationInformation(EStatusErrors.PASSWORD_INVALID_CHARS.getValue());
    }


}
