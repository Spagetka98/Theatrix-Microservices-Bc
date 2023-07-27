package cz.osu.activityservice.listener;

import cz.osu.activityservice.error.exception.MongoDatabaseUnavailableException;
import cz.osu.activityservice.model.message.RatingActionMessage;
import cz.osu.activityservice.service.MessageService;
import cz.osu.activityservice.service.MongoService;
import cz.osu.activityservice.service.TheatreActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RatingActionListener {
    private final TheatreActivityService theatreActivityServiceImpl;
    private final MongoService mongoServiceImpl;
    private final MessageService messageServiceImpl;

    @RabbitListener(queues = "${rabbit.queues.ratingActionQueue}")
    public void listener(RatingActionMessage message) {
        log.info(String.format("Processing message with userID: %s and idActivity: %d in RatingActionListener.listener", message.userID(), message.idActivity()));

        this.theatreActivityServiceImpl.ratingActionPerformed(message.idActivity(), message.userID(), message.isUpdate(), message.ratingValue(), message.timeOfSend());

        log.info(String.format("Message with userID: %s and idActivity: %d was processed in RatingActionListener.listener !", message.userID(), message.idActivity()));
    }

    @RabbitListener(queues = "${rabbit.queues.deadLetterRatingActionQueue}")
    public void deadLetterListener(RatingActionMessage message) {
        log.info(String.format("Processing message for dead letter queue with idActivity: %d in RatingActionListener.deadLetterListener", message.idActivity()));

        try {
            this.mongoServiceImpl.testDatabaseConnection();

            this.theatreActivityServiceImpl.ratingActionPerformed(message.idActivity(), message.userID(),message.isUpdate(), message.ratingValue(), message.timeOfSend());

            log.info(String.format("Message in dead letter queue with idActivity: %d in RatingActionListener.deadLetterListener was processed !", message.idActivity()));
        } catch (MongoDatabaseUnavailableException e) {
            this.messageServiceImpl.requeueActionMessage(message);

            log.info(String.format("Message in dead letter queue with idActivity: %d in RatingActionListener.deadLetterListener was resend to the queue !", message.idActivity()));
        } catch (Exception e) {
            this.messageServiceImpl.sendFailedActionMessage(message);

            log.error(String.format("Message in RatingActionListener.deadLetterListener could not be processed !, message: %s", message));
        }
    }
}
