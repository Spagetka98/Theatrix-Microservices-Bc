package cz.osu.ratingservice.repository;

import cz.osu.ratingservice.model.database.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends MongoRepository<Rating,String> {
    Optional<Rating> findByIdActivity(long idActivity);

    boolean existsByIdActivity(long idActivity);

    List<Rating> findAllByIdIn(List<String> ids);
}
