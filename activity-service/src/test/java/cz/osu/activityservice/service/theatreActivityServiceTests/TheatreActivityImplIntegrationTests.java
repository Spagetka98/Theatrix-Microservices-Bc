package cz.osu.activityservice.service.theatreActivityServiceTests;

import cz.osu.activityservice.dataLoader.XmlLoader;
import cz.osu.activityservice.model.database.TheatreActivity;
import cz.osu.activityservice.model.enums.ETheatreActivitySchemaless;
import cz.osu.activityservice.model.pojo.UserDetails;
import cz.osu.activityservice.model.request.ActivityParamRequest;
import cz.osu.activityservice.model.response.TheatreActivityPreviewResponse;
import cz.osu.activityservice.repository.TheatreActivityRepository;
import cz.osu.activityservice.service.AppUserService;
import cz.osu.activityservice.service.MessageService;
import cz.osu.activityservice.service.TheatreActivityService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataMongoTest(includeFilters = @ComponentScan.Filter(classes = Service.class))
@TestPropertySource(properties = {"spring.mongodb.embedded.version=3.5.5","spring.data.mongodb.uri=mongodb://localhost:27017/tests", "spring.data.mongodb.auto-index-creation = true"})
@ExtendWith(MockitoExtension.class)
class TheatreActivityImplIntegrationTests {
    @Autowired
    private TheatreActivityRepository theatreActivityRepository;
    @MockBean
    private AppUserService appUserServiceImpl;
    @MockBean
    private MessageService messageServiceImpl;
    @MockBean
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private TheatreActivityService theatreActivityService;

    @AfterEach
    void cleanUp() {
        this.theatreActivityRepository.deleteAll();
    }

    @DisplayName("getActivitiesPreviewResponse - Should return previewResponses based on given searched criteria")
    @Test
    void getActivitiesPreviewResponse() {
        //region SET_UP
        String zoneId = "Europe/Prague";
        List<String> expectedDivisions = List.of("OPERA", "EXTRA_AKCE");
        String searchedAuthor = "Auth";

        TheatreActivity expectedActivity1 = new TheatreActivity(1, "Activity1", "STAGE", expectedDivisions.get(0), Instant.now(), false);
        TheatreActivity expectedActivity2 = new TheatreActivity(2, "Activity2", "STAGE", expectedDivisions.get(1), Instant.now(), false);
        TheatreActivity expectedActivity3 = new TheatreActivity(3, "Activity3", "STAGE", expectedDivisions.get(1), Instant.now(), false);
        expectedActivity1.setSchemalessData(ETheatreActivitySchemaless.AUTHOR.getAttribute(), searchedAuthor);
        expectedActivity2.setSchemalessData(ETheatreActivitySchemaless.AUTHOR.getAttribute(), searchedAuthor);
        expectedActivity3.setSchemalessData(ETheatreActivitySchemaless.AUTHOR.getAttribute(), searchedAuthor);
        List<TheatreActivity> expectedActivities = List.of(expectedActivity1, expectedActivity2, expectedActivity3);
        this.theatreActivityRepository.saveAll(expectedActivities);

        TheatreActivity notExpectedActivity4 = new TheatreActivity(4, "Activy", "STAGE", expectedDivisions.get(0), Instant.now(), false);
        TheatreActivity notExpectedActivity5 = new TheatreActivity(5, "Activity4", "STAGE", expectedDivisions.get(1), Instant.now().atZone(ZoneId.of(zoneId)).plusDays(2).toInstant(), false);
        TheatreActivity notExpectedActivity6 = new TheatreActivity(6, "Activity5", "STAGE", expectedDivisions.get(1), Instant.now().atZone(ZoneId.of(zoneId)).minusDays(2).toInstant(), false);
        this.theatreActivityRepository.saveAll(List.of(notExpectedActivity4, notExpectedActivity5, notExpectedActivity6));

        ActivityParamRequest activityParamRequest = new ActivityParamRequest();
        activityParamRequest.setPage(0);
        activityParamRequest.setSizeOfPage(10);
        activityParamRequest.setDivisions(expectedDivisions);
        activityParamRequest.setActivityName("Activi");
        activityParamRequest.setAuthorName(searchedAuthor);
        activityParamRequest.setStartDate(Instant.now().atZone(ZoneId.of(zoneId)).toLocalDate());
        activityParamRequest.setEndDate(Instant.now().atZone(ZoneId.of(zoneId)).toLocalDate().plusDays(1));
        activityParamRequest.setLiked(false);
        activityParamRequest.setDisliked(false);
        activityParamRequest.setRated(false);
        activityParamRequest.setZoneID(zoneId);

        when(appUserServiceImpl.getUser()).thenReturn(new UserDetails("USER_ID", "USERNAME"));
        //endregion

        //region TESTING
        Map<String, Object> result = this.theatreActivityService.getActivitiesPreviewResponse(activityParamRequest);
        //endregion

        //region RESULT_CHECK
        assertThat(result.size()).isEqualTo(2);
        assertThat(result)
                .extracting("previews")
                .asInstanceOf(InstanceOfAssertFactories.list(TheatreActivityPreviewResponse.class))
                .extracting(TheatreActivityPreviewResponse::getIdActivity)
                .hasSameElementsAs(expectedActivities.stream().map(TheatreActivity::getIdActivity).toList());
        //endregion
    }
}
