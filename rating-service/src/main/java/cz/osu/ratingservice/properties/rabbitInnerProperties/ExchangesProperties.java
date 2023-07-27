package cz.osu.ratingservice.properties.rabbitInnerProperties;

import cz.osu.ratingservice.utility.ExceptionUtility;

public record ExchangesProperties(String deadLetterExchange,
                                  String ratingExchange,
                                  String newTheatreActivityExchange,
                                  String errorExchange) {
    public ExchangesProperties{
        ExceptionUtility.checkInput(deadLetterExchange,"Parameter deadLetterExchange in ExchangesProperties cannot be null or empty !");
        ExceptionUtility.checkInput(ratingExchange,"Parameter ratingExchange in ExchangesProperties cannot be null or empty !");
        ExceptionUtility.checkInput(newTheatreActivityExchange,"Parameter newTheatreActivityExchange in ExchangesProperties cannot be null or empty !");
        ExceptionUtility.checkInput(errorExchange,"Parameter errorExchange in ExchangesProperties cannot be null or empty !");
    }
}
