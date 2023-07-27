package cz.osu.ratingservice.listener;

import cz.osu.ratingservice.error.exception.MongoDatabaseUnavailableException;
import cz.osu.ratingservice.model.message.TheatreActivityAvailabilityMessage;
import cz.osu.ratingservice.service.MessageService;
import cz.osu.ratingservice.service.MongoService;
import cz.osu.ratingservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class TheatreActivityAvailabilityListener {
    private final RatingService ratingServiceImpl;
    private final MongoService mongoServiceImpl;
    private final MessageService messageServiceImpl;

    @RabbitListener(queues = "${rabbit.queues.newTheatreActivityQueue}")
    public void listener(TheatreActivityAvailabilityMessage message) {
        log.info(String.format("Processing message in TheatreActivityAvailabilityListener.listener with idActivity: %s", message.idActivity()));

        this.ratingServiceImpl.checkRatingRecord(message.idActivity(), Instant.parse(message.availabilityAt()), message.isErasingMessage());

        log.info(String.format("Processing message in TheatreActivityAvailabilityListener.listener with idActivity: %s was successful", message.idActivity()));
    }

    @RabbitListener(queues = "${rabbit.queues.deadLetterNewTheatreActivityQueue}")
    public void deadLetterListener(TheatreActivityAvailabilityMessage message) {
        log.info(String.format("Processing message for dead letter queue with idActivity: %d in TheatreActivityAvailabilityListener.deadLetterListener", message.idActivity()));

        try {
            this.mongoServiceImpl.testDatabaseConnection();

            this.ratingServiceImpl.checkRatingRecord(message.idActivity(), Instant.parse(message.availabilityAt()), message.isErasingMessage());

            log.info(String.format("Message in dead letter queue with idActivity: %d in TheatreActivityAvailabilityListener.deadLetterListener was processed !", message.idActivity()));
        } catch (MongoDatabaseUnavailableException e) {
            this.messageServiceImpl.requeueAvailabilityMessage(message);

            log.info(String.format("Message in dead letter queue with idActivity: %d in TheatreActivityAvailabilityListener.deadLetterListener was resend to the queue !", message.idActivity()));
        } catch (Exception e) {
            this.messageServiceImpl.sendFailedActivityMessage(message);

            log.error(String.format("Message in TheatreActivityAvailabilityListener.deadLetterListener could not be processed !, message: %s", message));
        }
    }
}
