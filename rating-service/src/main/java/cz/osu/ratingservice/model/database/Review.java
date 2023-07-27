package cz.osu.ratingservice.model.database;

import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Objects;

@Document(collection = "Reviews")
@CompoundIndexes({
        @CompoundIndex(name = "user_review", def = "{'ratingID' : 1, 'userID' : 1}", unique = true)
})
@Data
@ToString(of = { "id", "ratingID","userID","rating" })
public class Review {
    @Id
    private String id;

    @NotBlank(message = "Title cannot be blank")
    private String ratingID;

    @NotBlank(message = "UserID cannot be blank")
    private String userID;

    @NotBlank(message = "Username cannot be blank")
    private String username;

    @Min(value = 1, message = "Minimum value of rating is 1 char")
    @Max(value = 5, message = "Max value of rating is 5 char")
    private int rating;

    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotBlank(message = "Rating text cannot be blank")
    private String text;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant lastModified;

    @Version
    private Long version;

    public Review(int rating, @NonNull String ratingID,@NonNull String title, @NonNull String text, @NonNull String userID, @NonNull String username) {
        this.ratingID = ratingID;
        this.rating = rating;
        this.title = title;
        this.text = text;
        this.userID = userID;
        this.username=username;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUserID());
    }

    @Override
    public boolean equals(Object rating) {
        if (rating == this) return true;

        if (rating == null || rating.getClass() != this.getClass()) return false;

        Review guest = (Review) rating;
        return this.getUserID().equals(guest.getUserID());
    }
}
