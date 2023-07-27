package cz.osu.authenticationservice.service;

import cz.osu.authenticationservice.error.exception.RoleNotFoundException;
import cz.osu.authenticationservice.error.exception.UserInformationTakenException;
import cz.osu.authenticationservice.model.response.LoginResponse;
import org.springframework.security.authentication.BadCredentialsException;

public interface AuthenticationService {
    /**
     * It will try to authenticate user with AuthenticationManager.authenticate method
     * to which is passed UsernamePasswordAuthenticationToken to the AuthenticationProvider,
     * which will use the userDetailsService to get a user by his username and compare his password
     * with hashed password in UsernamePasswordAuthenticationToken.
     *
     * @param username the username parameter that will be used for authentication
     * @param password the password parameter that will be user for authentication
     * @return user information (his username, email, roles, accessToken, refreshToken)
     * @throws BadCredentialsException if passed credentials are incorrect or empty or null
     */
    LoginResponse authUser(String username, String password);

    /**
     * It will try to create a new user in database with basic role USER.
     *
     * @param username the username parameter that will be used as user's account name
     * @param email    the email parameter that will be used as user's account
     * @param password the password parameter that will be used for account's access
     * @throws IllegalArgumentException      if passed parameter is null or empty
     * @throws UserInformationTakenException if passed parameter (username or email) is already assign to another user
     * @throws RoleNotFoundException         if basic role USER was not found
     */
    void registerUser(String username, String email, String password);

    /**
     * It will remove all user tokens (access and refresh token) so they won't be able to be used for authentication no longer.
     */
    void logout();
}
