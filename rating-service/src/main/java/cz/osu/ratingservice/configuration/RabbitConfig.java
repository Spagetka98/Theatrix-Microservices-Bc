package cz.osu.ratingservice.configuration;

import cz.osu.ratingservice.properties.RabbitCredentialsProperties;
import cz.osu.ratingservice.properties.RabbitWorkaroundProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RabbitConfig {
    private final RabbitWorkaroundProperties rabbitWorkaroundProperties;
    private final RabbitCredentialsProperties rabbitCredentialsProperties;

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUri(rabbitCredentialsProperties.uri());
        return connectionFactory;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue getRatingActionQueue() {
        return QueueBuilder.durable(rabbitWorkaroundProperties.queues().ratingActionQueue())
                .withArgument("x-dead-letter-exchange", rabbitWorkaroundProperties.exchanges().deadLetterExchange())
                .withArgument("x-dead-letter-routing-key", rabbitWorkaroundProperties.keys().deadLetterRatingActionChangedKey())
                .build();
    }

    @Bean
    public Queue getErrorNewTheatreActivityQueue() {
        return QueueBuilder.durable(rabbitWorkaroundProperties.queues().errorNewTheatreActivityQueue())
                .build();
    }

    @Bean
    public Queue getDeadLetterRatingActionQueue() {
        return new Queue(rabbitWorkaroundProperties.queues().deadLetterRatingActionQueue(), true);
    }

    @Bean
    public DirectExchange getRatingExchange() {
        return ExchangeBuilder.directExchange(rabbitWorkaroundProperties.exchanges().ratingExchange()).durable(true).build();
    }

    @Bean
    public DirectExchange getErrorExchange() {
        return ExchangeBuilder.directExchange(rabbitWorkaroundProperties.exchanges().errorExchange()).durable(true).build();
    }

    @Bean
    public DirectExchange getDeadLetterExchange() {
        return ExchangeBuilder.directExchange(rabbitWorkaroundProperties.exchanges().deadLetterExchange()).durable(true).build();
    }

    @Bean
    public Binding bindingRatingActionQueue() {
        return BindingBuilder
                .bind(getRatingActionQueue())
                .to(getRatingExchange())
                .with(rabbitWorkaroundProperties.keys().ratingActionChangedKey());
    }

    @Bean
    public Binding bindingDeadLetterRatingActionQueue() {
        return BindingBuilder
                .bind(getDeadLetterRatingActionQueue())
                .to(getDeadLetterExchange())
                .with(rabbitWorkaroundProperties.keys().deadLetterRatingActionChangedKey());
    }

    @Bean
    public Binding bindingErrorNewTheatreActivityQueue() {
        return BindingBuilder.bind(getErrorNewTheatreActivityQueue()).to(getErrorExchange()).with(rabbitWorkaroundProperties.keys().errorNewTheatreActivityKey());
    }
}
