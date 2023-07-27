package cz.osu.authenticationservice.error.exception;

public class IncorrectRegistrationInformation extends RuntimeException {
    public IncorrectRegistrationInformation(String message){
        super(message);
    }
}
