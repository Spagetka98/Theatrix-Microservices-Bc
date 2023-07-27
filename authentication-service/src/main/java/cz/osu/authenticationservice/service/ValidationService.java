package cz.osu.authenticationservice.service;

import cz.osu.authenticationservice.model.response.GatewayValidationResponse;

public interface ValidationService {
    /**
     * The user sends a personal jwt token that goes through the filters for authentication and if it's OK the service JWT token is sent as response
     *
     * @return information about user that will be used as headers (Token,Id,Roles)
     */
    GatewayValidationResponse sendServiceJWT();

    /**
     * Checks the username to see if it contains empty characters, if length is between 6 and 25 characters, if it contains only letters.
     * @param username to be checked
     * @throws cz.osu.authenticationservice.error.exception.IncorrectRegistrationInformation if it does not meet the conditions
     */
    void usernameCheck(String username);

    /**
     * Checks the email to see if it is valid.
     * @param email to be checked
     * @throws cz.osu.authenticationservice.error.exception.IncorrectRegistrationInformation if it does not meet the conditions
     */
    void emailCheck(String email);

    /**
     * Checks the password to see if it contains empty characters, if length is between 6 and 45 characters, if it contains at least one uppercase, one lowercase and one number.
     * @param password to be checked
     * @throws cz.osu.authenticationservice.error.exception.IncorrectRegistrationInformation if it does not meet the conditions
     */
    void passwordCheck(String password);
}
