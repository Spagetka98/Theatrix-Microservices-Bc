package cz.osu.authenticationservice.error.exception;

public class UserInformationTakenException extends RuntimeException {
    public UserInformationTakenException(String takenInformation) {
        super(takenInformation);
    }
}
