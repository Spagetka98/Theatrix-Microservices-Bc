package cz.osu.authenticationservice.service.tokenService;

import cz.osu.authenticationservice.error.exception.RefreshTokenException;
import cz.osu.authenticationservice.model.database.AppUser;
import cz.osu.authenticationservice.model.database.embedded.AccessToken;
import cz.osu.authenticationservice.model.database.embedded.RefreshToken;
import cz.osu.authenticationservice.properties.TheatrixProperties;
import cz.osu.authenticationservice.repository.AppUserRepository;
import cz.osu.authenticationservice.service.AppUserService;
import cz.osu.authenticationservice.service.TokenServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceUnitTests {
    @Mock
    private TheatrixProperties theatrixProperties;
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private AppUserService appUserService;
    @InjectMocks
    private TokenServiceImpl tokenService;

    @DisplayName("createAccessTokenForUser - Should create access Tokens for user")
    @Test
    void createAccessTokenForUser() {
        //region SET_UP
        AppUser testedUser = new AppUser("USERNAME", "EMAIL", "PASSWORD");
        testedUser.setId("TEST_ID");
        when(appUserService.getUserById(any())).thenReturn(testedUser);
        when(theatrixProperties.jwtSecretUser()).thenReturn("USER_SECRET");
        when(theatrixProperties.jwtAccessTokenExpirationMin()).thenReturn(5L);
        //endregion

        //region TESTING
        AccessToken result = tokenService.createAccessTokenForUser(testedUser.getId());
        //endregion

        //region RESULT_CHECK
        verify(appUserRepository, times(1)).save(any(AppUser.class));
        assertThat(result).isNotNull();
        //endregion
    }

    @DisplayName("createAccessTokenForUser - Should thrown IllegalArgumentException because passed userId is empty")
    @Test
    void createAccessTokenForUserWhenUserIdIsEmpty() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                tokenService.createAccessTokenForUser(""))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("createRefreshTokenForUser - Should create refresh token for user")
    @Test
    void createRefreshTokenForUser() {
        //region SET_UP
        String oldRefreshToken = "OLD";
        AppUser testedUser = new AppUser("USERNAME", "EMAIL", "PASSWORD");
        testedUser.setId("TEST_ID");
        testedUser.setRefreshToken(new RefreshToken(oldRefreshToken, Instant.now().minusSeconds(10 * 60)));
        when(appUserService.getUserById(any())).thenReturn(testedUser);
        //endregion

        //region TESTING
        RefreshToken result = tokenService.createRefreshTokenForUser(testedUser.getId());
        //endregion

        //region RESULT_CHECK
        verify(appUserRepository, times(1)).save(any(AppUser.class));
        assertThat(result.getUuidTokenValue()).isNotEqualTo(oldRefreshToken);
        //endregion
    }

    @DisplayName("getUsernameFromJWT - Should thrown IllegalArgumentException because passed jwt is empty")
    @Test
    void getUsernameFromJWTWhenJWTIsEmpty() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                tokenService.getUsernameFromJWT(""))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("generateAccessTokenForRefreshToken - Should create new access token for refresh token")
    @Test
    void generateAccessTokenForRefreshToken() {
        //region SET_UP
        String refreshToken = "REFRESH_TOKEN";
        AppUser testedUser = new AppUser("USERNAME", "EMAIL", "PASSWORD");
        testedUser.setId("TEST_ID");
        testedUser.setRefreshToken(new RefreshToken(refreshToken, Instant.now().plusSeconds(10 * 60)));
        when(appUserService.getUserById(testedUser.getId())).thenReturn(testedUser);
        when(appUserRepository.findByRefreshTokenUuidTokenValue(refreshToken)).thenReturn(Optional.of(testedUser));
        when(theatrixProperties.jwtSecretUser()).thenReturn("USER_SECRET");
        when(theatrixProperties.jwtAccessTokenExpirationMin()).thenReturn(5L);
        //endregion

        //region TESTING
        String result = tokenService.generateAccessTokenForRefreshToken(refreshToken);
        //endregion

        //region RESULT_CHECK
        assertThat(result).isNotBlank();
        //endregion
    }

    @DisplayName("generateAccessTokenForRefreshToken - Should throw RefreshTokenException because passed refresh token is empty")
    @Test
    void generateAccessTokenForRefreshTokenWhenTokenIsEmpty() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                tokenService.generateAccessTokenForRefreshToken("")
        ).isInstanceOf(RefreshTokenException.class);
        //endregion
    }

    @DisplayName("generateAccessTokenForRefreshToken - Should throw RefreshTokenException because user for passed refresh was not found")
    @Test
    void generateAccessTokenForRefreshTokenWhenUserWasNotFound() {
        //region SET_UP
        when(appUserRepository.findByRefreshTokenUuidTokenValue(any(String.class))).thenReturn(Optional.empty());
        //endregion
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                tokenService.generateAccessTokenForRefreshToken("IdUser")
        ).isInstanceOf(RefreshTokenException.class);
        //endregion
    }

    @DisplayName("generateAccessTokenForRefreshToken - Should throw RefreshTokenException because refresh token expired")
    @Test
    void generateAccessTokenForRefreshTokenWhenTokenExpired() {
        //region SET_UP
        String refreshToken = "REFRESH_TOKEN";
        AppUser testedUser = new AppUser("USERNAME", "EMAIL", "PASSWORD");
        testedUser.setId("TEST_ID");
        testedUser.setRefreshToken(new RefreshToken(refreshToken, Instant.now().minusSeconds(10 * 60)));
        when(appUserRepository.findByRefreshTokenUuidTokenValue(refreshToken)).thenReturn(Optional.of(testedUser));
        //endregion
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                tokenService.generateAccessTokenForRefreshToken(refreshToken)
        ).isInstanceOf(RefreshTokenException.class);
        //endregion
    }

    @DisplayName("isUserRefreshTokenExpired - Should return true because token is expired")
    @Test
    void isUserRefreshTokenExpired() {
        //region SET_UP
        AppUser testedUser = new AppUser("USERNAME", "EMAIL", "PASSWORD");
        testedUser.setId("TEST_ID");
        testedUser.setRefreshToken(new RefreshToken("UUID", Instant.now().minusSeconds(10 * 60)));
        //endregion

        //region TESTING
        boolean result = tokenService.isUserRefreshTokenExpired(testedUser);
        //endregion

        //region RESULT_CHECK
        assertThat(result).isTrue();
        //endregion
    }

    @DisplayName("isUserRefreshTokenExpired - Should throw IllegalArgumentException because passed user is null")
    @Test
    void isUserRefreshTokenExpiredWhenUserIsNull() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                tokenService.isUserRefreshTokenExpired(null)
        ).isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

}
