package cz.osu.ratingservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import java.time.LocalDate;
import java.util.List;


@Setter
@Getter
public class RatingParamRequest {
        private long idActivity;
        @Min(value = 0,message = "Minimum value of page is 0")
        private int page = 0;
        @Min(value = 0,message = "Minimum activities per page is 0")
        private int sizeOfPage = 5;
        private List<Integer> ratings;
        @DateTimeFormat(pattern = "dd-MM-yyyy")
        private LocalDate startDate;
        @DateTimeFormat(pattern = "dd-MM-yyyy")
        private LocalDate endDate;
        private String zoneID;
}
