package cz.osu.authenticationservice.error.exception;

public class AppUserNotFoundException extends RuntimeException{
    public AppUserNotFoundException(String message){
        super(message);
    }
}
