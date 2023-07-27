package cz.osu.activityservice.configuration;

import cz.osu.activityservice.properties.RabbitCredentialsProperties;
import cz.osu.activityservice.properties.RabbitWorkaroundProperties;
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
    public Queue getNewTheatreActivityQueue() {
        return QueueBuilder.durable(rabbitWorkaroundProperties.queues().newTheatreActivityQueue())
                .withArgument("x-dead-letter-exchange", rabbitWorkaroundProperties.exchanges().deadLetterExchange())
                .withArgument("x-dead-letter-routing-key",rabbitWorkaroundProperties.keys().deadLetterNewTheatreActivityKey())
                .build();

    }

    @Bean
    public Queue getErrorRatingActionQueue() {
        return QueueBuilder.durable(rabbitWorkaroundProperties.queues().errorRatingActionQueue())
                .build();
    }

    @Bean
    public Queue getDeadLetterNewTheatreActivityQueue() {
        return QueueBuilder.durable(rabbitWorkaroundProperties.queues().deadLetterNewTheatreActivityQueue()).build();
    }

    @Bean
    public DirectExchange getNewTheatreActivityExchange() {
        return ExchangeBuilder.directExchange(rabbitWorkaroundProperties.exchanges().newTheatreActivityExchange()).durable(true).build();
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
    public Binding bindingNewTheatreActivityQueue() {
        return BindingBuilder
                .bind(getNewTheatreActivityQueue())
                .to(getNewTheatreActivityExchange())
                .with(rabbitWorkaroundProperties.keys().newTheatreActivityKey());
    }

    @Bean
    public Binding bindingDeadLetterNewTheatreActivityQueue() {
        return BindingBuilder.bind(getDeadLetterNewTheatreActivityQueue()).to(getDeadLetterExchange()).with(rabbitWorkaroundProperties.keys().deadLetterNewTheatreActivityKey());
    }

    @Bean
    public Binding bindingErrorRatingActionQueue() {
        return BindingBuilder.bind(getErrorRatingActionQueue()).to(getErrorExchange()).with(rabbitWorkaroundProperties.keys().errorRatingActionKey());
    }
}
