package cz.osu.activityservice.properties.rabbitInnerProperties;

import cz.osu.activityservice.utility.ExceptionUtility;

public record KeysProperties(String newTheatreActivityKey,
                             String ratingActionChangedKey,
                             String deadLetterNewTheatreActivityKey,
                             String errorRatingActionKey) {
    public KeysProperties{
        ExceptionUtility.checkInput(newTheatreActivityKey,"Parameter newTheatreActivityKey in KeysProperties cannot be null or empty !");
        ExceptionUtility.checkInput(ratingActionChangedKey,"Parameter ratingActionChangedKey in KeysProperties cannot be null or empty !");
        ExceptionUtility.checkInput(deadLetterNewTheatreActivityKey, "Parameter deadLetterNewTheatreActivityKey in KeysProperties cannot be null or empty !");
        ExceptionUtility.checkInput(errorRatingActionKey, "Parameter errorRatingActionKey in KeysProperties cannot be null or empty !");
    }
}
