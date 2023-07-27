package cz.osu.activityservice.utility;

import cz.osu.activityservice.model.enums.EDateFormats;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DateUtilityUnitTests {

    @DisplayName("changeDateFormat - Should change date format")
    @Test
    void changeDateFormat() {
        //region SET_UP
        String dateToBeChanged = "2022-12-12";
        String expected = "12-12-2022";
        //endregion

        //region TESTING
        String result = DateUtility.changeDateFormat(dateToBeChanged, EDateFormats.YYYY_MM_DD_HYPHEN, EDateFormats.DD_MM_YYYY_HYPHEN);
        //endregion

        //region RESULT_CHECK
        assertThat(result).isEqualTo(expected);
        //endregion
    }

    @DisplayName("changeDateFormat - Should throw IllegalStateException because passed format for date is not correct")
    @Test
    void changeDateFormatWhenFormatIsNotCorrect() {
        //region SET_UP
        String dateToBeChanged = "2022-12-12";
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                DateUtility.changeDateFormat(dateToBeChanged, EDateFormats.DD_MM_YYYY_HH_MM_SS_HYPHEN, EDateFormats.DD_MM_YYYY_HYPHEN)
        ).isInstanceOf(IllegalStateException.class);
        //endregion
    }

    @DisplayName("changeDateFormat - Should throw IllegalArgumentException because passed date is null")
    @Test
    void changeDateFormatWhenPassedDateIsNull() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                DateUtility.changeDateFormat(null, EDateFormats.DD_MM_YYYY_HH_MM_SS_HYPHEN, EDateFormats.DD_MM_YYYY_HYPHEN)
        ).isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("changeDateFormat - Should throw IllegalArgumentException because passed old_format is null")
    @Test
    void changeDateFormatWhenPassedOldFormatIsNull() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                DateUtility.changeDateFormat("12.12.2022",null, EDateFormats.DD_MM_YYYY_HYPHEN)
        ).isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("changeDateFormat - Should throw IllegalArgumentException because passed new_format is null")
    @Test
    void changeDateFormatWhenPassedNewFormatIsNull() {
        //region RESULT_CHECK
        assertThatThrownBy(() ->
                DateUtility.changeDateFormat("12.12.2022", EDateFormats.DD_MM_YYYY_HYPHEN,null)
        ).isInstanceOf(IllegalArgumentException.class);
        //endregion
    }
}
