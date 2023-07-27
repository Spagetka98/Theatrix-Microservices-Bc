package cz.osu.authenticationservice.service;

import cz.osu.authenticationservice.error.exception.AppUserNotFoundException;

public interface FilterService {

    /**
     * Try to validate user JWT token
     *
     * @param jwt the JWT token that will be used for validation
     * @return true if JWT is valid, false if parameter is null or token is not valid
     */
    boolean validateUserJWT(String jwt);

    /**
     * Get name of user from user JWT token
     *
     * @param jwt the JWT token that will be used to get username from it
     * @return a subject from JWT token (usually username)
     */
    String getNameFromJWT(String jwt);

    /**
     * Check if provided token is associated with the certain user.
     * Passed token is compared with user's access token saved in database.
     *
     * @param username the username parameter that will be used to find the certain user
     * @param jwt      the JWT token parameter that will be compared with the user's token
     * @return true if user's token is equal to passed JWT, otherwise false
     * @throws IllegalArgumentException if passed parameter is null or empty
     * @throws AppUserNotFoundException – if user was not found in database
     */
    boolean checkUsernameAndUserJWTAffiliation(String username, String jwt);
}
