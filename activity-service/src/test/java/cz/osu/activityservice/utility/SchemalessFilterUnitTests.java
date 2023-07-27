package cz.osu.activityservice.utility;

import cz.osu.activityservice.model.database.TheatreActivity;
import cz.osu.activityservice.model.enums.ETheatreActivitySchemaless;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SchemalessFilterUnitTests {

    @DisplayName("getData - Should return Author and Url from theatreActivity schemalessData")
    @Test
    void getData() {
        //region SET_UP
        String expectedAuthor = "AUTHOR";
        String expectedURL = "URL";

        TheatreActivity testedActivity = new TheatreActivity(1, "NAME", "STAGE", "DIVISION", Instant.now(), false);
        testedActivity.setSchemalessData(ETheatreActivitySchemaless.AUTHOR.getAttribute(),expectedAuthor);
        testedActivity.setSchemalessData(ETheatreActivitySchemaless.URL.getAttribute(),expectedURL);
        //endregion

        //region TESTING
        Map<String, Object> result = SchemalessFilter.getData(testedActivity, ETheatreActivitySchemaless.AUTHOR, ETheatreActivitySchemaless.URL);
        //endregion

        //region RESULT_CHECK
        assertThat(result)
                .extracting(ETheatreActivitySchemaless.AUTHOR.getAttribute())
                .asInstanceOf(InstanceOfAssertFactories.type(String.class))
                .isEqualTo(expectedAuthor);

        assertThat(result)
                .extracting(ETheatreActivitySchemaless.URL.getAttribute())
                .asInstanceOf(InstanceOfAssertFactories.type(String.class))
                .isEqualTo(expectedURL);
        //endregion
    }

    @DisplayName("getData - Should throw IllegalArgumentException because passed theatreActivity is null ")
    @Test
    void getDataWhenTheatreActivityIsNull() {
        //region RESULT_CHECK
        assertThatThrownBy(()->
                SchemalessFilter.getData(null, ETheatreActivitySchemaless.AUTHOR, ETheatreActivitySchemaless.URL)
        ).isInstanceOf(IllegalArgumentException.class);
        //endregion
    }

    @DisplayName("getData - Should throw IllegalArgumentException because passed Schemaless Keys are null ")
    @Test
    void getDataWhenSchemalessKeysAreNull() {
        //region SET_UP
        TheatreActivity testedActivity = new TheatreActivity(1, "NAME", "STAGE", "DIVISION", Instant.now(), false);
        //endregion

        //region RESULT_CHECK
        assertThatThrownBy(()->
                SchemalessFilter.getData(testedActivity, null)
        ).isInstanceOf(IllegalArgumentException.class);
        //endregion
    }
}
