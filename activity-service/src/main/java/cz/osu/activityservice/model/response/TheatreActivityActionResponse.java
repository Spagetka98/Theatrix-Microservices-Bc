package cz.osu.activityservice.model.response;

import cz.osu.activityservice.model.database.TheatreActivity;
import cz.osu.activityservice.model.pojo.UserDetails;
import lombok.Data;

@Data
public class TheatreActivityActionResponse {
    private boolean isLikedByUser;
    private boolean isDislikedByUser;
    private boolean isRatedByUser;
    private long totalLiked;
    private long totalDisliked;
    private long totalRated;

    public TheatreActivityActionResponse(UserDetails user, TheatreActivity theatreActivity) {
        this.isLikedByUser = theatreActivity.getLikedByUsers().contains(user.userId());
        this.isDislikedByUser = theatreActivity.getDislikedByUsers().contains(user.userId());
        this.isRatedByUser =theatreActivity.getRatedByUsers().contains(user.userId());
        this.totalLiked = theatreActivity.getLikedByUsers().size();
        this.totalDisliked = theatreActivity.getDislikedByUsers().size();
        this.totalRated = theatreActivity.getRatedByUsers().size();
    }
}
