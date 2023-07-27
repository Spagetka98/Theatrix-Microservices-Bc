package cz.osu.ratingservice;

import cz.osu.ratingservice.properties.MongoProperties;
import cz.osu.ratingservice.properties.RabbitCredentialsProperties;
import cz.osu.ratingservice.properties.RabbitWorkaroundProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableEurekaClient
@EnableMongoAuditing
@EnableConfigurationProperties({RabbitWorkaroundProperties.class, RabbitCredentialsProperties.class, MongoProperties.class})
public class RatingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RatingServiceApplication.class, args);
	}

}
