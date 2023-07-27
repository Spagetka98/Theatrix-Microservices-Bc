package cz.osu.activityservice.model.response;

import cz.osu.activityservice.model.enums.ETheatreActivitySchemaless;
import cz.osu.activityservice.model.database.TheatreActivity;
import cz.osu.activityservice.utility.SchemalessFilter;
import lombok.Data;

import java.util.Map;

@Data
public class TheatreActivityTopResponse {
    private long idActivity;
    private String name;
    private String division;
    private String stage;
    private String startDate;
    private double rating;
    private Map<String, Object> schemalessData;
    private long totalLiked;
    private long totalDisliked;
    private long totalRatings;

    public TheatreActivityTopResponse(TheatreActivity activity) {
        this.idActivity = activity.getIdActivity();
        this.name = activity.getName();
        this.division = activity.getDivision();
        this.stage = activity.getStage();
        this.startDate = activity.getStartDate().toString();
        this.rating = activity.getRating().getRatingValue();
        this.schemalessData = SchemalessFilter.getData(activity, ETheatreActivitySchemaless.END, ETheatreActivitySchemaless.AUTHOR, ETheatreActivitySchemaless.URL);
        this.totalLiked = activity.getLikedByUsers().size();
        this.totalDisliked = activity.getDislikedByUsers().size();
        this.totalRatings = activity.getRatedByUsers().size();
    }


}