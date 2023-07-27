package cz.osu.activityservice.service.theatreActivityServiceTests;

import cz.osu.activityservice.dataLoader.XmlLoader;
import cz.osu.activityservice.error.exception.TheatreActivityNotFoundException;
import cz.osu.activityservice.model.database.TheatreActivity;
import cz.osu.activityservice.model.database.embedded.Rating;
import cz.osu.activityservice.model.pojo.UserDetails;
import cz.osu.activityservice.model.response.TheatreActivityCurrentDayResponse;
import cz.osu.activityservice.model.response.TheatreActivityDetailsResponse;
import cz.osu.activityservice.model.response.TheatreActivityResponse;
import cz.osu.activityservice.model.response.TheatreActivityTopResponse;
import cz.osu.activityservice.repository.TheatreActivityRepository;
import cz.osu.activityservice.service.AppUserService;
import cz.osu.activityservice.service.MessageService;
import cz.osu.activityservice.service.TheatreActivityServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TheatreActivityImplUnitTests {
    @Mock
    private AppUserService appUserServiceImpl;
    @Mock
    private MessageService messageServiceImpl;
    @Mock
    private TheatreActivityRepository theatreActivityRepository;

    @InjectMocks
    private TheatreActivityServiceImpl theatreActivityService;

    @DisplayName("checkTheatreActivityAvailability - Should set theatre activity available")
    @Test
    void checkTheatreActivityAvailability() {
        //region SET_UP
        ArgumentCaptor<TheatreActivity> argumentCaptor = ArgumentCaptor.forClass(TheatreActivity.class);
        TheatreActivity expectedActivity = new TheatreActivity(1, "TEST_NAME", "TEST_STAGE", "TEST_DIVISION", Instant.now().minusSeconds(60), false);
        //endregion

        //region TESTING
        theatreActivityService.checkTheatreActivityAvailability(expectedActivity);
        //endregion

        //region RESULT_CHECK
        verify(theatreActivityRepository, times(1)).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().isAvailable()).isTrue();
        //endregion
    }

    @DisplayName("checkTheatreActivityAvailability - Should thrown IllegalArgumentException because passed parameter is null")
    @Test
    void checkTheatreActivityAvailabilityWhenParameterIsNotCorrect() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                theatreActivityService.checkTheatreActivityAvailability(null))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("getActivitiesPreviewResponse - Should thrown IllegalArgumentException because passed parameter is null")
    @Test
    void getActivitiesPreviewResponseWhenParameterIsNotCorrect() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                theatreActivityService.getActivitiesPreviewResponse(null))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("addLikedActivityToUser - Should add userId to the activity likedByUsers set")
    @Test
    void addLikedActivityToUser() {
        //region SET_UP
        ArgumentCaptor<TheatreActivity> argumentCaptor = ArgumentCaptor.forClass(TheatreActivity.class);

        long searched_id = 1;
        UserDetails testedUser = new UserDetails("USER_ID", "USERNAME");
        TheatreActivity testedActivity = new TheatreActivity(1, "TESTED_NAME", "TESTED_STAGE", "TESTED_DIVISION", Instant.now(), true);

        when(appUserServiceImpl.getUser()).thenReturn(testedUser);
        when(theatreActivityRepository.findByIdActivity(testedActivity.getIdActivity())).thenReturn(Optional.of(testedActivity));
        //endregion

        //region TESTING
        theatreActivityService.addLikedActivityToUser(searched_id);
        //endregion

        //region RESULT_CHECK
        verify(theatreActivityRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue().getLikedByUsers().contains(testedUser.userId())).isTrue();
        assertThat(argumentCaptor.getValue().getDislikedByUsers().contains(testedUser.userId())).isFalse();
        //endregion
    }

    @DisplayName("addLikedActivityToUser - Should thrown TheatreActivityNotFoundException because activity with passed IdActivity was not found")
    @Test
    void addLikedActivityToUserWhenParameterIsNotCorrect() {
        //region RESULT_CHECK
        assertThatThrownBy(() -> theatreActivityService.addLikedActivityToUser(1))
                .isInstanceOf(TheatreActivityNotFoundException.class);
        //endregion
    }

    @DisplayName("addDislikedActivityToUser - Should add userId to the activity dislikedByUsers set")
    @Test
    void addDislikedActivityToUser() {
        //region SET_UP
        ArgumentCaptor<TheatreActivity> argumentCaptor = ArgumentCaptor.forClass(TheatreActivity.class);

        long searched_id = 1;
        UserDetails testedUser = new UserDetails("USER_ID", "USERNAME");
        TheatreActivity testedActivity = new TheatreActivity(1, "TESTED_NAME", "TESTED_STAGE", "TESTED_DIVISION", Instant.now(), true);

        when(appUserServiceImpl.getUser()).thenReturn(testedUser);
        when(theatreActivityRepository.findByIdActivity(testedActivity.getIdActivity())).thenReturn(Optional.of(testedActivity));
        //endregion

        //region TESTING
        theatreActivityService.addDislikedActivityToUser(searched_id);
        //endregion

        //region RESULT_CHECK
        verify(theatreActivityRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue().getDislikedByUsers().contains(testedUser.userId())).isTrue();
        assertThat(argumentCaptor.getValue().getLikedByUsers().contains(testedUser.userId())).isFalse();
        //endregion
    }

    @DisplayName("addDislikedActivityToUser - Should thrown TheatreActivityNotFoundException because activity with passed IdActivity was not found")
    @Test
    void addDislikedActivityToUserWhenParameterIsNotCorrect() {
        //region RESULT_CHECK
        assertThatThrownBy(() -> theatreActivityService.addDislikedActivityToUser(1))
                .isInstanceOf(TheatreActivityNotFoundException.class);
        //endregion
    }

    @DisplayName("getAllDivisions - Should return all unique divisions")
    @Test
    void getAllDivisions() {
        //region SET_UP
        List<String> expected = List.of("OPERA", "EXTRA_AKCE");

        when(theatreActivityRepository.findDistinctDivision()).thenReturn(expected);
        //endregion

        //region TESTING
        List<String> result = theatreActivityService.getAllDivisions();
        //endregion

        //region RESULT_CHECK
        assertThat(result).hasSameElementsAs(expected);
        //endregion
    }

    @DisplayName("getTheatreActivity - Should return TheatreActivityResponse result")
    @Test
    void getTheatreActivity() {
        //region SET_UP
        long searched_id = 1;
        UserDetails testedUser = new UserDetails("USER_ID", "USERNAME");
        TheatreActivity testedActivity = new TheatreActivity(searched_id, "TESTED_NAME", "TESTED_STAGE", "TESTED_DIVISION", Instant.now(), true);

        when(appUserServiceImpl.getUser()).thenReturn(testedUser);
        when(theatreActivityRepository.findByIdActivity(searched_id)).thenReturn(Optional.of(testedActivity));
        //endregion

        //region TESTING
        TheatreActivityResponse result = theatreActivityService.getTheatreActivity(searched_id);
        //endregion

        //region RESULT_CHECK
        assertThat(result).isNotNull();
        //endregion
    }

    @DisplayName("getTheatreActivity - Should thrown TheatreActivityNotFoundException because activity with passed IdActivity was not found")
    @Test
    void getTheatreActivityWhenParameterIsNotCorrect() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                theatreActivityService.getTheatreActivity(1))
                .isInstanceOf(TheatreActivityNotFoundException.class);
        //endregion
    }

    @DisplayName("getTopTheatreActivities - Should return top activities as response")
    @Test
    void getTopTheatreActivities() {
        //region SET_UP
        TheatreActivity expectedActivity1 = new TheatreActivity(1, "NAME", "STAGE", "DIVISION", Instant.now(), false);
        TheatreActivity expectedActivity2 = new TheatreActivity(2, "NAME", "STAGE", "DIVISION", Instant.now(), false);
        TheatreActivity expectedActivity3 = new TheatreActivity(3, "NAME", "STAGE", "DIVISION", Instant.now(), false);


        when(theatreActivityRepository.findTop3Activities()).thenReturn(List.of(expectedActivity1, expectedActivity2, expectedActivity3));
        //endregion

        //region TESTING
        List<TheatreActivityTopResponse> theatreActivityTopResponse = theatreActivityService.getTopTheatreActivities();
        //endregion

        //region RESULT_CHECK
        assertThat(Stream.of(expectedActivity1, expectedActivity2, expectedActivity3).map(TheatreActivityTopResponse::new))
                .hasSameElementsAs(theatreActivityTopResponse);
        //endregion
    }

    @DisplayName("getTheatreActivityDetails - Should return top activities as response")
    @Test
    void getTheatreActivityDetails() {
        //region SET_UP
        TheatreActivity expectedActivity1 = new TheatreActivity(1, "NAME", "STAGE", "DIVISION", Instant.now(), false);
        TheatreActivity expectedActivity2 = new TheatreActivity(2, "NAME", "STAGE", "DIVISION", Instant.now(), false);

        when(theatreActivityRepository.findAllByIdActivityIn(any())).thenReturn(List.of(expectedActivity1, expectedActivity2));
        //endregion

        //region TESTING
        List<TheatreActivityDetailsResponse> theatreActivityDetailsResponse = theatreActivityService.getTheatreActivityDetails(List.of(1L,2L));
        //endregion

        //region RESULT_CHECK
        assertThat(Stream.of(expectedActivity1, expectedActivity2).map(TheatreActivityDetailsResponse::new))
                .hasSameElementsAs(theatreActivityDetailsResponse);
        //endregion
    }

    @DisplayName("getTheatreActivitiesForCurrentDay - Should return activities for current day as response")
    @Test
    void getTheatreActivitiesForCurrentDay() {
        //region SET_UP
        TheatreActivity expectedActivity1 = new TheatreActivity(1, "NAME", "STAGE", "DIVISION", Instant.now(), false);
        TheatreActivity expectedActivity2 = new TheatreActivity(2, "NAME", "STAGE", "DIVISION", Instant.now(), false);
        TheatreActivity expectedActivity3 = new TheatreActivity(3, "NAME", "STAGE", "DIVISION", Instant.now(), false);
        List<TheatreActivity> expectedActivities = List.of(expectedActivity1, expectedActivity2, expectedActivity3);

        when(theatreActivityRepository.findAllByStartDateBetweenOrderByStartDateAsc(any(), any())).thenReturn(expectedActivities);
        //endregion

        //region TESTING
        List<TheatreActivityCurrentDayResponse> theatreActivityTopResponse = theatreActivityService.getTheatreActivitiesForCurrentDay();
        //endregion

        //region RESULT_CHECK
        assertThat(theatreActivityTopResponse).hasSize(expectedActivities.size());
        //endregion
    }

    @DisplayName("ratingActionPerformed - Should update RatingValue and add userId to the ratedByUsers set")
    @Test
    void ratingActionPerformed() {
        //region SET_UP
        ArgumentCaptor<TheatreActivity> argumentCaptor = ArgumentCaptor.forClass(TheatreActivity.class);

        long expectedValue = 5;
        String expectedUserId = "USER_ID";

        Rating testedRating = new Rating(1);
        testedRating.setTimeOfUpdate(Instant.now());
        TheatreActivity testedActivity = new TheatreActivity(1, "NAME", "STAGE", "DIVISION", Instant.now(), false);
        testedActivity.setRating(testedRating);
        when(theatreActivityRepository.findByIdActivity(testedActivity.getIdActivity())).thenReturn(Optional.of(testedActivity));
        //endregion

        //region TESTING
        theatreActivityService.ratingActionPerformed(testedActivity.getIdActivity(), expectedUserId,false, expectedValue, Instant.now().plusSeconds(60).toString());
        //endregion

        //region RESULT_CHECK
        verify(theatreActivityRepository).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue().getRating().getRatingValue()).isEqualTo(expectedValue);
        assertThat(argumentCaptor.getValue().getRatedByUsers()).contains(expectedUserId);
        //endregion
    }

    @DisplayName("ratingActionPerformed - Should thrown TheatreActivityNotFoundException because activity with passed IdActivity was not found")
    @Test
    void ratingActionPerformedWhenIdActivityIsNotCorrect() {
        //region SET_UP
        when(theatreActivityRepository.findByIdActivity(any(Long.class))).thenReturn(Optional.empty());
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(()->
                theatreActivityService.ratingActionPerformed(1, "USER_ID", false,1, Instant.now().toString())
        )
                .isInstanceOf(TheatreActivityNotFoundException.class);
        //endregion
    }

    @DisplayName("ratingActionPerformed - Should thrown IllegalArgumentException because passed parameter UserId is empty string")
    @Test
    void ratingActionPerformedWhenUserIdIsEmpty() {
        //region RESULT_CHECK
        assertThatThrownBy(()->
                theatreActivityService.ratingActionPerformed(1, "",false, 1, Instant.now().toString())
        )
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("ratingActionPerformed - Should thrown IllegalArgumentException because passed parameter TimeOfSend is empty string")
    @Test
    void ratingActionPerformedWhenTimeOfSendIsEmpty() {
        //region RESULT_CHECK
        assertThatThrownBy(()->
                theatreActivityService.ratingActionPerformed(1, "UserId",false, 1, "")
        )
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }
}
