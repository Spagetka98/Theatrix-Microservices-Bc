package cz.osu.ratingservice.model.response;

import lombok.Data;

@Data
public class RatingUserResponse {
    private boolean userRated;
    private Integer userRating;
    private String ratingTitle;
    private String ratingText;

    public RatingUserResponse(boolean userRated, Integer userRating, String ratingTitle, String ratingText) {
        this.userRated = userRated;
        this.userRating = userRating;
        this.ratingTitle = ratingTitle;
        this.ratingText = ratingText;
    }
}