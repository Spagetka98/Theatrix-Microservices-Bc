package cz.osu.activityservice.service.appUserServiceTests;

import cz.osu.activityservice.model.pojo.UserDetails;
import cz.osu.activityservice.service.AppUserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplUnitTests {
    @InjectMocks
    private AppUserServiceImpl appUserService;

    @DisplayName("getUserDetails - Should return user information from SecurityContext")
    @Test
    void getUserDetails() {
        //region SET_UP
        UserDetails expectation = new UserDetails("testUserId", "testUsername");

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);

        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(expectation);
        //endregion

        //region TESTING
        UserDetails result = appUserService.getUser();
        //endregion

        //region RESULT_CHECK
        assertThat(result)
                .isEqualTo(expectation);
        //endregion
    }
}
