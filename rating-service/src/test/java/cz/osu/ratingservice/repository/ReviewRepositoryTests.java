package cz.osu.ratingservice.repository;

import cz.osu.ratingservice.model.database.Review;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataMongoTest
@TestPropertySource(properties = {"spring.mongodb.embedded.version=3.5.5","spring.data.mongodb.uri=mongodb://localhost:27017/tests"})
public class ReviewRepositoryTests {
    @Autowired
    private ReviewRepository reviewRepository;

    @AfterEach
    void cleanUpEach() {
        this.reviewRepository.deleteAll();
    }

    @DisplayName("findByUserIDAndRatingID - Should find a review by it's userID and ratingID")
    @Test
    void findByUserIDAndRatingID() {
        //region SET_UP
        String expected_rating_id = "RATING_ID";
        String expected_user_id = "USER_ID";

        Review expected = new Review(
                5,
                expected_rating_id,
                "test",
                "test",
                expected_user_id,
                "TEST_USER"
        );

        this.reviewRepository.save(expected);
        //endregion

        //region TESTING
        Review result = this.reviewRepository.findByUserIDAndRatingID(expected_user_id, expected_rating_id)
                .orElse(null);
        //endregion

        //region RESULT_CHECK
        assertThat(result).isNotNull();

        assertThat(result.getRatingID()).isEqualTo(expected_rating_id);

        assertThat(result.getUserID()).isEqualTo(expected_user_id);
        //endregion
    }

    @Test
    @DisplayName("findAllReviews - Should find all reviews that match search criteria")
    void findAllReviews() {
        //region SET_UP
        String ratingID = "ratingId";
        List<Integer> searchedRatings = List.of(2, 4);
        Instant searchedFrom = Instant.now().minusSeconds(2 * 60);
        Instant searchedTo = Instant.now().plusSeconds(5 * 60);

        Review expectedReview1 = new Review(4, ratingID, "testTitle", "testText", "userId1", "testUsername1");
        Review expectedReview2 = new Review(2, ratingID, "testTitle", "testText", "userId2", "testUsername2");

        Review review3 = new Review(3, ratingID, "testTitle", "testText", "userId3", "testUsername3");
        Review review4 = new Review(5, ratingID, "testTitle", "testText", "userId1", "testUsername4");
        review4.setCreatedAt(Instant.now().plusSeconds(10 * 60));

        List<Review> allReviews = List.of(expectedReview1, expectedReview2, review3, review4);
        List<Review> expectedReviews = List.of(expectedReview1, expectedReview2);

        this.reviewRepository.saveAll(allReviews);
        //endregion

        //region TESTING
        Page<Review> results = this.reviewRepository.findAllReviews(ratingID, searchedFrom, searchedTo, searchedRatings, PageRequest.of(0, 5));
        //endregion

        //region RESULT_CHECK
        assertThat(results.getContent()).hasSameElementsAs(expectedReviews);
        //endregion
    }

    @Test
    @DisplayName("findAllByUsernameOrderByCreatedAtAsc - Should find all reviews by username")
    void findAllByUsernameOrderByCreatedAtAsc() {
        //region SET_UP
        String searchedUsername = "testUsername1";

        Review expectedReview1 = new Review(4, "ratingId1", "testTitle", "testText", "userId1", searchedUsername);
        expectedReview1.setCreatedAt(Instant.now());
        Review expectedReview2 = new Review(2, "ratingId2", "testTitle", "testText", "userId2", searchedUsername);
        expectedReview2.setCreatedAt(Instant.now().plusSeconds(60));

        Review review3 = new Review(3, "ratingId3", "testTitle", "testText", "userId3", "testUsername2");
        Review review4 = new Review(5, "ratingId4", "testTitle", "testText", "userId1", "testUsername2");
        review4.setCreatedAt(Instant.now().plusSeconds(10 * 60));

        List<Review> allReviews = List.of(expectedReview1, expectedReview2, review3, review4);
        List<Review> expectedReviews = List.of(expectedReview1, expectedReview2);

        this.reviewRepository.saveAll(allReviews);
        //endregion

        //region TESTING
        Page<Review> results = this.reviewRepository.findAllByUsernameOrderByCreatedAtAsc(searchedUsername,  PageRequest.of(0, 2));
        //endregion

        //region RESULT_CHECK
        assertThat(results.getContent()).hasSameElementsAs(expectedReviews);
        //endregion
    }
}
