package cz.osu.authenticationservice.utility;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilityUnitTests {
    @DisplayName("generateJwtToken - Should return JWT token")
    @Test
    void generateJwtToken() {
        //region SET_UP
        String subject = "TEST_NAME";
        String key = "TEST_KEY";
        //endregion

        //region TESTING
        String actual = JwtUtility.generateJwtToken(subject, key,Date.from(Instant.now().plusSeconds(10*60)));
        //endregion

        //region RESULT_CHECK
        assertThat(actual).isNotNull();
        //endregion
    }

    @DisplayName("generateJwtToken - Should throw IllegalArgumentException because subject is null")
    @Test
    void generateJwtTokenWhenSubjectIsNull() {
        //region SET_UP
        String key = "TEST_KEY";
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(()->
                        JwtUtility.generateJwtToken(null, key,Date.from(Instant.now().plusSeconds(10*60)))
                ).isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("generateJwtToken - Should throw IllegalArgumentException because key is null")
    @Test
    void generateJwtTokenWhenKeyIsNull() {
        //region SET_UP
        String subject = "TEST_NAME";
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(()->
                JwtUtility.generateJwtToken(subject, null,Date.from(Instant.now().plusSeconds(10*60)))
        ).isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("generateJwtToken - Should throw IllegalArgumentException because expiration is null")
    @Test
    void generateJwtTokenWhenExpirationIsNull() {
        //region SET_UP
        String subject = "TEST_NAME";
        String key = "TEST_KEY";
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(()->
                JwtUtility.generateJwtToken(subject, key,null)
        ).isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("getUsernameFromJwtToken - Should return subject from JWT")
    @Test
    void getUsernameFromJwtToken() {
        //region SET_UP
        String expected = "TEST_NAME";
        String key = "TEST_KEY";
        String jwtToken = Jwts.builder()
                .setSubject(expected)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(24 * 60 * 60)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        //endregion

        //region TESTING
        String actual = JwtUtility.getUsernameFromJwtToken(jwtToken, key);
        //endregion

        //region RESULT_CHECK
        assertThat(actual).isEqualTo(expected);
        //endregion
    }

    @DisplayName("getUsernameFromJwtToken - Should throw IllegalArgumentException because token is empty string")
    @Test
    void getUsernameFromJwtTokenWhenTokenIsEmptyString() {
        //region SET_UP
        String key = "TEST_KEY";
        String jwtToken = "";
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                JwtUtility.getUsernameFromJwtToken(jwtToken, key))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("getUsernameFromJwtToken - Should throw IllegalArgumentException because key is key string")
    @Test
    void getUsernameFromJwtTokenWhenKeyIsEmptyString() {
        //region SET_UP
        String key = "";
        String jwtToken = "TOKEN";
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                JwtUtility.getUsernameFromJwtToken(jwtToken, key))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("validateJwtToken - Should return true for valid token")
    @Test
    void validateJwtToken() {
        //region SET_UP
        String subject = "TEST_NAME";
        String key = "TEST_KEY";
        String jwtToken = Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(Instant.now().plusSeconds(24 * 60 * 60)))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
        //endregion

        //region TESTING
        boolean actual = JwtUtility.validateJwtToken(jwtToken, key);
        //endregion

        //region RESULT_CHECK
        assertThat(actual).isTrue();
        //endregion
    }

    @DisplayName("validateJwtToken - Should throw IllegalArgumentException because key is key string")
    @Test
    void validateJwtTokenWhenKeyIsEmptyString() {
        //region SET_UP
        String key = "";
        String token ="TOKEN";
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(()->
                JwtUtility.validateJwtToken(token, key))
                .isInstanceOf(IllegalArgumentException.class);
        //endregion
    }
}
