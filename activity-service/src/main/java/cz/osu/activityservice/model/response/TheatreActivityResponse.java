package cz.osu.activityservice.model.response;

import cz.osu.activityservice.model.enums.ETheatreActivitySchemaless;
import cz.osu.activityservice.model.database.TheatreActivity;
import cz.osu.activityservice.utility.SchemalessFilter;
import lombok.Data;

import java.util.Map;

@Data
public class TheatreActivityResponse {
    private String name;
    private String stage;
    private String division;
    private String startDate;
    private boolean available;
    private Map<String, Object> schemalessData;
    private boolean isLikedByUser;
    private boolean isDislikedByUser;
    private int totalLiked;
    private int totalDisliked;
    private long milliSecLeft;

    public TheatreActivityResponse(TheatreActivity theatreActivity, long timeLeft, boolean isLikedByUser, boolean isDislikedByUser) {
        this.name = theatreActivity.getName();
        this.stage = theatreActivity.getStage();
        this.division = theatreActivity.getDivision();
        this.startDate = theatreActivity.getStartDate().toString();
        this.available = theatreActivity.isAvailable();
        this.schemalessData = SchemalessFilter.getData(theatreActivity, ETheatreActivitySchemaless.AUTHOR,ETheatreActivitySchemaless.END,ETheatreActivitySchemaless.URL,ETheatreActivitySchemaless.DESCRIPTION);
        this.isLikedByUser = isLikedByUser;
        this.isDislikedByUser = isDislikedByUser;
        this.totalLiked = theatreActivity.getLikedByUsers().size();
        this.totalDisliked = theatreActivity.getDislikedByUsers().size();
        this.milliSecLeft = timeLeft;
    }
}