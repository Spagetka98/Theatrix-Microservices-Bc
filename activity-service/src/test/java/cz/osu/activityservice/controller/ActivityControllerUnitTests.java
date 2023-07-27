package cz.osu.activityservice.controller;

import cz.osu.activityservice.model.enums.EHeaders;
import cz.osu.activityservice.properties.TheatrixProperties;
import cz.osu.activityservice.security.filter.AuthFilter;
import cz.osu.activityservice.service.TheatreActivityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ActivityController.class)
@EnableConfigurationProperties(TheatrixProperties.class)
class ActivityControllerUnitTests {
    @MockBean
    private MappingMongoConverter mappingMongoConverter;
    @MockBean
    private TheatreActivityService theatreActivityService;
    @Autowired
    private TheatrixProperties theatrixProperties;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private AuthFilter authFilter;

    private String testedUserId;
    private String testedUserRole;
    private String testedUsername;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(authFilter, "/")
                .build();
        this.testedUserId = "TEST_USER_ID";
        this.testedUserRole = "ROLE_USER";
        this.testedUsername = "TEST_USER";
    }

    @DisplayName("getTopActivities - Should test /activities/getTopActivities endpoint and get 200")
    @Test
    void getTopActivities() throws Exception {
        //region SET_UP
        String url = "/activities/getTopActivities";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //endregion
    }

    @DisplayName("getActivitiesForCurrentDay - Should test /activities/getActivitiesForCurrentDay endpoint and get 200")
    @Test
    void getActivitiesForCurrentDay() throws Exception {
        //region SET_UP
        String url = "/activities/getActivitiesForCurrentDay";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //endregion
    }

    @DisplayName("getActivitiesDetails - Should test /activities/getActivitiesDetails endpoint and get 200")
    @Test
    void getActivitiesDetails() throws Exception {
        //region SET_UP
        String url = "/activities/getActivitiesDetails";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //endregion
    }

    @DisplayName("getAllPreviewsPage - Should test /activities/allPreviews endpoint and get 200")
    @Test
    void getAllPreviewsPage() throws Exception {
        //region SET_UP
        String url = "/activities/allPreviews";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(get(url)
                        .header(EHeaders.USERNAME.getValue(), testedUsername)
                        .header(EHeaders.USER_ID.getValue(), testedUserId)
                        .header(EHeaders.ROLES.getValue(), testedUserRole)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //endregion
    }

    @DisplayName("getAllPreviewsPage - Should test /activities/allPreviews endpoint and get 400 because params (page,sizeOfPage,startDate,endDate) are not valid")
    @Test
    void getAllPreviewsPageWhenParamsAreNotCorrect() throws Exception {
        //region SET_UP
        String url = "/activities/allPreviews";
        String invalidPage = "-1";
        String invalidSizeOfPage = "-1";
        String invalidStartDate = "82.12.2022";
        String invalidEndDate = "82.12.2022";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(get(url)
                        .header(EHeaders.USERNAME.getValue(), testedUsername)
                        .header(EHeaders.USER_ID.getValue(), testedUserId)
                        .header(EHeaders.ROLES.getValue(), testedUserRole)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", invalidPage)
                        .param("sizeOfPage", invalidSizeOfPage)
                        .param("startDate", invalidStartDate)
                        .param("endDate", invalidEndDate))
                .andExpect(status().isBadRequest());
        //endregion
    }

    @DisplayName("getActivity - Should test /activities/getActivity endpoint and get 200")
    @Test
    void getActivity() throws Exception {
        //region SET_UP
        String url = "/activities/getActivity";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(get(url)
                        .header(EHeaders.USERNAME.getValue(), testedUsername)
                        .header(EHeaders.USER_ID.getValue(), testedUserId)
                        .header(EHeaders.ROLES.getValue(), testedUserRole)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "1"))
                .andExpect(status().isOk());
        //endregion
    }

    @DisplayName("getAllDivisions - Should test /activities/getDivisions endpoint and get 200")
    @Test
    void getAllDivisions() throws Exception {
        //region SET_UP
        String url = "/activities/getDivisions";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(get(url)
                        .header(EHeaders.USERNAME.getValue(), testedUsername)
                        .header(EHeaders.USER_ID.getValue(), testedUserId)
                        .header(EHeaders.ROLES.getValue(), testedUserRole)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //endregion
    }

    @DisplayName("addLikedActivity - Should test /activities/activityLiked endpoint and get 200")
    @Test
    void addLikedActivity() throws Exception {
        //region SET_UP
        String url = "/activities/activityLiked";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(post(url)
                        .header(EHeaders.USERNAME.getValue(), testedUsername)
                        .header(EHeaders.USER_ID.getValue(), testedUserId)
                        .header(EHeaders.ROLES.getValue(), testedUserRole)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "idActivity":"1"
                                }
                                """))
                .andExpect(status().isOk());
        //endregion
    }

    @DisplayName("addDislikedActivity - Should test /activities/activityDisliked endpoint and get 200")
    @Test
    void addDislikedActivity() throws Exception {
        //region SET_UP
        String url = "/activities/activityDisliked";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(post(url)
                        .header(EHeaders.USERNAME.getValue(), testedUsername)
                        .header(EHeaders.USER_ID.getValue(), testedUserId)
                        .header(EHeaders.ROLES.getValue(), testedUserRole)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "idActivity":"1"
                                }
                                """))
                .andExpect(status().isOk());
        //endregion
    }
}
