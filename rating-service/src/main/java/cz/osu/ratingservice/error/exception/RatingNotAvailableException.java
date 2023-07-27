package cz.osu.ratingservice.error.exception;

public class RatingNotAvailableException extends RuntimeException {
    public RatingNotAvailableException(String message) {
        super(message);
    }
}
