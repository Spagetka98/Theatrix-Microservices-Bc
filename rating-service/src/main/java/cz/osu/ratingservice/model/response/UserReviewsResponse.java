package cz.osu.ratingservice.model.response;

import cz.osu.ratingservice.model.database.Review;
import lombok.Data;

@Data
public class UserReviewsResponse {
    private long activityID;
    private String username;
    private String title;
    private int rating;
    private String text;
    private String creation;
    private String modified;

    public UserReviewsResponse(long idActivity,Review review) {
        this.activityID = idActivity;
        this.username = review.getUsername();
        this.title = review.getTitle();
        this.rating = review.getRating();
        this.text = review.getText();
        this.creation = review.getCreatedAt().toString();
        this.modified = review.getLastModified().toString();
    }
}
