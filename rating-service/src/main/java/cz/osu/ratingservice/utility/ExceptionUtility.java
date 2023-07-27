package cz.osu.ratingservice.utility;

public class ExceptionUtility {
    /**
     * Check value if it is not null or empty
     *
     * @param value        value to be checked
     * @param errorMessage message that will be displayed
     * @throws IllegalArgumentException - if value is null or empty
     */
    public static void checkInput(String value, String errorMessage) {
        if (value == null || value.isEmpty()) throw new IllegalArgumentException(errorMessage);
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
