package cz.osu.activityservice.properties;

import cz.osu.activityservice.properties.rabbitInnerProperties.ExchangesProperties;
import cz.osu.activityservice.properties.rabbitInnerProperties.KeysProperties;
import cz.osu.activityservice.properties.rabbitInnerProperties.QueuesProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties("rabbit")
public record RabbitWorkaroundProperties(@NestedConfigurationProperty QueuesProperties queues,
                                         @NestedConfigurationProperty ExchangesProperties exchanges,
                                         @NestedConfigurationProperty KeysProperties keys) {
}
