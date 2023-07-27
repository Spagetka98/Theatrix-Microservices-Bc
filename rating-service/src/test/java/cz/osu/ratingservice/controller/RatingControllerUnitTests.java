package cz.osu.ratingservice.controller;

import cz.osu.ratingservice.model.database.Rating;
import cz.osu.ratingservice.model.enums.EHeaders;
import cz.osu.ratingservice.model.response.RatingUserResponse;
import cz.osu.ratingservice.model.response.RatingsCountResponse;
import cz.osu.ratingservice.security.filter.AuthFilter;
import cz.osu.ratingservice.service.RatingService;
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

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RatingController.class)
class RatingControllerUnitTests {
    @MockBean
    private MappingMongoConverter mappingMongoConverter;
    @MockBean
    private RatingService ratingService;
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
        this.testedUsername = "TESTED_USERNAME";
    }

    @DisplayName("addRating - Should test /rating/addRating endpoint and get 200")
    @Test
    void addRating() throws Exception {
        //region SET_UP
        String url = "/rating/addRating";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(post(url)
                        .header(EHeaders.USERNAME.getValue(), testedUsername)
                        .header(EHeaders.USER_ID.getValue(), testedUserId)
                        .header(EHeaders.ROLES.getValue(), testedUserRole)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "idActivity":"1",
                                    "rating":"5",
                                    "title":"TEST",
                                    "text":"TEXT"
                                }
                                """))
                .andExpect(status().isOk());
        //endregion
    }

    @DisplayName("addRating - Should test /rating/addRating endpoint and get 400 because passed params are not valid! Rating can be between 1-5")
    @Test
    void addRatingWhenPostParamsIsNotCorrect() throws Exception {
        //region SET_UP
        String url = "/rating/addRating";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(post(url)
                        .header(EHeaders.USERNAME.getValue(), testedUsername)
                        .header(EHeaders.USER_ID.getValue(), testedUserId)
                        .header(EHeaders.ROLES.getValue(), testedUserRole)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "idActivity":"1",
                                    "rating":"9",
                                    "title":"TEST",
                                    "text":"TEXT"
                                }
                                """))
                .andExpect(status().isBadRequest());
        //endregion
    }

    @DisplayName("getActivityRatings - Should test /rating/getActivityRatings endpoint and get 200")
    @Test
    void getActivityRatings() throws Exception {
        //region SET_UP
        String url = "/rating/getActivityRatings";
        Rating testedRating = new Rating(1,Instant.now());
        testedRating.setRating(5);
        RatingsCountResponse expected = new RatingsCountResponse(testedRating);
        when(ratingService.getRatingsOfActivity(testedRating.getIdActivity())).thenReturn(expected);
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(get(url)
                        .header(EHeaders.USERNAME.getValue(), testedUsername)
                        .header(EHeaders.USER_ID.getValue(), testedUserId)
                        .header(EHeaders.ROLES.getValue(), testedUserRole)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id","1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activityRating").value(expected.getActivityRating()))
                .andDo(print());
        //endregion
    }

    @DisplayName("getUserRatings - Should test /rating/getUserRatings endpoint and get 200")
    @Test
    void getUserRatings() throws Exception {
        //region SET_UP
        String url = "/rating/getUserRatings";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(get(url)
                        .header(EHeaders.USERNAME.getValue(), testedUsername)
                        .header(EHeaders.USER_ID.getValue(), testedUserId)
                        .header(EHeaders.ROLES.getValue(), testedUserRole)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("username","Tonda")
                        .param("currentPage","1")
                        .param("sizeOfPage","1")
                       )
                .andExpect(status().isOk());
        //endregion
    }

    @DisplayName("getActivityUserRating - Should test /rating/getActivityUserRating endpoint and get 200")
    @Test
    void getActivityUserRating() throws Exception {
        //region SET_UP
        String url = "/rating/getActivityUserRating";
        RatingUserResponse expected = new RatingUserResponse(true,5,"TEST_TITLE","TEST_TEXT");
        when(ratingService.getRatingOfUser(any(Long.class))).thenReturn(expected);
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(get(url)
                        .header(EHeaders.USERNAME.getValue(), testedUsername)
                        .header(EHeaders.USER_ID.getValue(), testedUserId)
                        .header(EHeaders.ROLES.getValue(), testedUserRole)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id","1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userRated").value(expected.isUserRated()))
                .andExpect(jsonPath("$.userRating").value(expected.getUserRating()))
                .andExpect(jsonPath("$.ratingTitle").value(expected.getRatingTitle()))
                .andExpect(jsonPath("$.ratingText").value(expected.getRatingText()))
                .andDo(print());
        //endregion
    }

    @DisplayName("deleteActivityRating - Should test /rating/deleteActivityRating endpoint and get 200")
    @Test
    void deleteActivityRating() throws Exception {
        //region SET_UP
        String url = "/rating/deleteActivityRating";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(delete(url)
                        .header(EHeaders.USERNAME.getValue(), testedUsername)
                        .header(EHeaders.USER_ID.getValue(), testedUserId)
                        .header(EHeaders.ROLES.getValue(), testedUserRole)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id","1"))
                .andExpect(status().isOk())
                .andDo(print());
        //endregion
    }

    @DisplayName("changeActivityRating - Should test /rating/changeActivityRating endpoint and get 200")
    @Test
    void changeActivityRating() throws Exception {
        //region SET_UP
        String url = "/rating/changeActivityRating";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(put(url)
                        .header(EHeaders.USERNAME.getValue(), testedUsername)
                        .header(EHeaders.USER_ID.getValue(), testedUserId)
                        .header(EHeaders.ROLES.getValue(), testedUserRole)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "idActivity":"1",
                                    "rating":"5",
                                    "title":"TEST",
                                    "text":"TEXT"
                                }
                                """))
                .andExpect(status().isOk());
        //endregion
    }

    @DisplayName("changeActivityRating - Should test /rating/changeActivityRating endpoint and get 400 because passed params are not valid! Rating can be between 1-5")
    @Test
    void changeActivityRatingWhenPostParamsIsNotCorrect() throws Exception {
        //region SET_UP
        String url = "/rating/changeActivityRating";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(put(url)
                        .header(EHeaders.USERNAME.getValue(), testedUsername)
                        .header(EHeaders.USER_ID.getValue(), testedUserId)
                        .header(EHeaders.ROLES.getValue(), testedUserRole)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "idActivity":"1",
                                    "rating":"9",
                                    "title":"TEST",
                                    "text":"TEXT"
                                }
                                """))
                .andExpect(status().isBadRequest());
        //endregion
    }

    @DisplayName("getRatingPreviews - Should test /rating/getRatingPreviews endpoint and get 200")
    @Test
    void getRatingPreviews() throws Exception {
        //region SET_UP
        String url = "/rating/getRatingPreviews";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(get(url)
                        .header(EHeaders.USERNAME.getValue(), testedUsername)
                        .header(EHeaders.USER_ID.getValue(), testedUserId)
                        .header(EHeaders.ROLES.getValue(), testedUserRole)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("idActivity","1")
                        .param("page","0")
                        .param("sizeOfPage","1")
                        .param("ratings","1","2")
                        .param("startDate","12-12-2022")
                        .param("endDate","24-12-2022"))
                .andExpect(status().isOk());
        //endregion,
    }

    @DisplayName("getRatingPreviews - Should test /rating/getRatingPreviews endpoint and get 400 because passed params are not valid! startDate can be 82-12-2022")
    @Test
    void getRatingPreviewsWhenSearchParamsAreNotCorrect() throws Exception {
        //region SET_UP
        String url = "/rating/getRatingPreviews";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(get(url)
                        .header(EHeaders.USERNAME.getValue(), testedUsername)
                        .header(EHeaders.USER_ID.getValue(), testedUserId)
                        .header(EHeaders.ROLES.getValue(), testedUserRole)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("idActivity","1")
                        .param("page","0")
                        .param("sizeOfPage","1")
                        .param("ratings","1","2")
                        .param("startDate","82-12-2022")
                        .param("endDate","24-12-2022"))
                .andExpect(status().isBadRequest());
        //endregion,
    }
}
