package cz.osu.activityservice.error.exception;

public class RetryFallException extends RuntimeException {
    public RetryFallException(String message){
        super(message);
    }
}
