package cz.osu.authenticationservice.service.authenticationService;

import cz.osu.authenticationservice.model.database.AppUser;
import cz.osu.authenticationservice.model.database.embedded.AccessToken;
import cz.osu.authenticationservice.model.database.embedded.RefreshToken;
import cz.osu.authenticationservice.model.enums.ERole;
import cz.osu.authenticationservice.model.response.LoginResponse;
import cz.osu.authenticationservice.repository.AppUserRepository;
import cz.osu.authenticationservice.repository.RoleRepository;
import cz.osu.authenticationservice.security.userDetails.UserDetailsImpl;
import cz.osu.authenticationservice.service.AppUserService;
import cz.osu.authenticationservice.service.AuthenticationServiceImpl;
import cz.osu.authenticationservice.service.TokenService;
import cz.osu.authenticationservice.service.ValidationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplUnitTests {
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenServiceImpl;
    @Mock
    private AppUserService appUserServiceImpl;
    @Mock
    private ValidationService validationServiceImpl;
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @DisplayName("authUser - Should return LoginResponse")
    @Test
    void authUser() {
        UserDetailsImpl testedDetails = new UserDetailsImpl("USER_ID", "USERNAME", "EMAIL", "PASSWORD", List.of(new SimpleGrantedAuthority(ERole.ROLE_USER.name())));
        LoginResponse expected = new LoginResponse(testedDetails.getUsername(), testedDetails.getEmail(), testedDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList(), "ACCESS_TOKEN", "REFRESH_TOKEN");

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(testedDetails);

        when(tokenServiceImpl.createRefreshTokenForUser(any())).thenReturn(new RefreshToken(expected.refreshToken(), Instant.now()));
        when(tokenServiceImpl.createAccessTokenForUser(any())).thenReturn(new AccessToken(expected.accessToken()));


        LoginResponse result = authenticationService.authUser(testedDetails.getUsername(), testedDetails.getPassword());

        //region RESULT_CHECK
        assertThat(result).isEqualTo(expected);
        //endregion
    }

    @DisplayName("authUser - Should throw BadCredentialsException because parameter username is empty string")
    @Test
    void authUserWhenParameterUsernameIsEmpty() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                authenticationService.authUser("", "password"))
                .isInstanceOf(BadCredentialsException.class);
        //endregion
    }

    @DisplayName("authUser - Should throw BadCredentialsException because parameter password is empty string")
    @Test
    void authUserWhenParameterPasswordIsEmpty() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                authenticationService.authUser("username", ""))
                .isInstanceOf(BadCredentialsException.class);
        //endregion
    }

    @DisplayName("registerUser - Should throw IllegalArgumentException because parameter username is empty string")
    @Test
    void registerUserWhenParameterUsernameIsEmpty() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                authenticationService.registerUser("", "email", "password"))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("registerUser - Should throw IllegalArgumentException because parameter email is empty string")
    @Test
    void registerUserWhenParameterEmailIsEmpty() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                authenticationService.registerUser("username", "", "password"))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("registerUser - Should throw IllegalArgumentException because password email is empty string")
    @Test
    void registerUserWhenParameterParameterIsEmpty() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                authenticationService.registerUser("username", "email", ""))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("logout - Should remove all user's tokens")
    @Test
    void logout() {
        //region SET_UP
        ArgumentCaptor<AppUser> argumentCaptor = ArgumentCaptor.forClass(AppUser.class);

        AppUser testedUser = new AppUser("USERNAME", "EMAIL", "PASSWORD");
        testedUser.setRefreshToken(new RefreshToken("REFRESH_TOKEN", Instant.now()));
        testedUser.setAccessToken(new AccessToken("USER_TOKEN"));

        when(appUserServiceImpl.getAuthenticatedUser()).thenReturn(testedUser);
        //endregion

        //region TESTING
        this.authenticationService.logout();
        //endregion

        //region RESULT_CHECK
        verify(appUserRepository).save(argumentCaptor.capture());
        assertThat(testedUser.getAccessToken().getTokenValue()).isNull();
        assertThat(testedUser.getRefreshToken().getUuidTokenValue()).isNull();
        //endregion
    }
}
