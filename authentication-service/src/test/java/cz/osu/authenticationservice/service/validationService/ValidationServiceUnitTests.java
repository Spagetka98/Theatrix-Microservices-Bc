package cz.osu.authenticationservice.service.validationService;


import cz.osu.authenticationservice.error.exception.IncorrectRegistrationInformation;
import cz.osu.authenticationservice.model.database.AppUser;
import cz.osu.authenticationservice.model.database.embedded.AccessToken;
import cz.osu.authenticationservice.model.enums.EStatusErrors;
import cz.osu.authenticationservice.model.response.GatewayValidationResponse;
import cz.osu.authenticationservice.service.AppUserService;
import cz.osu.authenticationservice.service.ValidationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationServiceUnitTests {
    @Mock
    private AppUserService appUserServiceImpl;
    @InjectMocks
    private ValidationServiceImpl validationService;

    @DisplayName("sendServiceJWT - Should return basic user's information for communication")
    @Test
    void sendServiceJWT() {
        //region SET_UP
        AppUser testedUser = new AppUser("USERNAME", "EMAIL", "PASSWORD");
        testedUser.setAccessToken(new AccessToken("USER_TOKEN_VALUE"));
        GatewayValidationResponse expected = new GatewayValidationResponse(
                testedUser.getUsername(),
                testedUser.getId(),
                testedUser.getRoles().stream().map((role -> role.getName().name())).collect(Collectors.toSet()));
        when(appUserServiceImpl.getAuthenticatedUser()).thenReturn(testedUser);
        //endregion

        //region TESTING
        GatewayValidationResponse result = validationService.sendServiceJWT();
        //endregion

        //region RESULT_CHECK
        assertThat(result).isEqualTo(expected);
        //endregion
    }

    @DisplayName("usernameCheck - Should check username for empty spaces, length and if it contains only letters")
    @Test
    void usernameCheck() {
        //region SET_UP
        String usernameToBeCheck = "Tonda";
        //endregion

        //region TESTING
        assertDoesNotThrow(() -> this.validationService.usernameCheck(usernameToBeCheck));
        //endregion
    }

    @DisplayName("usernameCheck - Should throw IncorrectRegistrationInformation because it contains empty chars")
    @Test
    void usernameCheckWithEmptyChars() {
        //region SET_UP
        String usernameToBeCheck = "Tonda d";
        //endregion

        //region TESTING
        assertThatThrownBy(() -> this.validationService.usernameCheck(usernameToBeCheck))
                .isInstanceOf(IncorrectRegistrationInformation.class)
                .hasMessage(EStatusErrors.NAME_EMPTY_CHARS.getValue());
        //endregion
    }

    @DisplayName("usernameCheck - Should throw IncorrectRegistrationInformation because length is less than 5")
    @Test
    void usernameCheckWrongLength() {
        //region SET_UP
        String usernameToBeCheck = "Tond";
        //endregion

        //region TESTING
        assertThatThrownBy(() -> this.validationService.usernameCheck(usernameToBeCheck))
                .isInstanceOf(IncorrectRegistrationInformation.class)
                .hasMessage(EStatusErrors.NAME_INSUFFICIENT_LENGTH.getValue());
        //endregion
    }

    @DisplayName("usernameCheck - Should throw IncorrectRegistrationInformation because username contains numbers")
    @Test
    void usernameCheckContainsNumbers() {
        //region SET_UP
        String usernameToBeCheck = "Tonda98";
        //endregion

        //region TESTING
        assertThatThrownBy(() -> this.validationService.usernameCheck(usernameToBeCheck))
                .isInstanceOf(IncorrectRegistrationInformation.class)
                .hasMessage(EStatusErrors.NAME_INVALID_CHARS.getValue());
        //endregion
    }

    @DisplayName("emailCheck - Should check email for empty spaces and if it is valid")
    @Test
    void emailCheck() {
        //region SET_UP
        String emailToBeCheck = "Tonda98@gmail.com";
        //endregion

        //region TESTING
        assertDoesNotThrow(() -> this.validationService.emailCheck(emailToBeCheck));
        //endregion
    }

    @DisplayName("emailCheck - Should throw IncorrectRegistrationInformation because it contains empty chars")
    @Test
    void emailCheckWithEmptyChars() {
        //region SET_UP
        String emailToBeCheck = "Tonda 98@gmail.com";
        //endregion

        //region TESTING
        assertThatThrownBy(() -> this.validationService.emailCheck(emailToBeCheck))
                .isInstanceOf(IncorrectRegistrationInformation.class)
                .hasMessage(EStatusErrors.EMAIL_INVALID_CHARS.getValue());
        //endregion
    }

    @DisplayName("emailCheck - Should throw IncorrectRegistrationInformation because it is missing @")
    @Test
    void emailCheckMissingChar() {
        //region SET_UP
        String emailToBeCheck = "Tonda98gmail.com";
        //endregion

        //region TESTING
        assertThatThrownBy(() -> this.validationService.emailCheck(emailToBeCheck))
                .isInstanceOf(IncorrectRegistrationInformation.class)
                .hasMessage(EStatusErrors.EMAIL_INVALID_CHARS.getValue());
        //endregion
    }

    @DisplayName("emailCheck - Should throw IncorrectRegistrationInformation because it is missing domain name")
    @Test
    void emailCheckMissingDomainName() {
        //region SET_UP
        String emailToBeCheck = "Tonda98.com";
        //endregion

        //region TESTING
        assertThatThrownBy(() -> this.validationService.emailCheck(emailToBeCheck))
                .isInstanceOf(IncorrectRegistrationInformation.class)
                .hasMessage(EStatusErrors.EMAIL_INVALID_CHARS.getValue());
        //endregion
    }

    @DisplayName("passwordCheck - Should check password for empty spaces, length and valid chars(1 lowercase, 1 uppercase, 1 number)")
    @Test
    void passwordCheck() {
        //region SET_UP
        String passwordToBeCheck = "SuperHeslo123";
        //endregion

        //region TESTING
        assertDoesNotThrow(() -> this.validationService.passwordCheck(passwordToBeCheck));
        //endregion
    }

    @DisplayName("passwordCheck - Should throw IncorrectRegistrationInformation because it contains empty chars")
    @Test
    void passwordCheckWithEmptyChars() {
        //region SET_UP
        String passwordToBeCheck = "Super Heslo123";
        //endregion

        //region TESTING
        assertThatThrownBy(() -> this.validationService.passwordCheck(passwordToBeCheck))
                .isInstanceOf(IncorrectRegistrationInformation.class)
                .hasMessage(EStatusErrors.PASSWORD_EMPTY_CHARS.getValue());
        //endregion
    }

    @DisplayName("passwordCheck - Should throw IncorrectRegistrationInformation because length is less than 6")
    @Test
    void passwordCheckWrongLength() {
        //region SET_UP
        String passwordToBeCheck = "Super";
        //endregion

        //region TESTING
        assertThatThrownBy(() -> this.validationService.passwordCheck(passwordToBeCheck))
                .isInstanceOf(IncorrectRegistrationInformation.class)
                .hasMessage(EStatusErrors.PASSWORD_INSUFFICIENT_LENGTH.getValue());
        //endregion
    }

    @DisplayName("passwordCheck - Should throw IncorrectRegistrationInformation because it is missing number")
    @Test
    void passwordCheckMissingNumber() {
        //region SET_UP
        String passwordToBeCheck = "SuperHeslo";
        //endregion

        //region TESTING
        assertThatThrownBy(() -> this.validationService.passwordCheck(passwordToBeCheck))
                .isInstanceOf(IncorrectRegistrationInformation.class)
                .hasMessage(EStatusErrors.PASSWORD_INVALID_CHARS.getValue());
        //endregion
    }

    @DisplayName("passwordCheck - Should throw IncorrectRegistrationInformation because it is missing uppercase")
    @Test
    void passwordCheckMissingUpperCase() {
        //region SET_UP
        String passwordToBeCheck = "sperheslo123";
        //endregion

        //region TESTING
        assertThatThrownBy(() -> this.validationService.passwordCheck(passwordToBeCheck))
                .isInstanceOf(IncorrectRegistrationInformation.class)
                .hasMessage(EStatusErrors.PASSWORD_INVALID_CHARS.getValue());
        //endregion
    }

    @DisplayName("passwordCheck - Should throw IncorrectRegistrationInformation because it is missing lowercase")
    @Test
    void passwordCheckMissingLowerCase() {
        //region SET_UP
        String passwordToBeCheck = "SUPERHESLO123";
        //endregion

        //region TESTING
        assertThatThrownBy(() -> this.validationService.passwordCheck(passwordToBeCheck))
                .isInstanceOf(IncorrectRegistrationInformation.class)
                .hasMessage(EStatusErrors.PASSWORD_INVALID_CHARS.getValue());
        //endregion
    }
}
