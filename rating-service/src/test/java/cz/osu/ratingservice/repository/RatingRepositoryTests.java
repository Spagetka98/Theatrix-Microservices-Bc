package cz.osu.ratingservice.repository;

import cz.osu.ratingservice.model.database.Rating;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.List;

@DataMongoTest
@TestPropertySource(properties = {"spring.mongodb.embedded.version=3.5.5", "spring.data.mongodb.uri=mongodb://localhost:27017/tests"})
class RatingRepositoryTests {
    @Autowired
    private RatingRepository ratingRepository;
    private static final long EXPECTED_ID_ACTIVITY = 1;

    @BeforeEach
    void setUp() {
        Rating expected = new Rating(
                EXPECTED_ID_ACTIVITY,
                Instant.now());

        this.ratingRepository.save(expected);
    }

    @AfterEach
    void cleanUpEach() {
        this.ratingRepository.deleteAll();
    }

    @DisplayName("findByIdActivity - Should find a rating by IdActivity attribute")
    @Test
    void findByIdActivity() {
        //region TESTING
        Rating result = this.ratingRepository.findByIdActivity(EXPECTED_ID_ACTIVITY)
                .orElse(null);
        //endregion

        //region RESULT_CHECK
        assertThat(result).isNotNull();

        assertThat(result.getIdActivity())
                .isEqualTo(EXPECTED_ID_ACTIVITY);
        //endregion
    }

    @Test
    @DisplayName("existsByIdActivity - Should check if a rating exists by IdActivity attribute")
    void existsByIdActivity() {
        //region TESTING
        boolean result = this.ratingRepository.existsByIdActivity(EXPECTED_ID_ACTIVITY);
        //endregion

        //region RESULT_CHECK
        assertThat(result).isTrue();
        //endregion
    }

    @DisplayName("findAllByIdIn - Should find all ratings by theirs id attribute")
    @Test
    void findAllByIdIn() {
        //region SET_UP
        Rating expected1 = new Rating(
                2000,
                Instant.now());
        expected1.setId("2");

        Rating expected2 = new Rating(
                3000,
                Instant.now());
        expected2.setId("3");

        this.ratingRepository.save(expected1);
        this.ratingRepository.save(expected2);
        //endregion

        //region TESTING
        List<Rating> result = this.ratingRepository.findAllByIdIn(List.of("2","3"));
        //endregion

        //region RESULT_CHECK
        assertThat(result).isNotEmpty();

        assertThat(result.size()).isEqualTo(2);

        assertThat(result).hasSameElementsAs(List.of(expected1,expected2));
        //endregion
    }

}
