package cz.osu.authenticationservice.service.authenticationService;

import cz.osu.authenticationservice.error.exception.RoleNotFoundException;
import cz.osu.authenticationservice.error.exception.UserInformationTakenException;
import cz.osu.authenticationservice.model.database.AppUser;
import cz.osu.authenticationservice.model.database.Role;
import cz.osu.authenticationservice.model.enums.ERole;
import cz.osu.authenticationservice.repository.AppUserRepository;
import cz.osu.authenticationservice.repository.RoleRepository;
import cz.osu.authenticationservice.service.AppUserService;
import cz.osu.authenticationservice.service.AuthenticationService;
import cz.osu.authenticationservice.service.TokenService;
import cz.osu.authenticationservice.service.ValidationService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataMongoTest(includeFilters = @ComponentScan.Filter(classes = Service.class))
@Import(BCryptPasswordEncoder.class)
@TestPropertySource(properties = {"spring.mongodb.embedded.version=3.5.5","spring.data.mongodb.uri=mongodb://localhost:27017/tests", "spring.data.mongodb.auto-index-creation = true"})
@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplIntegrationTests {
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private TokenService tokenServiceImpl;
    @MockBean
    private ValidationService validationServiceImpl;
    @MockBean
    private AppUserService appUserServiceImpl;
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private AuthenticationService authenticationService;

    @AfterEach
    void cleanUp() {
        this.roleRepository.deleteAll();
        this.appUserRepository.deleteAll();
    }

    @DisplayName("registerUser - Should create new User and save it to the database")
    @Test
    void registerUser() {
        //region SET_UP
        String searchedName = "TESTED_USERNAME";
        this.roleRepository.save(new Role(ERole.ROLE_USER));
        //endregion

        //region TESTING
        this.authenticationService.registerUser(searchedName, "TESTED_EMAIL", "PASSWORD");
        //endregion

        //region RESULT_CHECK
        Optional<AppUser> result = this.appUserRepository.findByUsername(searchedName);
        assertThat(result).isNotNull();
        //endregion
    }

    @DisplayName("registerUser - Should thrown UserInformationTakenException because username is already taken")
    @Test
    void registerUserWhenUsernameIsTaken() {
        //region SET_UP
        String testedName = "TESTED_USERNAME";
        this.roleRepository.save(new Role(ERole.ROLE_USER));
        this.appUserRepository.save(new AppUser(testedName, "email", "password"));
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                this.authenticationService.registerUser(testedName, "TESTED_EMAIL", "PASSWORD"))
                .isInstanceOf(UserInformationTakenException.class);
        //endregion
    }

    @DisplayName("registerUser - Should thrown UserInformationTakenException because email is already taken")
    @Test
    void registerUserWhenEmailIsTaken() {
        //region SET_UP
        String testedEmail = "TESTED_USERNAME";
        this.roleRepository.save(new Role(ERole.ROLE_USER));
        this.appUserRepository.save(new AppUser("testedName", testedEmail, "password"));
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                this.authenticationService.registerUser("name", testedEmail, "PASSWORD"))
                .isInstanceOf(UserInformationTakenException.class);
        //endregion
    }

    @DisplayName("registerUser - Should thrown RoleNotFoundException because ROLE_USER was not fount")
    @Test
    void registerUserWhenRoleWasNotFound() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                this.authenticationService.registerUser("SEARCHED_NAME", "TESTED_EMAIL", "PASSWORD")
        ).isInstanceOf(RoleNotFoundException.class);
        //endregion
    }
}
