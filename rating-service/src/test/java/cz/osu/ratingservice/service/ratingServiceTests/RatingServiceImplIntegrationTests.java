package cz.osu.ratingservice.service.ratingServiceTests;

import cz.osu.ratingservice.error.exception.AlreadyRatedException;
import cz.osu.ratingservice.model.database.Rating;
import cz.osu.ratingservice.model.database.Review;
import cz.osu.ratingservice.model.pojo.UserDetails;
import cz.osu.ratingservice.model.request.RatingParamRequest;
import cz.osu.ratingservice.model.response.ReviewsResponse;
import cz.osu.ratingservice.repository.RatingRepository;
import cz.osu.ratingservice.repository.ReviewRepository;
import cz.osu.ratingservice.service.AppUserService;
import cz.osu.ratingservice.service.MessageService;
import cz.osu.ratingservice.service.RatingService;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DataMongoTest(includeFilters = @ComponentScan.Filter(classes = Service.class))
@TestPropertySource(properties = {"spring.mongodb.embedded.version=3.5.5","spring.data.mongodb.uri=mongodb://localhost:27017/tests", "spring.data.mongodb.auto-index-creation = true"})
@ExtendWith(MockitoExtension.class)
class RatingServiceImplIntegrationTests {
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @MockBean
    private AppUserService appUserServiceImpl;
    @MockBean
    private MessageService messageServiceImpl;
    @MockBean
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RatingService ratingService;

    @AfterEach
    void cleanUp() {
        this.reviewRepository.deleteAll();
        this.ratingRepository.deleteAll();
    }

    @DisplayName("addReview - Adding review should throw AlreadyRatedException if user already rated it")
    @Test
    void addReviewThrowExceptionDueToDuplication() {
        //region SET_UP
        long testedIdActivity = 1;
        UserDetails testedUser = new UserDetails("TEST_USER_ID", "USERNAME");
        when(appUserServiceImpl.getUserDetails()).thenReturn(testedUser);
        this.ratingRepository.save(new Rating(testedIdActivity, Instant.now().minusSeconds(5 * 60)));
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() -> {
            ratingService.addReview(testedIdActivity, 5, "TITLE", "TEXT");
            ratingService.addReview(testedIdActivity, 5, "TITLE", "TEXT");
        })
                .isInstanceOf(AlreadyRatedException.class);
        //endregion
    }

    @DisplayName("getReviews - Should return filtered activities based on given search criteria")
    @Test
    void getReviews() {
        //region SET_UP
        Rating testedRating = new Rating(1, Instant.now().minusSeconds(10 * 60));
        testedRating = this.ratingRepository.save(testedRating);

        Review expectedReview1 = new Review(4, testedRating.getId(), "TITLE", "TEXT", "USER_ID_1", "USERNAME_1");
        Review expectedReview2 = new Review(4, testedRating.getId(), "TITLE", "TEXT", "USER_ID_2", "USERNAME_2");
        Review expectedReview3 = new Review(4, testedRating.getId(), "TITLE", "TEXT", "USER_ID_3", "USERNAME_3");
        Review expectedReview4 = new Review(1, testedRating.getId(), "TITLE", "TEXT", "USER_ID_4", "USERNAME_4");
        Review expectedReview5 = new Review(1, testedRating.getId(), "TITLE", "TEXT", "USER_ID_5", "USERNAME_5");
        List<Review> expectedReviews = List.of(expectedReview1, expectedReview2, expectedReview3, expectedReview4, expectedReview5);

        Review notExpectedReview1 = new Review(5, testedRating.getId(), "TITLE", "TEXT", "USER_ID_6", "USERNAME_6");
        Review notExpectedReview2 = new Review(5, testedRating.getId(), "TITLE", "TEXT", "USER_ID_7", "USERNAME_7");
        Review notExpectedReview3 = new Review(3, testedRating.getId(), "TITLE", "TEXT", "USER_ID_8", "USERNAME_8");
        Review notExpectedReview4 = new Review(1, testedRating.getId(), "TITLE", "TEXT", "USER_ID_9", "USERNAME_9");
        Review notExpectedReview5 = new Review(1, testedRating.getId(), "TITLE", "TEXT", "USER_ID_10", "USERNAME_10");

        List<Review> notExpectedReviews = List.of(notExpectedReview1, notExpectedReview2, notExpectedReview3, notExpectedReview4, notExpectedReview5);
        this.reviewRepository.saveAll(Stream.concat(expectedReviews.stream(), notExpectedReviews.stream()).toList());

        int twoDaysSeconds = 48 * 60 * 60;

        notExpectedReview4.setCreatedAt(Instant.now().minusSeconds(twoDaysSeconds));
        this.reviewRepository.save(notExpectedReview4);

        notExpectedReview5.setCreatedAt(Instant.now().plusSeconds(twoDaysSeconds));
        this.reviewRepository.save(notExpectedReview5);

        String zoneId = "Europe/Prague";
        RatingParamRequest searchedCriteria = new RatingParamRequest();
        searchedCriteria.setIdActivity(1);
        searchedCriteria.setPage(0);
        searchedCriteria.setSizeOfPage(10);
        searchedCriteria.setRatings(List.of(1, 4));
        searchedCriteria.setStartDate(Instant.now().atZone(ZoneId.of(zoneId)).toLocalDate());
        searchedCriteria.setEndDate(Instant.now().atZone(ZoneId.of(zoneId)).toLocalDate().plusDays(1));
        searchedCriteria.setZoneID(zoneId);
        //endregion

        //region TESTING
        Map<String, Object> result = ratingService.getReviews(searchedCriteria);
        //endregion

        //region RESULT_CHECK
        assertThat(result.size()).isEqualTo(2);
        assertThat(result)
                .extracting("previews")
                .asInstanceOf(InstanceOfAssertFactories.list(ReviewsResponse.class))
                .extracting(ReviewsResponse::getId)
                .hasSameElementsAs(expectedReviews.stream().map(Review::getId).toList());
        //endregion

    }

}
