package cz.osu.activityservice.service;

import cz.osu.activityservice.model.database.TheatreActivity;
import cz.osu.activityservice.model.message.RatingActionMessage;

public interface MessageService {
    /**
     * Send message to the rabbitMQ about new Theater performance that was read from XML.
     *
     * @param theatreActivity the information about performance (idActivity, time of availability)
     * @param isErasingMessage if true then the Rating service will delete the corresponding record
     * @throws IllegalArgumentException if passed parameter is null
     */
    void sendNewTheatreActivity(TheatreActivity theatreActivity,boolean isErasingMessage);

    /**
     * Send message to the rabbitMQ about Rating Action (e.g. user added new rating) back to the original queue.
     *
     * @param message the message information about some user action.
     * @throws IllegalArgumentException if passed parameter is null
     */
    void requeueActionMessage(RatingActionMessage message);

    /**
     * Sends a message to the error queue (RatingActionErrorQueue) for analysis
     *
     * @param message the message information about some user action.
     * @throws IllegalArgumentException if passed parameter is null
     */
    void sendFailedActionMessage(RatingActionMessage message);
}
