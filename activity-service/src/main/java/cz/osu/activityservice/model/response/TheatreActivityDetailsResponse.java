package cz.osu.activityservice.model.response;

import cz.osu.activityservice.model.database.TheatreActivity;
import lombok.Data;

import java.time.Instant;

@Data
public class TheatreActivityDetailsResponse {
    private long idActivity;
    private String name;
    private Instant startDate;

    public TheatreActivityDetailsResponse(TheatreActivity theatreActivity){
        this.idActivity = theatreActivity.getIdActivity();
        this.name = theatreActivity.getName();
        this.startDate = theatreActivity.getStartDate();
    }
}
