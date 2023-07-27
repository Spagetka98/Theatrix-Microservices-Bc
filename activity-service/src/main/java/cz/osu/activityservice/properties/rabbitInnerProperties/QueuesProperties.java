package cz.osu.activityservice.properties.rabbitInnerProperties;

import cz.osu.activityservice.utility.ExceptionUtility;

public record QueuesProperties(String ratingActionQueue,
                               String newTheatreActivityQueue,
                               String deadLetterNewTheatreActivityQueue,
                               String deadLetterRatingActionQueue,
                               String errorRatingActionQueue) {
    public QueuesProperties{
        ExceptionUtility.checkInput(ratingActionQueue,"Parameter ratingActionQueue in QueuesProperties cannot be null or empty !");
        ExceptionUtility.checkInput(newTheatreActivityQueue,"Parameter newTheatreActivityQueue in QueuesProperties cannot be null or empty !");
        ExceptionUtility.checkInput(deadLetterNewTheatreActivityQueue,"Parameter deadLetterNewTheatreActivityQueue in QueuesProperties cannot be null or empty !");
        ExceptionUtility.checkInput(deadLetterRatingActionQueue,"Parameter deadLetterRatingActionQueue in QueuesProperties cannot be null or empty !");
        ExceptionUtility.checkInput(errorRatingActionQueue,"Parameter errorRatingActionQueue in QueuesProperties cannot be null or empty !");
    }
}
