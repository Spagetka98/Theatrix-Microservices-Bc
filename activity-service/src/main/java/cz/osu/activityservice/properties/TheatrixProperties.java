package cz.osu.activityservice.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("theatrix")
public record TheatrixProperties(List<String> excludedUrls) {
}
