package cz.osu.activityservice.model.response;

import cz.osu.activityservice.model.enums.ETheatreActivitySchemaless;
import cz.osu.activityservice.model.database.TheatreActivity;
import cz.osu.activityservice.utility.SchemalessFilter;
import lombok.Data;

import java.util.Map;

@Data
public class TheatreActivityCurrentDayResponse {
    private String name;
    private String division;
    private String startDate;
    private boolean available;
    private Map<String, Object> schemalessData;
    private long milliSecLeft;

    public TheatreActivityCurrentDayResponse(TheatreActivity theatreActivity, long timeLeft) {
        this.name = theatreActivity.getName();
        this.division = theatreActivity.getDivision();
        this.startDate = theatreActivity.getStartDate().toString();
        this.available = theatreActivity.isAvailable();
        this.schemalessData = SchemalessFilter.getData(theatreActivity, ETheatreActivitySchemaless.END, ETheatreActivitySchemaless.AUTHOR, ETheatreActivitySchemaless.URL);
        this.milliSecLeft = timeLeft;
    }
}
