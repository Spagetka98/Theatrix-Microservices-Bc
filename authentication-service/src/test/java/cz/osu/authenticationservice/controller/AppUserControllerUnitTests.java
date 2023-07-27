package cz.osu.authenticationservice.controller;

import cz.osu.authenticationservice.model.enums.EHeaders;
import cz.osu.authenticationservice.properties.TheatrixProperties;
import cz.osu.authenticationservice.security.filter.AuthTokenFilter;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AppUserController.class)
@EnableConfigurationProperties(TheatrixProperties.class)
class AppUserControllerUnitTests {
    @MockBean
    private MappingMongoConverter mappingMongoConverter;
    @MockBean
    private FilterService filterService;
    @MockBean
    private PasswordService passwordService;
    @Autowired
    private TheatrixProperties theatrixProperties;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private AuthTokenFilter authTokenFilter;

    private String testedJwtToken;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilter(authTokenFilter, "/")
                .build();

        this.testedJwtToken = Jwts.builder()
                .setSubject("TEST_USER")
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(24 * 60 * 60)))
                .signWith(SignatureAlgorithm.HS512, theatrixProperties.jwtSecretUser())
                .compact();
    }

    @DisplayName("changePassword - Should test endpoint /user/changePassword and return 200")
    @Test
    void changePassword() throws Exception {
        //region SET_UP
        String url = "/user/changePassword";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(put(url)
                        .header(EHeaders.AUTHORIZATION.getValue(), testedJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "currentPassword":"oldPassword",
                                    "newPassword":"newPassword"
                                }
                                """))
                .andExpect(status().isOk());
        //endregion
    }

    @DisplayName("changePassword - Should test endpoint /user/changePassword and return 400 because params are empty strings")
    @Test
    void changePasswordWhenParamsAreNotCorrect() throws Exception {
        //region SET_UP
        String url = "/user/changePassword";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(put(url)
                        .header(EHeaders.AUTHORIZATION.getValue(), testedJwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "currentPassword":"",
                                    "newPassword":""
                                }
                                """))
                .andExpect(status().isBadRequest());
        //endregion
    }
}
