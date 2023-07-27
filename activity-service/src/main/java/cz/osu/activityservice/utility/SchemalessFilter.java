package cz.osu.activityservice.utility;

import cz.osu.activityservice.model.enums.ETheatreActivitySchemaless;
import cz.osu.activityservice.model.database.TheatreActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SchemalessFilter {
    /**
     * Filters by key all performance attributes that can be schemaless.
     *
     * @param theatreActivity performance that will be filtered
     * @param keys            attributes that will be searched for
     * @return filtered attributes and their values
     * @throws IllegalArgumentException if passed parameters are null or empty
     */
    public static Map<String, Object> getData(TheatreActivity theatreActivity, ETheatreActivitySchemaless... keys) {
        ExceptionUtility.checkInput(theatreActivity, "Parameter TheatreActivity in SchemalessFilter.getData cannot be null !");
        ExceptionUtility.checkArray(keys, "Parameter keys in SchemalessFilter.getData cannot be null or empty !");

        Map<String, Object> data = new HashMap<>();

        Arrays.stream(keys).toList().forEach((key) -> {
            String KEY = key.getAttribute();
            Map<String, Object> schemalessData = theatreActivity.getSchemalessData();

            if (schemalessData != null && schemalessData.containsKey(KEY)) {
                data.put(KEY, schemalessData.get(KEY));
            }
        });

        return data;
    }
}
