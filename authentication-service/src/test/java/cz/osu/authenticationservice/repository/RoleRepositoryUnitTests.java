package cz.osu.authenticationservice.repository;

import cz.osu.authenticationservice.model.database.AppUser;
import cz.osu.authenticationservice.model.database.Role;
import cz.osu.authenticationservice.model.enums.ERole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@TestPropertySource(properties = {"spring.mongodb.embedded.version=3.5.5", "spring.data.mongodb.uri=mongodb://localhost:27017/tests"})
class RoleRepositoryUnitTests {
    @Autowired
    private RoleRepository roleRepository;

    @AfterEach
    void cleanUp() {
        this.roleRepository.deleteAll();
    }

    @DisplayName("findByName - Should return Role from database by name of role")
    @Test
    void findByName() {
        //region SET_UP
        Role testedRole = new Role();
        testedRole.setName(ERole.ROLE_USER);

        this.roleRepository.save(testedRole);
        //endregion

        //region TESTING
        Optional<Role> result = this.roleRepository.findByName(ERole.ROLE_USER);
        //endregion

        //region RESULT_CHECK
        assertThat(result).isNotNull();
        //endregion
    }
}
