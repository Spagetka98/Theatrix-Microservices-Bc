package cz.osu.ratingservice.model.response;

import cz.osu.ratingservice.model.database.Rating;
import lombok.Data;

@Data
public class RatingsCountResponse {
    private double activityRating;
    private long totalRated;
    private long totalOneRatings;
    private long totalTwoRatings;
    private long totalThreeRatings;
    private long totalFourRatings;
    private long totalFiveRatings;

    public RatingsCountResponse(Rating rating) {
        this.activityRating = rating.getRating();
        this.totalRated = rating.getTotalRatings();
        this.totalOneRatings = rating.getOneStar().size();
        this.totalTwoRatings = rating.getTwoStar().size();
        this.totalThreeRatings = rating.getThreeStar().size();
        this.totalFourRatings = rating.getFourStar().size();
        this.totalFiveRatings = rating.getFiveStar().size();
    }
}
