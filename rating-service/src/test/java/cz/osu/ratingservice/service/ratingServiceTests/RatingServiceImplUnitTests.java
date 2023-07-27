package cz.osu.ratingservice.service.ratingServiceTests;

import cz.osu.ratingservice.error.exception.RatingNotAvailableException;
import cz.osu.ratingservice.error.exception.RatingNotFoundException;
import cz.osu.ratingservice.error.exception.RatingOutOfBoundsException;
import cz.osu.ratingservice.error.exception.ReviewNotFoundException;
import cz.osu.ratingservice.model.database.Rating;
import cz.osu.ratingservice.model.database.Review;
import cz.osu.ratingservice.model.pojo.UserDetails;
import cz.osu.ratingservice.model.request.RatingParamRequest;
import cz.osu.ratingservice.model.response.RatingUserResponse;
import cz.osu.ratingservice.model.response.RatingsCountResponse;
import cz.osu.ratingservice.model.response.UserReviewsResponse;
import cz.osu.ratingservice.repository.RatingRepository;
import cz.osu.ratingservice.repository.ReviewRepository;
import cz.osu.ratingservice.service.AppUserService;
import cz.osu.ratingservice.service.MessageService;
import cz.osu.ratingservice.service.RatingServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceImplUnitTests {
    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private AppUserService appUserServiceImpl;
    @Mock
    private MessageService messageServiceImpl;
    @InjectMocks
    private RatingServiceImpl ratingService;

    @DisplayName("addReview - should save new user's review and update activity rating")
    @Test
    void addReview() {
        //region SET_UP
        UserDetails testedUser = new UserDetails("TEST_USER_ID", "USERNAME");
        Rating testedRating = new Rating(1, Instant.now().minusSeconds(60));
        testedRating.setId("testID");

        when(appUserServiceImpl.getUserDetails()).thenReturn(testedUser);
        when(ratingRepository.findByIdActivity(testedRating.getIdActivity())).thenReturn(Optional.of(testedRating));
        //endregion

        //region TESTING
        ratingService.addReview(testedRating.getIdActivity(), 5, "TITLE", "TEXT");
        //endregion

        //region RESULT_CHECK
        verify(reviewRepository).save(any(Review.class));
        verify(ratingRepository).save(any(Rating.class));
        //endregion
    }

    @DisplayName("addReview - Adding review should throw RatingOutOfBoundsException because passed ratingValue is 6 (Allowed ratingValue is between 1-5)")
    @Test
    void addReviewWhenPassedRatingIsNotBetween1And5() {
        //region SET_UP
        UserDetails testedUser = new UserDetails("TEST_USER_ID", "USERNAME");
        Rating testedRating = new Rating(1, Instant.now().minusSeconds(60));
        testedRating.setId("testID");

        when(appUserServiceImpl.getUserDetails()).thenReturn(testedUser);
        when(ratingRepository.findByIdActivity(testedRating.getIdActivity())).thenReturn(Optional.of(testedRating));
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() -> {
            ratingService.addReview(testedRating.getIdActivity(), 6, "TITLE", "TEXT");
        })
                .isInstanceOf(RatingOutOfBoundsException.class);
        //endregion
    }

    @DisplayName("addReview - Adding review should throw a RatingNotAvailableException because rating dateTime not passed")
    @Test
    void addReviewWhenRatingIsNotAvailable() {
        //region SET_UP
        Rating testedRating = new Rating(1, Instant.now().plusSeconds(10 * 60));

        when(ratingRepository.findByIdActivity(testedRating.getIdActivity())).thenReturn(Optional.of(testedRating));
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.addReview(testedRating.getIdActivity(), 5, "TITLE", "TEXT"))
                .isInstanceOf(RatingNotAvailableException.class);
        //endregion
    }

    @DisplayName("addReview - Adding review should throw a RatingNotFoundException because passed IdActivity parameter is incorrect")
    @Test
    void addReviewWhenPassedIdActivityIsNotCorrect() {
        //region SET_UP
        when(ratingRepository.findByIdActivity(any(Long.class))).thenReturn(Optional.empty());
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.addReview(1, 5, "TITLE", "TEXT"))
                .isInstanceOf(RatingNotFoundException.class);
        //endregion
    }

    @DisplayName("addReview - Adding review should throw a IllegalArgumentException because passed title parameter is empty")
    @Test
    void addReviewWhenPassedTitleIsNotCorrect() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.addReview(1, 5, "", "TEXT"))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("addReview - Adding review should throw a IllegalArgumentException because passed text parameter is empty")
    @Test
    void addReviewWhenPassedTextIsNotCorrect() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.addReview(1, 5, "TITLE", ""))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("updateReview - Updating review should throw a IllegalArgumentException because passed title parameter is empty")
    @Test
    void updateReviewWhenPassedTitleIsNotCorrect() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.updateReview(1, 5, "", "TEXT"))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("updateReview - Updating review should throw a IllegalArgumentException because passed text parameter is empty")
    @Test
    void updateReviewWhenPassedTextIsNotCorrect() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.updateReview(1, 5, "TITLE", ""))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("updateReview - Updating review should throw a RatingNotFoundException because passed IdActivity parameter is incorrect")
    @Test
    void updatingReviewWhenPassedIdActivityIsNotCorrect() {
        //region SET_UP
        UserDetails testedUser = new UserDetails("USER_ID", "USERNAME");

        when(appUserServiceImpl.getUserDetails()).thenReturn(testedUser);
        when(ratingRepository.findByIdActivity(any(Long.class))).thenReturn(Optional.empty());
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.updateReview(1, 5, "TITLE", "TEXT"))
                .isInstanceOf(RatingNotFoundException.class);
        //endregion
    }

    @DisplayName("updateReview - Updating review should throw a ReviewNotFoundException because review for updating not exist")
    @Test
    void updatingReviewWhenReviewForUpdatingWasNotFound() {
        //region SET_UP
        UserDetails testedUser = new UserDetails("USER_ID", "USERNAME");
        Rating testedRating = new Rating(1, Instant.now());

        when(appUserServiceImpl.getUserDetails()).thenReturn(testedUser);
        when(ratingRepository.findByIdActivity(any(Long.class))).thenReturn(Optional.of(testedRating));
        when(reviewRepository.findByUserIDAndRatingID(any(String.class), any())).thenReturn(Optional.empty());
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.updateReview(testedRating.getIdActivity(), 5, "TITLE", "TEXT"))
                .isInstanceOf(ReviewNotFoundException.class);
        //endregion
    }

    @DisplayName("updateReview - Updating review should change only text and title information")
    @Test
    void updatingReviewWithNewTitleAndText() {
        //region SET_UP
        ArgumentCaptor<Review> argumentCaptor = ArgumentCaptor.forClass(Review.class);

        UserDetails testedUser = new UserDetails("USER_ID", "USERNAME");
        Rating testedRating = new Rating(1, Instant.now());

        Review reviewToBeUpdated = new Review(1, "RATING_ID", "OLD_TITLE", "OLD_TEXT", "USER_ID", "USERNAME");
        String newTitle = "NEW_TITLE";
        String newText = "NEW_TEXT";


        when(appUserServiceImpl.getUserDetails()).thenReturn(testedUser);
        when(ratingRepository.findByIdActivity(any(Long.class))).thenReturn(Optional.of(testedRating));
        when(reviewRepository.findByUserIDAndRatingID(any(String.class), any())).thenReturn(Optional.of(reviewToBeUpdated));
        //endregion

        //region TESTING
        ratingService.updateReview(testedRating.getIdActivity(), reviewToBeUpdated.getRating(), newTitle, newText);
        //endregion

        //region RESULT_CHECK
        verify(reviewRepository, times(1)).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getTitle()).isEqualTo(newTitle);
        assertThat(argumentCaptor.getValue().getText()).isEqualTo(newText);
        //endregion
    }

    @DisplayName("changeRatingSet - Change of rating set should throw IllegalArgumentException because passed rating is null")
    @Test
    void changeRatingSetForUpdatingSetCollectionWhenRatingIsNotCorrect() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.changeRatingSet(
                        null
                        , new Review(4, "ratingID", "TITLE", "TEXT", "USER_ID", "USERNAME")
                        , "TITLE"
                        , "TEXT",
                        5))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("changeRatingSet - Change of rating set should throw IllegalArgumentException because passed review is null")
    @Test
    void changeRatingSetForUpdatingSetCollectionWhenReviewIsNotCorrect() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.changeRatingSet(
                        new Rating(1, Instant.now())
                        , null
                        , "TITLE"
                        , "TEXT",
                        5))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("changeRatingSet - Change of rating set should throw IllegalArgumentException because passed title is empty")
    @Test
    void changeRatingSetForUpdatingSetCollectionWhenTitleIsNotCorrect() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.changeRatingSet(
                        new Rating(1, Instant.now())
                        , new Review(4, "ratingID", "TITLE", "TEXT", "USER_ID", "USERNAME")
                        , ""
                        , "TEXT",
                        5))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("changeRatingSet - Change of rating set should throw IllegalArgumentException because passed title is empty")
    @Test
    void changeRatingSetForUpdatingSetCollectionWhenTextIsNotCorrect() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.changeRatingSet(
                        new Rating(1, Instant.now())
                        , new Review(4, "ratingID", "TITLE", "TEXT", "USER_ID", "USERNAME")
                        , "TITLE"
                        , "",
                        5))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("changeRatingSet - Change of rating set should update whole review including ratingValue")
    @Test
    void changeRatingSetForUpdatingSetCollectionReviewUpdate() {
        //region SET_UP
        ArgumentCaptor<Review> argumentCaptor = ArgumentCaptor.forClass(Review.class);

        Rating testedRating = new Rating(1, Instant.now());
        Review reviewToBeUpdated = new Review(1, "ratingID", "OLD_TITLE", "OLD_TEXT", "USER_ID", "USERNAME");
        String newTitle = "NEW_TITLE";
        String newText = "NEW_TEXT";
        int newValue = 5;
        //endregion

        //region TESTING
        ratingService.changeRatingSet(
                testedRating
                , reviewToBeUpdated
                , newTitle
                , newText,
                newValue);
        //endregion

        //region RESULT_CHECK
        verify(reviewRepository, times(1)).save(argumentCaptor.capture());

        assertThat(reviewToBeUpdated.getTitle()).isEqualTo(argumentCaptor.getValue().getTitle());
        assertThat(reviewToBeUpdated.getText()).isEqualTo(argumentCaptor.getValue().getText());
        assertThat(reviewToBeUpdated.getRating()).isEqualTo(argumentCaptor.getValue().getRating());
        //endregion
    }

    @DisplayName("changeRatingSet - Change of rating set should update Rating sets based on given ratingValue (Review should be removed from old set and put into new one)")
    @Test
    void changeRatingSetForUpdatingSetCollectionRatingUpdate() {
        //region SET_UP
        ArgumentCaptor<Rating> argumentCaptor = ArgumentCaptor.forClass(Rating.class);

        Rating ratingToBeUpdated = new Rating(1, Instant.now());
        Review testedReview = new Review(1, "ratingID", "OLD_TITLE", "OLD_TEXT", "USER_ID", "USERNAME");
        ratingToBeUpdated.getOneStar().add(testedReview);

        String newTitle = "NEW_TITLE";
        String newText = "NEW_TEXT";
        int newValue = 5;
        //endregion

        //region TESTING
        ratingService.changeRatingSet(
                ratingToBeUpdated
                , testedReview
                , newTitle
                , newText,
                newValue);
        //endregion

        //region RESULT_CHECK
        verify(ratingRepository, times(1)).save(argumentCaptor.capture());

        assertThat(ratingToBeUpdated.getOneStar().size()).isEqualTo(0);
        assertThat(ratingToBeUpdated.getFiveStar().size()).isEqualTo(1);
        //endregion
    }

    @DisplayName("deleteReview - Deleting review should throw RatingNotFoundException because passed idActivity is incorrect")
    @Test
    void deleteReviewWhenIdActivityIsNotCorrect() {
        //region SET_UP
        UserDetails testedUser = new UserDetails("USER_ID", "USERNAME");

        when(appUserServiceImpl.getUserDetails()).thenReturn(testedUser);
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.deleteReview(1))
                .isInstanceOf(RatingNotFoundException.class);
        //endregion
    }

    @DisplayName("deleteReview - Deleting review should throw ReviewNotFoundException because user hasn't add a review")
    @Test
    void deleteReviewWhenUserHasNotAddedReview() {
        //region SET_UP
        UserDetails testedUser = new UserDetails("USER_ID", "USERNAME");
        Rating testedRating = new Rating(1, Instant.now());

        when(appUserServiceImpl.getUserDetails()).thenReturn(testedUser);
        when(ratingRepository.findByIdActivity(any(Long.class))).thenReturn(Optional.of(testedRating));
        when(reviewRepository.findByUserIDAndRatingID(any(), any())).thenReturn(Optional.empty());
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.deleteReview(1))
                .isInstanceOf(ReviewNotFoundException.class);
        //endregion
    }

    @DisplayName("deleteReview - Deleting review")
    @Test
    void deleteReview() {
        //region SET_UP
        UserDetails testedUser = new UserDetails("USER_ID", "USERNAME");
        Rating testedRating = new Rating(1, Instant.now());
        testedRating.setId("RATING_ID");
        Review reviewToBeDeleted = new Review(5, testedRating.getId(), "TITLE", "TEXT", testedUser.userId(), testedUser.username());
        testedRating.getFiveStar().add(reviewToBeDeleted);

        when(appUserServiceImpl.getUserDetails()).thenReturn(testedUser);
        when(ratingRepository.findByIdActivity(any(Long.class))).thenReturn(Optional.of(testedRating));
        when(reviewRepository.findByUserIDAndRatingID(any(), any())).thenReturn(Optional.of(reviewToBeDeleted));
        //endregion

        //region TESTING
        ratingService.deleteReview(1);
        //endregion

        //region RESULT_CHECK
        verify(reviewRepository, times(1)).delete(reviewToBeDeleted);
        //endregion
    }

    @DisplayName("deleteReview - Deleting review should update ratingSets of rating")
    @Test
    void deleteReviewShouldUpdateRating() {
        //region SET_UP
        ArgumentCaptor<Rating> argumentCaptor = ArgumentCaptor.forClass(Rating.class);

        UserDetails testedUser = new UserDetails("USER_ID", "USERNAME");
        Rating testedRating = new Rating(1, Instant.now());
        testedRating.setId("RATING_ID");
        Review reviewToBeDeleted = new Review(5, testedRating.getId(), "TITLE", "TEXT", testedUser.userId(), testedUser.username());
        testedRating.getFiveStar().add(reviewToBeDeleted);

        when(appUserServiceImpl.getUserDetails()).thenReturn(testedUser);
        when(ratingRepository.findByIdActivity(any(Long.class))).thenReturn(Optional.of(testedRating));
        when(reviewRepository.findByUserIDAndRatingID(any(), any())).thenReturn(Optional.of(reviewToBeDeleted));
        //endregion

        //region TESTING
        ratingService.deleteReview(1);
        //endregion

        //region RESULT_CHECK
        verify(ratingRepository, times(1)).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue().getFiveStar().size()).isEqualTo(0);
        //endregion
    }

    @DisplayName("getRatingsOfActivity - Should return all rating information for activity based on given IdActivity")
    @Test
    void getRatingsOfActivity() {
        //region SET_UP
        long searchedIdActivity = 1;
        Rating foundedRating = new Rating(searchedIdActivity, Instant.now());
        RatingsCountResponse expected = new RatingsCountResponse(foundedRating);

        when(ratingRepository.findByIdActivity(searchedIdActivity)).thenReturn(Optional.of(foundedRating));
        //endregion

        //region TESTING
        RatingsCountResponse actual = ratingService.getRatingsOfActivity(searchedIdActivity);
        //endregion

        //region RESULT_CHECK
        assertThat(actual).isEqualTo(expected);
        //endregion
    }

    @DisplayName("getRatingsOfActivity - Should throw RatingNotFoundException because activity for given IdActivity is not exist")
    @Test
    void getRatingsOfActivityWhenIdActivityIsNotCorrect() {
        //region SET_UP
        long searchedIdActivity = 1;
        when(ratingRepository.findByIdActivity(searchedIdActivity)).thenReturn(Optional.empty());
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.getRatingsOfActivity(searchedIdActivity))
                .isInstanceOf(RatingNotFoundException.class);
        //endregion
    }

    @DisplayName("getRatingOfUser - Should return user's personal review that was written for activity")
    @Test
    void getRatingOfUser() {
        //region SET_UP
        long searchedIdActivity = 1;
        UserDetails testedUser = new UserDetails("USER_ID", "USERNAME");
        Rating testedRating = new Rating(searchedIdActivity, Instant.now());
        Review testedReview = new Review(5, "RATING_ID", "TITLE", "TEXT", "USER_ID", "USERNAME");
        RatingUserResponse expected = new RatingUserResponse(true, testedReview.getRating(), testedReview.getTitle(), testedReview.getText());

        when(appUserServiceImpl.getUserDetails()).thenReturn(testedUser);
        when(ratingRepository.findByIdActivity(searchedIdActivity)).thenReturn(Optional.of(testedRating));
        when(reviewRepository.findByUserIDAndRatingID(testedUser.userId(), testedRating.getId())).thenReturn(Optional.of(testedReview));
        //endregion

        //region TESTING
        RatingUserResponse actual = ratingService.getRatingOfUser(searchedIdActivity);
        //endregion

        //region RESULT_CHECK
        assertThat(actual).isEqualTo(expected);
        //endregion
    }

    @DisplayName("getRatingOfUser - Should throw RatingNotFoundException because activity for given IdActivity is not exist")
    @Test
    void getRatingOfUserWhenIdActivityIsNotCorrect() {
        //region SET_UP
        long searchedIdActivity = 1;
        UserDetails testedUser = new UserDetails("USER_ID", "USERNAME");

        when(appUserServiceImpl.getUserDetails()).thenReturn(testedUser);
        when(ratingRepository.findByIdActivity(searchedIdActivity)).thenReturn(Optional.empty());
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.getRatingOfUser(searchedIdActivity))
                .isInstanceOf(RatingNotFoundException.class);
        //endregion

    }

    @DisplayName("getReviews - Should throw IllegalArgumentException because searched criteria are null")
    @Test
    void getReviewsWhenSearchedCriteriaAreNull() {
        //region RESULT_CHECK
        assertThatThrownBy(() -> ratingService.getReviews(null))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("getReviews - Should throw RatingNotFoundException because searched criteria idActivity is not correct")
    @Test
    void getReviewsWhenSearchedCriteriaIdActivityIsNotCorrect() {
        //region SET_UP
        RatingParamRequest testedParams = new RatingParamRequest();
        testedParams.setIdActivity(0);

        when(ratingRepository.findByIdActivity(testedParams.getIdActivity())).thenReturn(Optional.empty());
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() -> ratingService.getReviews(testedParams))
                .isInstanceOf(RatingNotFoundException.class);
        //endregion
    }

    @DisplayName("createNewRating - Should save new Rating")
    @Test
    void createNewRating() {
        //region SET_UP
        when(ratingRepository.findByIdActivity(any(Long.class))).thenReturn(Optional.empty());
        //endregion

        //region TESTING
        this.ratingService.checkRatingRecord(1,Instant.now(),false);
        //endregion

        //region RESULT_CHECK
        verify(ratingRepository,times(1)).save(any(Rating.class));
        //endregion
    }

    @DisplayName("createNewRating - Should throw IllegalArgumentException because startDate parameter is null")
    @Test
    void createNewRatingWhenStartDateIsNull() {
        //region RESULT_CHECK
        assertThatThrownBy(()->
                this.ratingService.checkRatingRecord(1,null,false))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("getUserRatings - It should find all user reviews and assign a rating id to each review")
    @Test
    void getUserRatings() {
        //region SET_UP
        String searchedUsername = "TEST_USERNAME";

        Rating expectedRating1 = new Rating(2,Instant.now());
        expectedRating1.setId("11");
        Rating expectedRating2 = new Rating(3,Instant.now());
        expectedRating2.setId("22");
        List<Rating> expectedRatings = List.of(expectedRating1,expectedRating2);

        Review expectedReview1 = new Review(1, expectedRating1.getId(), "TEST_TITLE","TEST_TEXT","TEST_USER_ID",searchedUsername);
        expectedReview1.setId("1");
        expectedReview1.setCreatedAt(Instant.now());
        expectedReview1.setLastModified(Instant.now());

        Review expectedReview2 = new Review(1, expectedRating2.getId(),"TEST_TITLE","TEST_TEXT","TEST_USER_ID",searchedUsername);
        expectedReview2.setId("1");
        expectedReview2.setCreatedAt(Instant.now());
        expectedReview2.setLastModified(Instant.now());

        List<Review> expectedReviews = List.of(expectedReview1,expectedReview2);

        when(reviewRepository.findAllByUsernameOrderByCreatedAtAsc(anyString(),any())).thenReturn(new PageImpl<>(expectedReviews));
        when(ratingRepository.findAllByIdIn(any())).thenReturn(expectedRatings);
        //endregion

        //region TESTING
        Map<String, Object> result = this.ratingService.getUserRatings(searchedUsername,0,5);
        //endregion

        //region RESULT_CHECK
        assertThat((List<UserReviewsResponse>)result.get("previews")).hasSize(2);
        //endregion
    }

    @DisplayName("getUserRatings - Should throw a IllegalArgumentException because passed username parameter is empty")
    @Test
    void getUserRatingsWhenPassedUsernameIsNotCorrect() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ratingService.getUserRatings("", 5, 2))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }
}
