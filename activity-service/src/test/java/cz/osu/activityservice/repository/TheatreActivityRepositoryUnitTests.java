package cz.osu.activityservice.repository;

import cz.osu.activityservice.model.database.TheatreActivity;
import cz.osu.activityservice.model.database.embedded.Rating;
import cz.osu.activityservice.model.enums.ETheatreActivitySchemaless;
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
@TestPropertySource(properties = {"spring.mongodb.embedded.version=3.5.5", "spring.data.mongodb.uri=mongodb://localhost:27017/tests"})
class TheatreActivityRepositoryUnitTests {
    @Autowired
    private TheatreActivityRepository theatreActivityRepository;

    @AfterEach
    void cleanUpEach() {
        this.theatreActivityRepository.deleteAll();
    }

    @DisplayName("findByIdActivity - Should find a TheatreActivity by IdActivity attribute")
    @Test
    void findByIdActivity() {
        //region SET_UP
        long searched_id_activity = 1;

        TheatreActivity theatreActivity = new TheatreActivity(searched_id_activity, "TEST_ACTIVITY", "STAGE", "DIVISION", Instant.now(), false);
        this.theatreActivityRepository.save(theatreActivity);
        //endregion

        //region TESTING
        TheatreActivity result = this.theatreActivityRepository.findByIdActivity(searched_id_activity)
                .orElse(null);
        //endregion

        //region RESULT_CHECK
        assertThat(result).isNotNull();

        assertThat(result.getIdActivity())
                .isEqualTo(searched_id_activity);
        //endregion
    }

    @DisplayName("findAllByIdActivityIn - Should find all Theatre activities by their activity IDs in the list")
    @Test
    void findAllByIdActivityIn() {
        //region SET_UP
        long searched_id_activity1 = 1;
        long searched_id_activity2 = 2;

        TheatreActivity expectedTheatreActivity1 = new TheatreActivity(searched_id_activity1, "TEST_ACTIVITY", "STAGE", "DIVISION", Instant.now(), false);
        TheatreActivity expectedTheatreActivity2 = new TheatreActivity(searched_id_activity2, "TEST_ACTIVITY", "STAGE", "DIVISION", Instant.now(), false);
        TheatreActivity notExpectedTheatreActivity1 = new TheatreActivity(3, "TEST_ACTIVITY", "STAGE", "DIVISION", Instant.now(), false);

        this.theatreActivityRepository.saveAll(List.of(expectedTheatreActivity1,expectedTheatreActivity2,notExpectedTheatreActivity1));
        //endregion

        //region TESTING
        List<TheatreActivity> result = this.theatreActivityRepository.findAllByIdActivityIn(List.of(searched_id_activity1,searched_id_activity2));
        //endregion

        //region RESULT_CHECK
        assertThat(result).hasSameElementsAs(List.of(expectedTheatreActivity1,expectedTheatreActivity2));
        //endregion
    }

    @DisplayName("findAllByStartDateBetween - Should find all TheatreActivities with their startDate between two dates")
    @Test
    void findAllByStartDateBetween() {
        //region SET_UP
        TheatreActivity expectedActivity1 = new TheatreActivity(1, "TEST_ACTIVITY", "STAGE", "DIVISION", Instant.now(), false);
        TheatreActivity expectedActivity2 = new TheatreActivity(2, "TEST_ACTIVITY", "STAGE", "DIVISION", Instant.now(), false);
        TheatreActivity notExpectedActivity1 = new TheatreActivity(3, "TEST_ACTIVITY", "STAGE", "DIVISION", Instant.now().plusSeconds(48 * 60 * 60), false);
        this.theatreActivityRepository.saveAll(List.of(expectedActivity1, expectedActivity2, notExpectedActivity1));
        //endregion

        //region TESTING
        List<TheatreActivity> results = this.theatreActivityRepository.findAllByStartDateBetweenOrderByStartDateAsc(Instant.now().minusSeconds(60), Instant.now().plusSeconds(60));
        //endregion

        //region RESULT_CHECK
        assertThat(results)
                .hasSameElementsAs(List.of(expectedActivity1, expectedActivity2));
        //endregion
    }

    @DisplayName("findTop3Activities - Should find three activities with highest rating value and rating count")
    @Test
    void findTop3Activities() {
        //region SET_UP
        TheatreActivity expectedActivity1 = new TheatreActivity(1, "EXPECTED_1", "STAGE", "DIVISION", Instant.now(), false);
        expectedActivity1.setRating(new Rating(3));
        expectedActivity1.getRatedByUsers().add("TEST_USER_1");
        expectedActivity1.getRatedByUsers().add("TEST_USER_2");
        expectedActivity1.getRatedByUsers().add("TEST_USER_3");
        expectedActivity1.getRatedByUsers().add("TEST_USER_4");
        expectedActivity1.getRatedByUsers().add("TEST_USER_5");

        TheatreActivity expectedActivity2 = new TheatreActivity(2, "EXPECTED_2", "STAGE", "DIVISION", Instant.now(), false);
        expectedActivity2.setRating(new Rating(5));
        expectedActivity2.getRatedByUsers().add("TEST_USER_1");
        expectedActivity2.getRatedByUsers().add("TEST_USER_2");

        TheatreActivity expectedActivity3 = new TheatreActivity(3, "EXPECTED_3", "STAGE", "DIVISION", Instant.now(), false);
        expectedActivity3.setRating(new Rating(4));
        expectedActivity3.getRatedByUsers().add("TEST_USER_1");
        expectedActivity3.getRatedByUsers().add("TEST_USER_2");

        TheatreActivity notExpectedActivity1 = new TheatreActivity(4, "NOT_EXPECTED_4", "STAGE", "DIVISION", Instant.now().plusSeconds(48 * 60 * 60), false);
        notExpectedActivity1.setRating(new Rating(5));
        notExpectedActivity1.getRatedByUsers().add("TEST_USER_1");
        this.theatreActivityRepository.saveAll(List.of(expectedActivity1, expectedActivity2, expectedActivity3, notExpectedActivity1));
        //endregion

        //region TESTING
        List<TheatreActivity> results = this.theatreActivityRepository.findTop3Activities();
        //endregion

        //region RESULT_CHECK
        assertThat(results).hasSize(3);
        assertThat(results.get(0)).isEqualTo(expectedActivity1);
        assertThat(results.get(1)).isEqualTo(expectedActivity2);
        assertThat(results.get(2)).isEqualTo(expectedActivity3);
        //endregion
    }

    @DisplayName("findDistinctDivision - Should find all unique divisions")
    @Test
    void findDistinctDivision() {
        //region SET_UP
        List<String> expectedDivisions=List.of("OPERA","MUZIKAL","EXTRA_AKCE","OPERETA");
        TheatreActivity activity1 = new TheatreActivity(1, "EXPECTED_1", "STAGE", expectedDivisions.get(0), Instant.now(), false);
        TheatreActivity activity2 = new TheatreActivity(2, "EXPECTED_2", "STAGE", expectedDivisions.get(1), Instant.now(), false);
        TheatreActivity activity3 = new TheatreActivity(3, "EXPECTED_3", "STAGE", expectedDivisions.get(2), Instant.now(), false);
        TheatreActivity activity4 = new TheatreActivity(4, "EXPECTED_4", "STAGE", expectedDivisions.get(3), Instant.now(), false);
        TheatreActivity activity5 = new TheatreActivity(5, "EXPECTED_5", "STAGE", expectedDivisions.get(3), Instant.now(), false);
        this.theatreActivityRepository.saveAll(List.of(activity1, activity2, activity3, activity4,activity5));
        //endregion

        //region TESTING
        List<String> results = this.theatreActivityRepository.findDistinctDivision();
        //endregion

        //region RESULT_CHECK
        assertThat(results).hasSize(expectedDivisions.size());
        assertThat(results).hasSameElementsAs(expectedDivisions);
        //endregion
    }

    @DisplayName("searchTheatreActivities - Should find all TheatreActivities based on given searched criteria")
    @Test
    void searchTheatreActivities() {
        //region SET_UP
        String searchedName = "SEARCHED_NAME";
        String searchedAuthor = "SEARCHED_AUTHOR";
        List<String> searchedDivisions = List.of("OPERA","OPERETA");

        TheatreActivity expectedActivity1 = new TheatreActivity(1, searchedName, "STAGE", searchedDivisions.get(0), Instant.now(), false);
        TheatreActivity expectedActivity2 = new TheatreActivity(2, searchedName, "STAGE", searchedDivisions.get(1), Instant.now(), false);
        TheatreActivity activity3 = new TheatreActivity(3, searchedName, "STAGE", searchedDivisions.get(1), Instant.now().minusSeconds(20*60), false);
        TheatreActivity activity4 = new TheatreActivity(4, searchedName, "STAGE", "DIVISION", Instant.now(), false);
        TheatreActivity activity5 = new TheatreActivity(5, "TEST_ACTIVITY", "STAGE", "DIVISION", Instant.now(), false);

        expectedActivity1.setSchemalessData(ETheatreActivitySchemaless.AUTHOR.getAttribute(),searchedAuthor);
        expectedActivity2.setSchemalessData(ETheatreActivitySchemaless.AUTHOR.getAttribute(),searchedAuthor);
        activity3.setSchemalessData(ETheatreActivitySchemaless.AUTHOR.getAttribute(),searchedAuthor);

        this.theatreActivityRepository.saveAll(List.of(expectedActivity1, expectedActivity2, activity3,activity4,activity5));
        //endregion

        //region TESTING
        Page<TheatreActivity> results = this.theatreActivityRepository.searchTheatreActivities(
                searchedName,
                searchedAuthor,
                true,
                Instant.now().minusSeconds(10*60),
                Instant.now().plusSeconds(10*60),
                searchedDivisions,
                PageRequest.of(0, 10)
                );
        //endregion

        //region RESULT_CHECK
        assertThat(results)
                .hasSameElementsAs(List.of(expectedActivity1, expectedActivity2));
        //endregion
    }

    @DisplayName("searchLikedTheatreActivities - Should find all TheatreActivities based on given searched criteria and user's id is in likedByUsers set")
    @Test
    void searchLikedTheatreActivities() {
        //region SET_UP
        String testedUserID ="USER_ID";
        String searchedName = "SEARCHED_NAME";
        String searchedAuthor = "SEARCHED_AUTHOR";
        List<String> searchedDivisions = List.of("OPERA","OPERETA");

        TheatreActivity expectedActivity1 = new TheatreActivity(1, searchedName, "STAGE", searchedDivisions.get(0), Instant.now(), false);
        TheatreActivity expectedActivity2 = new TheatreActivity(2, searchedName, "STAGE", searchedDivisions.get(1), Instant.now(), false);
        TheatreActivity activity3 = new TheatreActivity(3, searchedName, "STAGE", searchedDivisions.get(1), Instant.now().minusSeconds(20*60), false);
        TheatreActivity activity4 = new TheatreActivity(4, searchedName, "STAGE", "DIVISION", Instant.now(), false);
        TheatreActivity activity5 = new TheatreActivity(5, "TEST_ACTIVITY", "STAGE", "DIVISION", Instant.now(), false);

        expectedActivity1.setSchemalessData(ETheatreActivitySchemaless.AUTHOR.getAttribute(),searchedAuthor);
        expectedActivity2.setSchemalessData(ETheatreActivitySchemaless.AUTHOR.getAttribute(),searchedAuthor);
        activity3.setSchemalessData(ETheatreActivitySchemaless.AUTHOR.getAttribute(),searchedAuthor);

        expectedActivity1.getLikedByUsers().add(testedUserID);
        expectedActivity2.getLikedByUsers().add(testedUserID);
        activity3.getLikedByUsers().add(testedUserID);
        activity4.getLikedByUsers().add(testedUserID);
        activity5.getLikedByUsers().add(testedUserID);

        this.theatreActivityRepository.saveAll(List.of(expectedActivity1, expectedActivity2, activity3,activity4,activity5));
        //endregion

        //region TESTING
        Page<TheatreActivity> results = this.theatreActivityRepository.searchTheatreActivities(
                searchedName,
                searchedAuthor,
                true,
                Instant.now().minusSeconds(10*60),
                Instant.now().plusSeconds(10*60),
                searchedDivisions,
                PageRequest.of(0, 10)
        );
        //endregion

        //region RESULT_CHECK
        assertThat(results)
                .hasSameElementsAs(List.of(expectedActivity1, expectedActivity2));
        //endregion
    }

    @DisplayName("searchDislikedTheatreActivities - Should find all TheatreActivities based on given searched criteria and user's id is in dislikedByUsers set")
    @Test
    void searchDislikedTheatreActivities() {
        //region SET_UP
        String testedUserID ="USER_ID";
        String searchedName = "SEARCHED_NAME";
        String searchedAuthor = "SEARCHED_AUTHOR";
        List<String> searchedDivisions = List.of("OPERA","OPERETA");

        TheatreActivity expectedActivity1 = new TheatreActivity(1, searchedName, "STAGE", searchedDivisions.get(0), Instant.now(), false);
        TheatreActivity expectedActivity2 = new TheatreActivity(2, searchedName, "STAGE", searchedDivisions.get(1), Instant.now(), false);
        TheatreActivity activity3 = new TheatreActivity(3, searchedName, "STAGE", searchedDivisions.get(1), Instant.now().minusSeconds(20*60), false);
        TheatreActivity activity4 = new TheatreActivity(4, searchedName, "STAGE", "DIVISION", Instant.now(), false);
        TheatreActivity activity5 = new TheatreActivity(5, "TEST_ACTIVITY", "STAGE", "DIVISION", Instant.now(), false);

        expectedActivity1.setSchemalessData(ETheatreActivitySchemaless.AUTHOR.getAttribute(),searchedAuthor);
        expectedActivity2.setSchemalessData(ETheatreActivitySchemaless.AUTHOR.getAttribute(),searchedAuthor);
        activity3.setSchemalessData(ETheatreActivitySchemaless.AUTHOR.getAttribute(),searchedAuthor);

        expectedActivity1.getDislikedByUsers().add(testedUserID);
        expectedActivity2.getDislikedByUsers().add(testedUserID);
        activity3.getDislikedByUsers().add(testedUserID);
        activity4.getDislikedByUsers().add(testedUserID);
        activity5.getDislikedByUsers().add(testedUserID);

        this.theatreActivityRepository.saveAll(List.of(expectedActivity1, expectedActivity2, activity3,activity4,activity5));
        //endregion

        //region TESTING
        Page<TheatreActivity> results = this.theatreActivityRepository.searchTheatreActivities(
                searchedName,
                searchedAuthor,
                true,
                Instant.now().minusSeconds(10*60),
                Instant.now().plusSeconds(10*60),
                searchedDivisions,
                PageRequest.of(0, 10)
        );
        //endregion

        //region RESULT_CHECK
        assertThat(results)
                .hasSameElementsAs(List.of(expectedActivity1, expectedActivity2));
        //endregion
    }

    @DisplayName("searchRatedTheatreActivities - Should find all TheatreActivities based on given searched criteria and user's id is in ratedByUsers set")
    @Test
    void searchRatedTheatreActivities() {
        //region SET_UP
        String testedUserID ="USER_ID";
        String searchedName = "SEARCHED_NAME";
        String searchedAuthor = "SEARCHED_AUTHOR";
        List<String> searchedDivisions = List.of("OPERA","OPERETA");

        TheatreActivity expectedActivity1 = new TheatreActivity(1, searchedName, "STAGE", searchedDivisions.get(0), Instant.now(), false);
        TheatreActivity expectedActivity2 = new TheatreActivity(2, searchedName, "STAGE", searchedDivisions.get(1), Instant.now(), false);
        TheatreActivity activity3 = new TheatreActivity(3, searchedName, "STAGE", searchedDivisions.get(1), Instant.now().minusSeconds(20*60), false);
        TheatreActivity activity4 = new TheatreActivity(4, searchedName, "STAGE", "DIVISION", Instant.now(), false);
        TheatreActivity activity5 = new TheatreActivity(5, "TEST_ACTIVITY", "STAGE", "DIVISION", Instant.now(), false);

        expectedActivity1.setSchemalessData(ETheatreActivitySchemaless.AUTHOR.getAttribute(),searchedAuthor);
        expectedActivity2.setSchemalessData(ETheatreActivitySchemaless.AUTHOR.getAttribute(),searchedAuthor);
        activity3.setSchemalessData(ETheatreActivitySchemaless.AUTHOR.getAttribute(),searchedAuthor);

        expectedActivity1.getRatedByUsers().add(testedUserID);
        expectedActivity2.getRatedByUsers().add(testedUserID);
        activity3.getRatedByUsers().add(testedUserID);
        activity4.getRatedByUsers().add(testedUserID);
        activity5.getRatedByUsers().add(testedUserID);

        this.theatreActivityRepository.saveAll(List.of(expectedActivity1, expectedActivity2, activity3,activity4,activity5));
        //endregion

        //region TESTING
        Page<TheatreActivity> results = this.theatreActivityRepository.searchTheatreActivities(
                searchedName,
                searchedAuthor,
                true,
                Instant.now().minusSeconds(10*60),
                Instant.now().plusSeconds(10*60),
                searchedDivisions,
                PageRequest.of(0, 10)
        );
        //endregion

        //region RESULT_CHECK
        assertThat(results)
                .hasSameElementsAs(List.of(expectedActivity1, expectedActivity2));
        //endregion
    }
}
