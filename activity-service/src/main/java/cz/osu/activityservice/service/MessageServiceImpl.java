package cz.osu.activityservice.service;

import cz.osu.activityservice.model.message.RatingActionMessage;
import cz.osu.activityservice.model.message.TheatreActivityAvailabilityMessage;
import cz.osu.activityservice.model.database.TheatreActivity;
import cz.osu.activityservice.properties.RabbitWorkaroundProperties;
import cz.osu.activityservice.utility.ExceptionUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{
    private final RabbitTemplate rabbitTemplate;
    private final RabbitWorkaroundProperties rabbitWorkaroundProperties;

    /**
     * @see MessageService#sendNewTheatreActivity(TheatreActivity, boolean)
     */
    @Override
    public void sendNewTheatreActivity(TheatreActivity theatreActivity,boolean isErasingMessage) {
        ExceptionUtility.checkInput(theatreActivity,"Parameter TheatreActivity in MessageServiceImpl.sendNewTheatreActivity cannot be null !");

        rabbitTemplate.convertAndSend(
                rabbitWorkaroundProperties.exchanges().newTheatreActivityExchange(),
                rabbitWorkaroundProperties.keys().newTheatreActivityKey(),
                new TheatreActivityAvailabilityMessage(
                        theatreActivity.getIdActivity(),
                        theatreActivity.getEndTimeOfActivity().toString(),
                        isErasingMessage));
    }

    /**
     * @see MessageService#requeueActionMessage(RatingActionMessage)
     */
    @Override
    public void requeueActionMessage(RatingActionMessage message) {
        ExceptionUtility.checkInput(message,"Parameter TheatreActivity in MessageServiceImpl.requeueActionMessage cannot be null !");

        this.rabbitTemplate.convertAndSend(
                rabbitWorkaroundProperties.exchanges().ratingExchange(),
                rabbitWorkaroundProperties.keys().ratingActionChangedKey(),
                message
        );
    }

    /**
     * @see MessageService#sendFailedActionMessage(RatingActionMessage)
     */
    @Override
    public void sendFailedActionMessage(RatingActionMessage message) {
        ExceptionUtility.checkInput(message,"Parameter TheatreActivity in MessageServiceImpl.sendFailedActionMessage cannot be null !");

        this.rabbitTemplate.convertAndSend(
                rabbitWorkaroundProperties.exchanges().errorExchange(),
                rabbitWorkaroundProperties.keys().errorRatingActionKey(),
                message
        );
    }
}
