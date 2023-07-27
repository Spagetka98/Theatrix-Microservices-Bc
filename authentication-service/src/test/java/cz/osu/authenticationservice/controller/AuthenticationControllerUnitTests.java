package cz.osu.authenticationservice.controller;

import cz.osu.authenticationservice.model.enums.EHeaders;
import cz.osu.authenticationservice.properties.TheatrixProperties;
import cz.osu.authenticationservice.security.filter.AuthTokenFilter;
import cz.osu.authenticationservice.service.AuthenticationService;
import cz.osu.authenticationservice.service.FilterService;
import cz.osu.authenticationservice.service.PasswordService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@EnableConfigurationProperties(TheatrixProperties.class)
class AuthenticationControllerUnitTests {
    @MockBean
    private MappingMongoConverter mappingMongoConverter;
    @MockBean
    private FilterService filterService;
    @MockBean
    private PasswordService passwordService;
    @MockBean
    private AuthenticationService authenticationService;
    @Autowired
    private TheatrixProperties theatrixProperties;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private AuthTokenFilter authTokenFilter;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @DisplayName("authenticateUser - Should test endpoint /auth/signin and return 200")
    @Test
    void authenticateUser() throws Exception {
        //region SET_UP
        String url = "/auth/signin";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username":"name",
                                    "password":"pwd"
                                }
                                """))
                .andExpect(status().isOk());
        //endregion
    }

    @DisplayName("authenticateUser - Should test endpoint /auth/signin and return 400 because parameters are not correct")
    @Test
    void authenticateUserWhenParametersAreNotCorrect() throws Exception {
        //region SET_UP
        String url = "/auth/signin";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username":"",
                                    "password":""
                                }
                                """))
                .andExpect(status().isBadRequest());
        //endregion
    }

    @DisplayName("registerUser - Should test endpoint /auth/signup and return 200")
    @Test
    void registerUser() throws Exception {
        //region SET_UP
        String url = "/auth/signup";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username":"username",
                                    "email":"email@gmail.com",
                                    "password":"password123"
                                }
                                """))
                .andExpect(status().isOk());
        //endregion
    }

    @DisplayName("registerUser - Should test endpoint /auth/signup and return 400 because parameters are not correct")
    @Test
    void registerUserWhenParametersAreNotCorrect() throws Exception {
        //region SET_UP
        String url = "/auth/signup";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username":"",
                                    "email":"",
                                    "password":""
                                }
                                """))
                .andExpect(status().isBadRequest());
        //endregion
    }

    @DisplayName("logout - Should test endpoint /auth/signout and return 200")
    @Test
    void logout() throws Exception {
        //region SET_UP
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(authTokenFilter, "/")
                .build();

        String testedJwtToken = Jwts.builder()
                .setSubject("TEST_USER")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(24 * 60 * 60)))
                .signWith(SignatureAlgorithm.HS512, theatrixProperties.jwtSecretUser())
                .compact();

        String url = "/auth/signout";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(get(url)
                        .header(EHeaders.AUTHORIZATION.getValue(), testedJwtToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //endregion
    }
}
