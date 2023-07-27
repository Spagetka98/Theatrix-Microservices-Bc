package cz.osu.authenticationservice.properties;

import cz.osu.authenticationservice.utility.ExceptionUtility;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("theatrix")
public record TheatrixProperties(String jwtSecretUser,
                                 String jwtPreposition,
                                 long jwtAccessTokenExpirationMin,
                                 long jwtRefreshTokenExpirationMin,
                                 List<String> excludedUrls) {

    public TheatrixProperties{
        ExceptionUtility.checkInput(jwtSecretUser,"Parameter JwtSecretUser cannot be null or empty !");
        ExceptionUtility.checkInput(jwtPreposition,"Parameter JwtPreposition cannot be null or empty !");
    }

    @Override
    public String toString() {
        return String.format("JwtPrepostions: %s, jwtAccessTokenExpirationMin: %d, jwtRefreshTokenExpirationMin: %d",jwtPreposition(),jwtAccessTokenExpirationMin(),jwtRefreshTokenExpirationMin());
    }
}
