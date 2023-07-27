package cz.osu.ratingservice.service;

import cz.osu.ratingservice.model.message.RatingActionMessage;
import cz.osu.ratingservice.model.message.TheatreActivityAvailabilityMessage;
import cz.osu.ratingservice.properties.RabbitWorkaroundProperties;
import cz.osu.ratingservice.utility.ExceptionUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final RabbitWorkaroundProperties rabbitWorkaroundProperties;
    private final RabbitTemplate rabbitTemplate;

    /**
     * @see MessageService#sendRatingActionNotice(String, long, double, boolean)
     */
    @Override
    public void sendRatingActionNotice(String userID, long idActivity, double ratingValue,boolean isUpdate) {
        ExceptionUtility.checkInput(userID, "Parameter userID in MessageProducerServiceImpl.ratingActionPerformed cannot be null or empty !");

        rabbitTemplate.convertAndSend(
                rabbitWorkaroundProperties.exchanges().ratingExchange(),
                rabbitWorkaroundProperties.keys().ratingActionChangedKey(),
                new RatingActionMessage(
                        userID,
                        idActivity,
                        isUpdate,
                        ratingValue,
                        Instant.now().toString()));
    }

    /**
     * @see MessageService#requeueAvailabilityMessage(TheatreActivityAvailabilityMessage)
     */
    @Override
    public void requeueAvailabilityMessage(TheatreActivityAvailabilityMessage message) {
        ExceptionUtility.checkInput(message, "Parameter message in MessageServiceImpl.requeueAvailabilityMessage cannot be null !");

        this.rabbitTemplate.convertAndSend(
                rabbitWorkaroundProperties.exchanges().newTheatreActivityExchange(),
                rabbitWorkaroundProperties.keys().newTheatreActivityKey(),
                message
        );
    }

    /**
     * @see MessageService#sendFailedActivityMessage(TheatreActivityAvailabilityMessage)
     */
    @Override
    public void sendFailedActivityMessage(TheatreActivityAvailabilityMessage message) {
        ExceptionUtility.checkInput(message, "Parameter message in MessageServiceImpl.sendFailedActivityMessage cannot be null !");

        this.rabbitTemplate.convertAndSend(
                rabbitWorkaroundProperties.exchanges().errorExchange(),
                rabbitWorkaroundProperties.keys().errorNewTheatreActivityKey(),
                message
        );
    }
}
