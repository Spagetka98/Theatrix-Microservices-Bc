package cz.osu.activityservice.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ActivityParamRequest {
        @Min(value = 0,message = "Minimum value of page is 0")
        private int page = 0;
        @Min(value = 0,message = "Minimum activities per page is 0")
        private int sizeOfPage = 6;
        private List<String> divisions;
        private String activityName;
        private String authorName;
        @DateTimeFormat(pattern = "dd-MM-yyyy")
        private LocalDate startDate;
        @DateTimeFormat(pattern = "dd-MM-yyyy")
        private LocalDate endDate;
        private Boolean liked;
        private Boolean disliked;
        private Boolean rated;
        private String zoneID;
}
