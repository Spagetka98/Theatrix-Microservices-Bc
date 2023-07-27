package cz.osu.authenticationservice.service;

import cz.osu.authenticationservice.error.exception.AppUserNotFoundException;
import cz.osu.authenticationservice.error.exception.RefreshTokenException;
import cz.osu.authenticationservice.model.database.AppUser;
import cz.osu.authenticationservice.model.database.embedded.AccessToken;
import cz.osu.authenticationservice.model.database.embedded.RefreshToken;

public interface TokenService {
    /**
     * It will create a personal access token for authentication of user and service access token for communication across services.
     * If user already has tokens, it will check if they are still valid.
     * If they're still valid, it will return them and if not they will create new ones.
     *
     * @param idUser the id that will be used to find the user
     * @return tokens for authentication across services
     * @throws IllegalArgumentException – if passed parameter is null or empty
     * @throws AppUserNotFoundException – if user with passed id was not found in database
     */
    AccessToken createAccessTokenForUser(String idUser);

    /**
     * It will create a refresh token for user. This token can be used to get a JWT token.
     * If user already has refresh token, it will check if it is still valid.
     * If it is expired, it will send new one and if not it will send current user's refresh token.
     *
     * @param idUser the id of user
     * @return user's personal refresh token
     * @throws IllegalArgumentException - if passed parameter is null or empty
     * @throws AppUserNotFoundException - if user with passed id was not found in database
     */
    RefreshToken createRefreshTokenForUser(String idUser);

    /**
     * It will validate user JWT token.
     *
     * @param jwt the personal JWT token used for authentication
     * @return true if JWT is valid, otherwise false
     */
    boolean validateUserJWT(String jwt);

    /**
     * It will get a username from user JWT token.
     *
     * @param jwt the personal JWT token of user
     * @return subject from JWT
     * @throws IllegalArgumentException if passed parameter is null or empty
     */
    String getUsernameFromJWT(String jwt);

    /**
     * It will check user's refresh token and if it is valid, it will create new access tokens and save them to the database.
     *
     * @param refreshToken user's personal refresh token
     * @return personal access JWT token that is used for authentication
     * @throws RefreshTokenException if passed parameter is null or empty or if refresh token is expired or token could not be found in database
     */
    String generateAccessTokenForRefreshToken(String refreshToken);

    /**
     * Validate token and if it is expired, it will remove refresh token from database.
     *
     * @param user the user whose token is to be checked
     * @return true if token is expired, otherwise false
     * @throws IllegalArgumentException if passed parameter is null
     */
    boolean isUserRefreshTokenExpired(AppUser user);
}
