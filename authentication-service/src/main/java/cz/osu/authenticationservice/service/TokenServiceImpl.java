package cz.osu.authenticationservice.service;

import cz.osu.authenticationservice.error.exception.RefreshTokenException;
import cz.osu.authenticationservice.model.database.AppUser;
import cz.osu.authenticationservice.model.database.embedded.AccessToken;
import cz.osu.authenticationservice.model.database.embedded.RefreshToken;
import cz.osu.authenticationservice.properties.TheatrixProperties;
import cz.osu.authenticationservice.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import cz.osu.authenticationservice.utility.ExceptionUtility;
import cz.osu.authenticationservice.utility.JwtUtility;

import java.time.*;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TheatrixProperties theatrixProperties;
    private final AppUserRepository appUserRepository;
    private final AppUserService appUserServiceImpl;

    /**
     * @see TokenServiceImpl#createAccessTokenForUser(String)
     */
    @Override
    public AccessToken createAccessTokenForUser(String userId) {
        ExceptionUtility.checkInput(userId, "Parameter userId in TokenServiceImpl.createAccessTokenForUser cannot be null or empty !");

        AppUser user = appUserServiceImpl.getUserById(userId);

        AccessToken accessToken = user.getAccessToken();

        if (!checkAccessTokens(accessToken)) {
            AccessToken newAccessToken = generateAccessTokensForUser(user);

            user.setAccessToken(newAccessToken);

            appUserRepository.save(user);
        }

        return user.getAccessToken();
    }

    /**
     * @see TokenServiceImpl#createRefreshTokenForUser(String)
     */
    @Override
    public RefreshToken createRefreshTokenForUser(String userId) {
        ExceptionUtility.checkInput(userId, "Parameter userId in TokenServiceImpl.createRefreshTokenForUser cannot be null or empty !");

        AppUser user = appUserServiceImpl.getUserById(userId);

        RefreshToken refreshToken = user.getRefreshToken();

        if (refreshToken == null || refreshToken.getExpiryDate() == null || refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
            RefreshToken newRefreshToken = new RefreshToken(
                    UUID.randomUUID().toString(),
                    Instant.now().plusSeconds(theatrixProperties.jwtRefreshTokenExpirationMin() * 60)
            );

            user.setRefreshToken(newRefreshToken);

            appUserRepository.save(user);

            return newRefreshToken;
        }

        return refreshToken;
    }

    /**
     * @see TokenServiceImpl#validateUserJWT(String)
     */
    @Override
    public boolean validateUserJWT(String jwt) {
        return JwtUtility.validateJwtToken(jwt, theatrixProperties.jwtSecretUser());
    }

    /**
     * @see TokenServiceImpl#getUsernameFromJWT(String)
     */
    @Override
    public String getUsernameFromJWT(String jwt) {
        ExceptionUtility.checkInput(jwt, "Parameter JWT in TokenServiceImpl.getUsernameFromJWT cannot be null or empty!");

        return JwtUtility.getUsernameFromJwtToken(jwt, theatrixProperties.jwtSecretUser());
    }

    /**
     * @see TokenServiceImpl#generateAccessTokenForRefreshToken(String)
     */
    @Override
    public String generateAccessTokenForRefreshToken(String refreshToken) {
        if (refreshToken == null || refreshToken.trim().isEmpty())
            throw new RefreshTokenException("Invalid refresh token!");

        AppUser user = appUserRepository.findByRefreshTokenUuidTokenValue(refreshToken)
                .orElseThrow(() -> new RefreshTokenException("Invalid refresh token !"));

        if (this.isUserRefreshTokenExpired(user))
            throw new RefreshTokenException("Invalid refresh token !");

        return this.createAccessTokenForUser(user.getId()).getTokenValue();
    }

    /**
     * @see TokenServiceImpl#isUserRefreshTokenExpired(AppUser)
     */
    @Override
    public boolean isUserRefreshTokenExpired(AppUser user) {
        ExceptionUtility.checkInput(user, "Parameter AppUser can not be null !");

        if (user.getRefreshToken().getExpiryDate().compareTo(Instant.now()) < 0) {

            user.setRefreshToken(new RefreshToken());
            appUserRepository.save(user);

            return true;
        }

        return false;
    }

    /**
     * Check user's tokens if both are valid
     *
     * @param accessToken - tokens of user to be checked
     * @return false if tokens are invalid or null, otherwise true
     */
    private boolean checkAccessTokens(AccessToken accessToken) {
        if (accessToken == null) return false;

        return JwtUtility.validateJwtToken(accessToken.getTokenValue(), theatrixProperties.jwtSecretUser());
    }

    /**
     * Create new access tokens
     *
     * @param user - the user for whom tokens are to be created
     * @return personal access token for authentication and service token for communication across services
     * @throws IllegalArgumentException if passed parameter is null
     */
    private AccessToken generateAccessTokensForUser(AppUser user) {
        ExceptionUtility.checkInput(user, "Parameter user in TokenServiceImpl.createAccessTokenForUser cannot be null !");

        Date expiration = Date.from(Instant.now().plusSeconds(theatrixProperties.jwtAccessTokenExpirationMin() * 60));

        return new AccessToken(
                JwtUtility.generateJwtToken(user.getUsername(), theatrixProperties.jwtSecretUser(), expiration)
        );
    }
}
