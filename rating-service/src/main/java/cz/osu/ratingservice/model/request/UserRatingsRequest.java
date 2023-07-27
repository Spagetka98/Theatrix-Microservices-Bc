package cz.osu.ratingservice.model.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public record UserRatingsRequest(
        @NotBlank(message = "Username cannot be blank")
        String username,
        @Min(value = 0, message = "Current page cannot be below 0")
        int currentPage,
        @Min(value = 1, message = "Size of page cannot be below 0")
        int sizeOfPage
){
}
