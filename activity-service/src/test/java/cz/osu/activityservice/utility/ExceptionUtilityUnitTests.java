package cz.osu.activityservice.utility;

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

    @DisplayName("checkArray - Should throw IllegalArgumentException with certain message because testedArray is empty (size = 0)")
    @Test
    void checkInputWhenTestedArrayIsEmpty() {
        //region SET_UP
        String[] testedArray = new String[0];
        String errorMessage = "ERROR_MESSAGE";
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ExceptionUtility.checkArray(testedArray, errorMessage))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(errorMessage);
        //endregion
    }

    @DisplayName("checkArray - Should throw IllegalArgumentException with certain message because testedArray is null")
    @Test
    void checkInputWhenTestedArrayIsNull() {
        //region SET_UP
        String errorMessage = "ERROR_MESSAGE";
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(() ->
                ExceptionUtility.checkArray(null, errorMessage))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(errorMessage);
        //endregion
    }
}
