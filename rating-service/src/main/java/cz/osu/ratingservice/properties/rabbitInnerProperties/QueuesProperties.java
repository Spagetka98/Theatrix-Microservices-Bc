package cz.osu.ratingservice.properties.rabbitInnerProperties;

import cz.osu.ratingservice.utility.ExceptionUtility;

public record QueuesProperties(String newTheatreActivityQueue,
                               String deadLetterNewTheatreActivityQueue,
                               String ratingActionQueue,
                               String deadLetterRatingActionQueue,
                               String errorNewTheatreActivityQueue) {
    public QueuesProperties {
        ExceptionUtility.checkInput(newTheatreActivityQueue, "Parameter newTheatreActivityQueue in QueuesProperties cannot be null or empty !");
        ExceptionUtility.checkInput(deadLetterNewTheatreActivityQueue, "Parameter deadLetterNewTheatreActivityQueue in QueuesProperties cannot be null or empty !");
        ExceptionUtility.checkInput(ratingActionQueue, "Parameter ratingActionQueue in QueuesProperties cannot be null or empty !");
        ExceptionUtility.checkInput(deadLetterRatingActionQueue, "Parameter deadLetterRatingActionQueue in QueuesProperties cannot be null or empty !");
        ExceptionUtility.checkInput(errorNewTheatreActivityQueue, "Parameter errorNewTheatreActivityQueue in QueuesProperties cannot be null or empty !");
    }
}
