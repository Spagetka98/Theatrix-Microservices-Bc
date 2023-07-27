package cz.osu.ratingservice.model.database;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Document(collection = "Ratings")
@Data
@ToString(of = { "id", "idActivity","rating" })
public class Rating {
    @Id
    private String id;

    @Indexed(name = "idActivity_index", unique = true)
    private long idActivity;

    private Instant date;

    private boolean available;

    @Min(0)
    @Max(5)
    private double rating;

    @NotNull(message = "One star ratting set cannot be null")
    @DBRef(lazy = true)
    private Set<Review> oneStar;

    @NotNull(message = "Two star ratting set cannot be null")
    @DBRef(lazy = true)
    private Set<Review> twoStar;

    @NotNull(message = "Three star ratting set cannot be null")
    @DBRef(lazy = true)
    private Set<Review> threeStar;

    @NotNull(message = "Four star ratting set cannot be null")
    @DBRef(lazy = true)
    private Set<Review> fourStar;

    @NotNull(message = "Five star ratting set cannot be null")
    @DBRef(lazy = true)
    private Set<Review> fiveStar;

    @Version
    private Long version;

    private Rating(){
        this.available = false;
        this.rating = 0;
        this.oneStar = new HashSet<>();
        this.twoStar = new HashSet<>();
        this.threeStar = new HashSet<>();
        this.fourStar = new HashSet<>();
        this.fiveStar = new HashSet<>();
    }

    public Rating(long idActivity,Instant dateTime){
        this();
        this.idActivity = idActivity;
        this.date = dateTime;
    }

    public long getTotalRatings() {
        return this.getOneStar().size() + this.getTwoStar().size() + this.getThreeStar().size() + this.getFourStar().size() + this.getFiveStar().size();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(),this.getIdActivity());
    }

    @Override
    public boolean equals(Object rating) {
        if (rating == this) return true;

        if (rating == null || rating.getClass() != this.getClass()) return false;

        Rating guest = (Rating) rating;
        return this.getId().equals(guest.getId());
    }
}
