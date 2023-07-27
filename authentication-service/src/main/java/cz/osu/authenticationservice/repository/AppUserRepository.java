package cz.osu.authenticationservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import cz.osu.authenticationservice.model.database.AppUser;

import java.util.Optional;

public interface AppUserRepository extends MongoRepository<AppUser, String> {
    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByRefreshTokenUuidTokenValue(String refreshToken);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
