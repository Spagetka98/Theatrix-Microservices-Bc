package cz.osu.activityservice;

import cz.osu.activityservice.properties.MongoProperties;
import cz.osu.activityservice.properties.RabbitCredentialsProperties;
import cz.osu.activityservice.properties.RabbitWorkaroundProperties;
import cz.osu.activityservice.properties.TheatrixProperties;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableEurekaClient
@EnableMongoAuditing
@EnableScheduling
@EnableSchedulerLock(defaultLockAtMostFor = "PT2H15M")
@EnableConfigurationProperties({MongoProperties.class, RabbitWorkaroundProperties.class, RabbitCredentialsProperties.class, TheatrixProperties.class})
@EnableRetry
public class ActivityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ActivityServiceApplication.class, args);
	}

}
