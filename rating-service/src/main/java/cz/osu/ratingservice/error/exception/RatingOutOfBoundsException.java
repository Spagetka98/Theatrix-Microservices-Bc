package cz.osu.ratingservice.error.exception;

public class RatingOutOfBoundsException extends RuntimeException {
    public RatingOutOfBoundsException(String message) {
        super(message);
    }
}
