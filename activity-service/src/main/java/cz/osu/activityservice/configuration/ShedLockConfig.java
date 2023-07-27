package cz.osu.activityservice.configuration;

import com.mongodb.client.MongoClient;
import cz.osu.activityservice.properties.MongoProperties;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.mongo.MongoLockProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ShedLockConfig{
    private final MongoProperties mongoProperties;
    @Bean
    public LockProvider lockProvider(MongoClient mongo) {
        return new MongoLockProvider(mongo.getDatabase(mongoProperties.name()));
    }
}
