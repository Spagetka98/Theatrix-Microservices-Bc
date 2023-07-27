package cz.osu.activityservice.model.response;

import cz.osu.activityservice.model.enums.ETheatreActivitySchemaless;
import cz.osu.activityservice.model.database.TheatreActivity;
import cz.osu.activityservice.utility.SchemalessFilter;
import lombok.Data;

import java.util.Map;

@Data
public class TheatreActivityPreviewResponse {
    private long idActivity;
    private String name;
    private String division;
    private String startDate;
    private double rating;
    private boolean available;
    private Map<String, Object> schemalessData;
    private boolean isLikedByUser;
    private boolean isDislikedByUser;
    private boolean isRatedByUser;
    private long totalLiked;
    private long totalDisliked;
    private long totalRated;
    private long milliSecLeft;

    public TheatreActivityPreviewResponse(TheatreActivity theatreActivity, boolean isLikedByUser, boolean isDislikedByUser, boolean isRatedByUser, long timeLeft) {
        this.isLikedByUser = isLikedByUser;
        this.isDislikedByUser = isDislikedByUser;
        this.isRatedByUser = isRatedByUser;
        this.idActivity = theatreActivity.getIdActivity();
        this.name = theatreActivity.getName();
        this.division = theatreActivity.getDivision();
        this.startDate = theatreActivity.getStartDate().toString();
        this.rating = theatreActivity.getRating().getRatingValue();
        this.available = theatreActivity.isAvailable();
        this.schemalessData = SchemalessFilter.getData(theatreActivity, ETheatreActivitySchemaless.AUTHOR,ETheatreActivitySchemaless.END);
        this.totalLiked = theatreActivity.getLikedByUsers().size();
        this.totalDisliked = theatreActivity.getDislikedByUsers().size();
        this.totalRated = theatreActivity.getRatedByUsers().size();
        this.milliSecLeft = timeLeft;
    }
}
