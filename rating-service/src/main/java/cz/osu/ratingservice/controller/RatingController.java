package cz.osu.ratingservice.controller;

import cz.osu.ratingservice.model.request.RatingParamRequest;
import cz.osu.ratingservice.model.request.RatingRequest;
import cz.osu.ratingservice.model.request.UserRatingsRequest;
import cz.osu.ratingservice.model.response.RatingUserResponse;
import cz.osu.ratingservice.model.response.RatingsCountResponse;
import cz.osu.ratingservice.service.RatingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/rating")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PostMapping("/addRating")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addRating(@Valid @RequestBody RatingRequest ratingRequest) {
        log.info(String.format("Add rating to activity with id: %d", ratingRequest.idActivity()));

        this.ratingService.addReview(ratingRequest.idActivity(), ratingRequest.rating(), ratingRequest.title(), ratingRequest.text());

        log.info("Rating was successfully added");

        return ResponseEntity.ok().build();
    }

    @GetMapping("/getActivityRatings")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getActivityRatings(
            @RequestParam long id) {
        log.info(String.format("Loading ratings of activity with id: %d", id));

        RatingsCountResponse ratingsCountResponse = this.ratingService.getRatingsOfActivity(id);

        log.info("Activity ratings was successfully loaded");

        return ResponseEntity.ok(ratingsCountResponse);
    }

    @GetMapping("/getUserRatings")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserRatings(
            @Valid UserRatingsRequest userRatingsRequest) {
        log.info(String.format("Loading user´s ratings for user with username: %s", userRatingsRequest.username()));

        Map<String, Object> previews = this.ratingService.getUserRatings(userRatingsRequest.username(),userRatingsRequest.currentPage(),userRatingsRequest.sizeOfPage());

        log.info("User´s ratings was successfully loaded");

        return ResponseEntity.ok(previews);
    }

    @GetMapping("/getActivityUserRating")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getActivityUserRating(
            @RequestParam long id) {
        log.info(String.format("Loading user rating of activity with id: %d", id));

        RatingUserResponse ratingsUserResponse = this.ratingService.getRatingOfUser(id);

        log.info("User rating was successfully loaded");

        return ResponseEntity.ok(ratingsUserResponse);
    }

    @DeleteMapping("/deleteActivityRating")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteActivityRating(
            @RequestParam long id) {
        log.info(String.format("Loading rating of activity with id: %d", id));

        this.ratingService.deleteReview(id);

        log.info("Rating was successfully deleted");

        return ResponseEntity.ok().build();
    }

    @PutMapping("/changeActivityRating")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateActivityRating(@Valid @RequestBody RatingRequest ratingRequest) {
        log.info(String.format("Updating rating of activity with id: %d", ratingRequest.idActivity()));

        this.ratingService.updateReview(ratingRequest.idActivity(), ratingRequest.rating(),ratingRequest.title(), ratingRequest.text());

        log.info("Rating was successfully updated");

        return ResponseEntity.ok().build();
    }

    @GetMapping("/getRatingPreviews")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getRatingPreviews(@Valid RatingParamRequest ratingParamRequest) {
        log.info(String.format("Loading rating previews of activity with id: %d", ratingParamRequest.getIdActivity()));

        Map<String, Object> previews = this.ratingService.getReviews(ratingParamRequest);

        log.info("Previews were successfully loaded!");

        return ResponseEntity.ok(previews);
    }

}
