package cz.osu.authenticationservice.utility;

import org.springframework.security.authentication.BadCredentialsException;

public class ExceptionUtility {
    /**
     * Check value if it is not null or empty
     *
     * @param value        value to be checked
     * @param errorMessage message that will be displayed
     * @throws BadCredentialsException - if value is null or empty
     */
    public static void checkCredentials(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) throw new BadCredentialsException(errorMessage);
    }

    /**
     * Check value if it is not null or empty
     *
     * @param value        value to be checked
     * @param errorMessage message that will be displayed
     * @throws IllegalArgumentException - if value is null or empty
     */
    public static void checkInput(String value, String errorMessage) {
        if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException(errorMessage);
    }

    /**
     * Check value if it is not null
     *
     * @param value        value to be checked
     * @param errorMessage message that will be displayed
     * @throws IllegalArgumentException - if value is null
     */
    public static void checkInput(Object value, String errorMessage) {
        if (value == null) throw new IllegalArgumentException(errorMessage);
    }
}
