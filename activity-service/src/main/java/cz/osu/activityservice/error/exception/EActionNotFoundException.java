package cz.osu.activityservice.error.exception;

public class EActionNotFoundException extends RuntimeException{
    public EActionNotFoundException(String message){
        super(message);
    }
}
