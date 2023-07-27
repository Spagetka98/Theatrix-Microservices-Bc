package cz.osu.authenticationservice.repository;

import cz.osu.authenticationservice.model.enums.ERole;
import cz.osu.authenticationservice.model.database.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
