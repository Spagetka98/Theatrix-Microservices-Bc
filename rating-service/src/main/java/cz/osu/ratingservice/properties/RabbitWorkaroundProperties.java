package cz.osu.ratingservice.properties;

import cz.osu.ratingservice.properties.rabbitInnerProperties.ExchangesProperties;
import cz.osu.ratingservice.properties.rabbitInnerProperties.KeysProperties;
import cz.osu.ratingservice.properties.rabbitInnerProperties.QueuesProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("rabbit")
public record RabbitWorkaroundProperties(@NestedConfigurationProperty QueuesProperties queues,
                                         @NestedConfigurationProperty ExchangesProperties exchanges,
                                         @NestedConfigurationProperty KeysProperties keys) {
}
