package cz.osu.ratingservice.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;


public record RatingRequest(
        long idActivity,

        @Min(value = 1, message = "Minimum rating is 1")
        @Max(value = 5, message = "Maximum rating is 5")
        int rating,

        @NotBlank(message = "Title cannot be blank")
        String title,

        @NotBlank(message = "Text cannot be blank")
        String text
) {

}
