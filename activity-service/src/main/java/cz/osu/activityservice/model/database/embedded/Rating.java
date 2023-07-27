package cz.osu.activityservice.model.database.embedded;

import lombok.Data;
import lombok.ToString;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.Instant;
import java.util.Objects;

@Data
@ToString(of = {"ratingValue", "timeOfUpdate"})
public class Rating {
    @Min(0)
    @Max(5)
    private double ratingValue;

    @LastModifiedDate
    private Instant timeOfUpdate;

    public Rating(double ratingValue){
        this.ratingValue = ratingValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ratingValue,timeOfUpdate);
    }

    @Override
    public boolean equals(Object ratingValue) {
        if (ratingValue == this) return true;

        if (ratingValue == null || ratingValue.getClass() != this.getClass()) return false;

        Rating guest = (Rating) ratingValue;

        return this.getRatingValue()== guest.getRatingValue() && this.getTimeOfUpdate().equals(guest.getTimeOfUpdate());
    }
}
