package cz.osu.ratingservice.service;

import cz.osu.ratingservice.model.message.TheatreActivityAvailabilityMessage;

public interface MessageService {
    /**
     * Sends a message about the action performed by the user (e.g. user add/update/delete rating to activity).
     *
     * @param userID      the user who performed the action
     * @param idActivity  activity for which the action occurred
     * @param ratingValue activity evaluation
     * @param isUpdate    user has not added or removed a rating, no need to go through the set
     * @throws IllegalArgumentException if passed parameter userID is null or empty
     */
    void sendRatingActionNotice(String userID, long idActivity, double ratingValue,boolean isUpdate);

    /**
     * Send message to the rabbitMQ about new Theatre performance back to the original queue.
     *
     * @param message the message information about performance.
     * @throws IllegalArgumentException if passed parameter is null
     */
    void requeueAvailabilityMessage(TheatreActivityAvailabilityMessage message);

    /**
     * Sends a message to the error queue (ErrorNewTheatreActivityQueue) for analysis
     *
     * @param message the message information about performance.
     * @throws IllegalArgumentException if passed parameter is null
     */
    void sendFailedActivityMessage(TheatreActivityAvailabilityMessage message);
}
