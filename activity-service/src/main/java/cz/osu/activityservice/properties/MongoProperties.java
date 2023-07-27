package cz.osu.activityservice.properties;

import cz.osu.activityservice.utility.ExceptionUtility;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.data.mongodb")
public record MongoProperties(String name, String uri) {
    public MongoProperties {
        ExceptionUtility.checkInput(name, "Parameter name in MongoProperties cannot be null or empty !");
        ExceptionUtility.checkInput(uri, "Parameter uri in MongoProperties cannot be null or empty !");
    }

    @Override
    public String toString() {
        return String.format("MongoDB name: %s", name);
    }
}
