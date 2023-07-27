package cz.osu.authenticationservice.service.appUserService;

import cz.osu.authenticationservice.error.exception.AppUserNotFoundException;
import cz.osu.authenticationservice.model.database.AppUser;
import cz.osu.authenticationservice.repository.AppUserRepository;
import cz.osu.authenticationservice.service.AppUserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceImplUnitTests {
    @Mock
    private AppUserRepository appUserRepository;
    @InjectMocks
    private AppUserServiceImpl appUserService;

    @DisplayName("getAuthenticatedUser - Should return user information from SecurityContext")
    @Test
    void getAuthenticatedUser() {
        //region SET_UP
        AppUser expectation = new AppUser("testUsername", "testEmail", "testPwd");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getName()).thenReturn(expectation.getUsername());
        Mockito.when(appUserRepository.findByUsername(expectation.getUsername())).thenReturn(Optional.of(expectation));
        //endregion

        //region TESTING
        AppUser result = appUserService.getAuthenticatedUser();
        //endregion

        //region RESULT_CHECK
        assertThat(result)
                .isEqualTo(expectation);
        //endregion
    }

    @DisplayName("getAuthenticatedUser - Should throw AppUserNotFoundException because user with passed username was not found")
    @Test
    void getAuthenticatedUserWhenUsernameIsNotCorrect() {
        //region SET_UP
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getName()).thenReturn("test");

        Mockito.when(appUserRepository.findByUsername(any())).thenReturn(Optional.empty());
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                appUserService.getAuthenticatedUser())
                .isInstanceOf(AppUserNotFoundException.class);
        //endregion
    }

    @DisplayName("getUserByUsername - Should return user information from database based on given username")
    @Test
    void getUserByUsername() {
        //region SET_UP
        AppUser expectation = new AppUser("testUsername", "testEmail", "testPwd");
        Mockito.when(appUserRepository.findByUsername(expectation.getUsername())).thenReturn(Optional.of(expectation));
        //endregion

        //region TESTING
        AppUser result = appUserService.getUserByUsername(expectation.getUsername());
        //endregion

        //region RESULT_CHECK
        assertThat(result)
                .isEqualTo(expectation);
        //endregion
    }

    @DisplayName("getUserByUsername - Should throw AppUserNotFoundException because user with passed username was not found")
    @Test
    void getUserByUsernameWhenUsernameIsNotCorrect() {
        //region SET_UP
        Mockito.when(appUserRepository.findByUsername(any())).thenReturn(Optional.empty());
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                appUserService.getUserByUsername("testName"))
                .isInstanceOf(AppUserNotFoundException.class);
        //endregion
    }

    @DisplayName("getUserByUsername - Should throw IllegalArgumentException because passed username is empty string")
    @Test
    void getUserByUsernameWhenParameterUsernameIsNotCorrect() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                appUserService.getUserByUsername(""))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("getUserById - Should return user information from database based on given id")
    @Test
    void getUserById() {
        //region SET_UP
        AppUser expectation = new AppUser("testUsername", "testEmail", "testPwd");
        Mockito.when(appUserRepository.findById(expectation.getUsername())).thenReturn(Optional.of(expectation));
        //endregion

        //region TESTING
        AppUser result = appUserService.getUserById(expectation.getUsername());
        //endregion

        //region RESULT_CHECK
        assertThat(result)
                .isEqualTo(expectation);
        //endregion
    }

    @DisplayName("getUserById - Should throw AppUserNotFoundException because user with passed id was not found")
    @Test
    void getUserByIdWhenIdIsNotCorrect() {
        //region SET_UP
        AppUser expectation = new AppUser("testUsername", "testEmail", "testPwd");
        Mockito.when(appUserRepository.findById(any())).thenReturn(Optional.empty());
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(()->
                appUserService.getUserById(expectation.getUsername()))
                .isInstanceOf(AppUserNotFoundException.class);
        //endregion
    }

    @DisplayName("getUserById - Should throw IllegalArgumentException because passed username is empty string")
    @Test
    void getUserByIdWhenParameterIdIsNotCorrect() {
        //region RESULT_CHECK
        assertThatThrownBy(()->
                appUserService.getUserById(""))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }
}
