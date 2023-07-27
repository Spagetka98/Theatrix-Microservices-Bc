package cz.osu.authenticationservice.repository;

import cz.osu.authenticationservice.model.database.AppUser;
import cz.osu.authenticationservice.model.database.embedded.RefreshToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@TestPropertySource(properties = {"spring.mongodb.embedded.version=3.5.5", "spring.data.mongodb.uri=mongodb://localhost:27017/tests"})
class AppUserRepositoryUnitTests {

    @Autowired
    private AppUserRepository appUserRepository;

    @AfterEach
    void cleanUp() {
        this.appUserRepository.deleteAll();
    }

    @DisplayName("findByUsername - Should return AppUser from database by his username")
    @Test
    void findByUsername() {
        //region SET_UP
        AppUser expected = new AppUser("USERNAME", "EMAIL", "PWD");
        this.appUserRepository.save(expected);
        //endregion

        //region TESTING
        Optional<AppUser> result = this.appUserRepository.findByUsername(expected.getUsername());
        //endregion

        //region RESULT_CHECK
        assertThat(result).isNotNull();
        //endregion
    }

    @DisplayName("findByRefreshTokenUuidTokenValue - Should return AppUser from database by his refresh token's UUID")
    @Test
    void findByRefreshTokenUuidTokenValue() {
        //region SET_UP
        AppUser expected = new AppUser("USERNAME", "EMAIL", "PWD");
        String testedUUID = UUID.randomUUID().toString();
        expected.setRefreshToken(new RefreshToken(testedUUID, Instant.now()));
        this.appUserRepository.save(expected);
        //endregion

        //region TESTING
        Optional<AppUser> result = this.appUserRepository.findByRefreshTokenUuidTokenValue(testedUUID);
        //endregion

        //region RESULT_CHECK
        assertThat(result).isNotNull();
        //endregion
    }

    @DisplayName("existsByUsername - Should return true if user with this username is in database")
    @Test
    void existsByUsername() {
        //region SET_UP
        AppUser expected = new AppUser("USERNAME", "EMAIL", "PWD");
        this.appUserRepository.save(expected);
        //endregion

        //region TESTING
        boolean result = this.appUserRepository.existsByUsername(expected.getUsername());
        //endregion

        //region RESULT_CHECK
        assertThat(result).isTrue();
        //endregion
    }

    @DisplayName("existsByEmail - Should return true if user with this email is in database")
    @Test
    void existsByEmail() {
        //region SET_UP
        AppUser expected = new AppUser("USERNAME", "EMAIL", "PWD");
        this.appUserRepository.save(expected);
        //endregion

        //region TESTING
        boolean result = this.appUserRepository.existsByEmail(expected.getEmail());
        //endregion

        //region RESULT_CHECK
        assertThat(result).isTrue();
        //endregion
    }
}
