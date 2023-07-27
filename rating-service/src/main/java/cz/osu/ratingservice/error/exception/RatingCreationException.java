package cz.osu.ratingservice.error.exception;

public class RatingCreationException extends RuntimeException {
    public RatingCreationException(String message) {
        super(message);
    }
}
