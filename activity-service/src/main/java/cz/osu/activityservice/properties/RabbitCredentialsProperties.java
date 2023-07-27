package cz.osu.activityservice.properties;

import cz.osu.activityservice.utility.ExceptionUtility;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.rabbitmq")
public record RabbitCredentialsProperties(String uri) {
    public RabbitCredentialsProperties{
        ExceptionUtility.checkInput(uri,"Parameter host in RabbitCredentialsProperties cannot be null or empty !");}

    @Override
    public String toString() {
        return "RabbitCredentialsProperties contains uri for CloudAMQP";
    }
}
