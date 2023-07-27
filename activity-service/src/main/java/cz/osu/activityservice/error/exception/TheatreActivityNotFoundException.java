package cz.osu.activityservice.error.exception;

public class TheatreActivityNotFoundException extends RuntimeException{
    public TheatreActivityNotFoundException(String message){
        super(message);
    }
}
