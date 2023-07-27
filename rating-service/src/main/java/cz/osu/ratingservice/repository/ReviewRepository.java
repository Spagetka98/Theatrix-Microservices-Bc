package cz.osu.ratingservice.repository;

import cz.osu.ratingservice.model.database.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<Review,String> {
    Optional<Review> findByUserIDAndRatingID(String userID,String ratingID);

    @Query(value = "{ 'ratingID':?0,'createdAt': {$gte:?1,$lte:?2},'rating': {$in : ?3 } }",
            sort = "{'createdAt':1}")
    Page<Review> findAllReviews(String ratingID,Instant from, Instant to, List<Integer> rating, Pageable pageable);

    Page<Review> findAllByUsernameOrderByCreatedAtAsc(String username, Pageable pageable);

}
