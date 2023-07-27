package cz.osu.ratingservice.error.exception;

public class RetryFallException extends RuntimeException {
    public RetryFallException(String message) {
        super(message);
    }
}
