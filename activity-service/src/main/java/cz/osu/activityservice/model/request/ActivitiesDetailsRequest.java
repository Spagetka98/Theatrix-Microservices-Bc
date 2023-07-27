package cz.osu.activityservice.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ActivitiesDetailsRequest {
    private List<Long> ids;
}
