package cz.osu.authenticationservice.service.filterService;

import cz.osu.authenticationservice.model.database.AppUser;
import cz.osu.authenticationservice.model.database.embedded.AccessToken;
import cz.osu.authenticationservice.service.AppUserService;
import cz.osu.authenticationservice.service.FilterServiceImpl;
import cz.osu.authenticationservice.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FilterServiceUnitTests {
    @Mock
    private TokenService tokenService;
    @Mock
    private AppUserService appUserServiceImpl;
    @InjectMocks
    private FilterServiceImpl filterService;

    @DisplayName("validateUserJWT - Should return true if token is valid")
    @Test
    void validateUserJWT() {
        //region SET_UP
        when(tokenService.validateUserJWT(any())).thenReturn(true);
        //endregion

        //region TESTING
        boolean result = filterService.validateUserJWT("test");
        //endregion

        //region RESULT_CHECK
        assertThat(result).isTrue();
        //endregion
    }

    @DisplayName("validateUserJWT - Should return false if token is null")
    @Test
    void validateUserJWTWhenTokenIsNull() {
        //region TESTING
        boolean result = filterService.validateUserJWT(null);
        //endregion

        //region RESULT_CHECK
        assertThat(result).isFalse();
        //endregion
    }

    @DisplayName("getNameFromJWT - Should return name from JWT")
    @Test
    void getNameFromJWT() {
        //region SET_UP
        String expected = "NAME";
        when(tokenService.getUsernameFromJWT(any())).thenReturn(expected);
        //endregion

        //region TESTING
        String result = filterService.getNameFromJWT("test");
        //endregion

        //region RESULT_CHECK
        assertThat(result).isEqualTo(expected);
        //endregion
    }

    @DisplayName("getNameFromJWT - Should throw IllegalArgumentException because JWT is null")
    @Test
    void getNameFromJWTWhenJWTIsNull() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                filterService.getNameFromJWT(null))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("checkUsernameAndUserJWTAffiliation - Should check if token belongs to the user and return true")
    @Test
    void checkUsernameAndUserJWTAffiliation() {
        //region SET_UP
        AppUser testedUser = new AppUser("USERNAME", "EMAIL", "PASSWORD");
        AccessToken testedToken = new AccessToken("USER_TOKEN");
        testedUser.setAccessToken(testedToken);

        when(appUserServiceImpl.getUserByUsername(any())).thenReturn(testedUser);
        //endregion

        //region TESTING
        boolean result = filterService.checkUsernameAndUserJWTAffiliation(testedUser.getUsername(), testedToken.getTokenValue());
        //endregion

        //region RESULT_CHECK
        assertThat(result).isTrue();
        //endregion
    }

    @DisplayName("checkUsernameAndUserJWTAffiliation - Should throw IllegalArgumentException because parameter username is empty string")
    @Test
    void checkUsernameAndUserJWTAffiliationWhenUsernameIsEmpty() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                filterService.checkUsernameAndUserJWTAffiliation("", "JWT"))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("checkUsernameAndUserJWTAffiliation - Should throw IllegalArgumentException because parameter JWT is empty string")
    @Test
    void checkUsernameAndUserJWTAffiliationWhenJWTIsEmpty() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                filterService.checkUsernameAndUserJWTAffiliation("USERNAME", ""))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }
}
