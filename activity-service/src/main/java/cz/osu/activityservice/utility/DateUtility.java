package cz.osu.activityservice.utility;

import cz.osu.activityservice.model.enums.EDateFormats;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateUtility {
    /**
     * Attempts to change the date format.
     *
     * @param date       date to be formatted
     * @param old_format the date format that is currently used
     * @param new_format new format for date parameter
     * @return date in new format
     * @throws IllegalStateException if any error occurs during the change
     */
    public static String changeDateFormat(String date, EDateFormats old_format, EDateFormats new_format) {
        ExceptionUtility.checkInput(date, "Parameter date in DateUtility.changeDateFormat cannot be null or empty !");
        ExceptionUtility.checkInput(old_format, "Parameter old_format in DateUtility.changeDateFormat cannot be null !");
        ExceptionUtility.checkInput(new_format, "Parameter new_format in DateUtility.changeDateFormat cannot be null !");

        try {
            DateTimeFormatter oldDateFormat = DateTimeFormatter.ofPattern(old_format.getValue());

            LocalDate ld = LocalDate.parse(date, oldDateFormat);

            DateTimeFormatter fOut = DateTimeFormatter.ofPattern(new_format.getValue());

            return ld.format(fOut);
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Occurred during parsing String date: %s ", date));
        }
    }

}