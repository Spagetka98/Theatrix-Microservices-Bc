package cz.osu.authenticationservice.service.passwordService;

import cz.osu.authenticationservice.model.database.AppUser;
import cz.osu.authenticationservice.repository.AppUserRepository;
import cz.osu.authenticationservice.service.AppUserService;
import cz.osu.authenticationservice.service.PasswordServiceImpl;
import cz.osu.authenticationservice.service.ValidationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordServiceUnitTests {
    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private AppUserService appUserServiceImpl;
    @Mock
    private ValidationService validationServiceImpl;
    @Mock
    private PasswordEncoder encoder;
    @InjectMocks
    private PasswordServiceImpl passwordService;

    @DisplayName("changePassword - Should change user's password")
    @Test
    void changePassword() {
        //region SET_UP
        ArgumentCaptor<AppUser> argumentCaptor = ArgumentCaptor.forClass(AppUser.class);

        String new_password = "NEW_PASSWORD";
        AppUser testedUser = new AppUser("USERNAME", "EMAIL", "OLD_PASSWORD");

        when(appUserServiceImpl.getAuthenticatedUser()).thenReturn(testedUser);
        when(encoder.matches(any(String.class), any(String.class))).thenReturn(true);
        when(encoder.encode(any(String.class))).thenReturn(new_password);
        //endregion

        //region TESTING
        passwordService.changePassword(testedUser.getPassword(), new_password);
        //endregion

        //region RESULT_CHECK
        verify(appUserRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getPassword()).isEqualTo(new_password);
        //endregion
    }

    @DisplayName("changePassword - Should throw BadCredentialsException because password is not correct")
    @Test
    void changePasswordWhenPasswordsAreNotMatch() {
        //region SET_UP
        String new_password = "NEW_PASSWORD";
        AppUser testedUser = new AppUser("USERNAME", "EMAIL", "OLD_PASSWORD");
        when(appUserServiceImpl.getAuthenticatedUser()).thenReturn(testedUser);
        when(encoder.matches(any(String.class), any(String.class))).thenReturn(false);
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                passwordService.changePassword(testedUser.getPassword(), new_password)
        ).isInstanceOf(BadCredentialsException.class);
        //endregion
    }

    @DisplayName("changePassword - Should throw IllegalArgumentException because passed current password is empty string")
    @Test
    void changePasswordWhenCurrentPasswordIsEmpty() {
        //region SET_UP
        String new_password = "NEW_PASSWORD";
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                passwordService.changePassword("", new_password)
        ).isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("changePassword - Should throw IllegalArgumentException because passed new password is empty string")
    @Test
    void changePasswordWhenNewPasswordIsEmpty() {
        //region SET_UP
        String new_password = "NEW_PASSWORD";
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                passwordService.changePassword("", new_password)
        ).isInstanceOf(IllegalArgumentException.class);
        //endregion
    }
}
