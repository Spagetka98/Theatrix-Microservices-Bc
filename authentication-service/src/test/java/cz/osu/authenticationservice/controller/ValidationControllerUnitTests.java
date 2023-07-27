package cz.osu.authenticationservice.controller;

import cz.osu.authenticationservice.properties.TheatrixProperties;
import cz.osu.authenticationservice.security.filter.AuthTokenFilter;
import cz.osu.authenticationservice.service.FilterService;
import cz.osu.authenticationservice.service.PasswordService;
import cz.osu.authenticationservice.service.TokenService;
import cz.osu.authenticationservice.service.ValidationService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ValidationController.class)
@EnableConfigurationProperties(TheatrixProperties.class)
class ValidationControllerUnitTests {
    @MockBean
    private MappingMongoConverter mappingMongoConverter;
    @MockBean
    private FilterService filterService;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private ValidationService validationService;
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

    @DisplayName("refreshToken - Should test endpoint /validation/token and return 200")
    @Test
    void refreshToken() throws Exception {
        //region SET_UP
        String url = "/validation/token";
        //endregion

        //region RESULT_CHECK
        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON)
                      )
                .andExpect(status().isOk());
        //endregion
    }
}
