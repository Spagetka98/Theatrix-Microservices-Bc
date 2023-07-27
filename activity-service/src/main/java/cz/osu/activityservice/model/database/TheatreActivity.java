package cz.osu.activityservice.model.database;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import cz.osu.activityservice.model.database.embedded.Rating;
import cz.osu.activityservice.model.enums.ETheatreActivitySchemaless;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.*;

@Document(collection = "Program")
@Data
@ToString(of = {"idActivity", "name", "stage", "division", "startDate", "available", "schemalessData"})
public class TheatreActivity {
    @Id
    private String id;

    @Indexed(name = "idActivity_index", unique = true)
    private long idActivity;

    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotBlank(message = "Stage cannot be blank")
    private String stage;

    @NotBlank(message = "Division cannot be blank")
    private String division;

    @NotNull(message = "StartDate cannot be null")
    private Instant startDate;

    private boolean available;

    private Rating rating;

    private Map<String, Object> schemalessData;

    @NotNull(message = "List of liked by users cannot be null")
    private Set<String> likedByUsers;

    @NotNull(message = "List of disliked by users cannot be null")
    private Set<String> dislikedByUsers;

    @NotNull(message = "List of rated by users cannot be null")
    private Set<String> ratedByUsers;

    @Version
    private Long version;

    @JsonAnySetter
    public void setSchemalessData(String key, Object value) {
        if (null == schemalessData) {
            schemalessData = new HashMap<>();
        }
        schemalessData.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getSchemalessData() {
        return schemalessData;
    }

    public Instant getEndTimeOfActivity() {
        String KEY = ETheatreActivitySchemaless.END.getAttribute();

        if (this.getSchemalessData() != null && this.getSchemalessData().containsKey(KEY)){
            try{
                return ((Date) this.getSchemalessData().get(KEY)).toInstant();
            }catch (ClassCastException e){
                return ((Instant) this.getSchemalessData().get(KEY));
            }catch (Exception e){
                throw new IllegalArgumentException(String.format("Error in TheatreActivity.getEndTimeOfActivity when retrieving endDate for activity with id: %d",this.getIdActivity()));
            }
        }
        else
            return this.startDate;
    }

    private TheatreActivity() {
        this.likedByUsers = new HashSet<>();
        this.dislikedByUsers = new HashSet<>();
        this.ratedByUsers = new HashSet<>();
        this.rating = new Rating(0);
    }

    public TheatreActivity(long idActivity, @NonNull String name, @NonNull String stage, @NonNull String division, @NonNull Instant startDate,
                           boolean available) {
        this();
        this.idActivity = idActivity;
        this.name = name;
        this.stage = stage;
        this.division = division;
        this.startDate = startDate;
        this.available = available;

    }

    @Override
    public int hashCode() {
        return Objects.hash(idActivity, name);
    }

    @Override
    public boolean equals(Object activity) {
        if (activity == this) return true;

        if (activity == null || activity.getClass() != this.getClass()) return false;

        TheatreActivity guest = (TheatreActivity) activity;
        return this.getId().compareTo(guest.getId()) == 0;
    }
}
