package cz.osu.activityservice.error.exception;

public class MongoDatabaseUnavailableException extends RuntimeException{
    public MongoDatabaseUnavailableException(String message){
        super(message);
    }
}
