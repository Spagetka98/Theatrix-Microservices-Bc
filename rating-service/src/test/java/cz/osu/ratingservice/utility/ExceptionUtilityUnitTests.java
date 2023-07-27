package cz.osu.ratingservice.utility;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExceptionUtilityUnitTests {

    @DisplayName("checkInput - Should throw IllegalArgumentException with certain message because testedValue is null")
    @Test
    void checkInputWhenTestedValueIsNull() {
        //region SET_UP
        String testedText = null;
        String errorMessage = "ERROR_MESSAGE";
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ExceptionUtility.checkInput(testedText, errorMessage))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(errorMessage);
        //endregion
    }

    @DisplayName("checkInput - Should throw IllegalArgumentException with certain message because testedValue is empty string")
    @Test
    void checkInputWhenTestedValueIsEmptyString() {
        //region SET_UP
        String testedText = "";
        String errorMessage = "ERROR_MESSAGE";
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ExceptionUtility.checkInput(testedText, errorMessage))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(errorMessage);
        //endregion
    }
}
