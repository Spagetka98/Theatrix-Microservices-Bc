package cz.osu.ratingservice.error.exception;

public class MongoDatabaseUnavailableException extends RuntimeException {
    public MongoDatabaseUnavailableException(String message) {
        super(message);
    }
}
