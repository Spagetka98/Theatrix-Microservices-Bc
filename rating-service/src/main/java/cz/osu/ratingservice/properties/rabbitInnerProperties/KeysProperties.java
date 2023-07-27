package cz.osu.ratingservice.properties.rabbitInnerProperties;

import cz.osu.ratingservice.utility.ExceptionUtility;

public record KeysProperties(String ratingActionChangedKey,
                             String newTheatreActivityKey,
                             String deadLetterRatingActionChangedKey,
                             String errorNewTheatreActivityKey) {

    public KeysProperties{
        ExceptionUtility.checkInput(ratingActionChangedKey,"Parameter ratingActionChangedKey in KeysProperties cannot be null or empty !");
        ExceptionUtility.checkInput(newTheatreActivityKey,"Parameter newTheatreActivityKey in KeysProperties cannot be null or empty !");
        ExceptionUtility.checkInput(deadLetterRatingActionChangedKey,"Parameter deadLetterRatingActionChangedKey in KeysProperties cannot be null or empty !");
        ExceptionUtility.checkInput(errorNewTheatreActivityKey,"Parameter errorNewTheatreActivityKey in KeysProperties cannot be null or empty !");
    }
}
