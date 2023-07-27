package cz.osu.activityservice.controller;

import cz.osu.activityservice.model.request.ActivitiesDetailsRequest;
import cz.osu.activityservice.model.request.ActivityActionRequest;
import cz.osu.activityservice.model.request.ActivityParamRequest;
import cz.osu.activityservice.model.response.TheatreActivityActionResponse;
import cz.osu.activityservice.model.response.TheatreActivityCurrentDayResponse;
import cz.osu.activityservice.model.response.TheatreActivityResponse;
import cz.osu.activityservice.model.response.TheatreActivityTopResponse;
import cz.osu.activityservice.model.response.TheatreActivityDetailsResponse;
import cz.osu.activityservice.service.TheatreActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityController {
    private final TheatreActivityService theatreActivityService;

    @GetMapping("/getTopActivities")
    public ResponseEntity<?> getTopActivities() {
        log.info("Loading top activities");

        List<TheatreActivityTopResponse> theatreActivities = this.theatreActivityService.getTopTheatreActivities();

        log.info("Top activities were loaded!");

        return ResponseEntity.ok(theatreActivities);
    }

    @GetMapping("/getActivitiesForCurrentDay")
    public ResponseEntity<?> getActivitiesForCurrentDay() {
        log.info("Loading activities for current day");

        List<TheatreActivityCurrentDayResponse> theatreActivities = this.theatreActivityService.getTheatreActivitiesForCurrentDay();

        log.info("Activities for current day were loaded!");

        return ResponseEntity.ok(theatreActivities);
    }

    @GetMapping( "/allPreviews")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllPreviewsPage(@Valid ActivityParamRequest searchParameters) {

        log.info(String.format("Loading all TheatreActivities for page: %d with size: %d", searchParameters.getPage(), searchParameters.getSizeOfPage()));

        Map<String, Object> previews = this.theatreActivityService.getActivitiesPreviewResponse(searchParameters);

        log.info(String.format("TheatreActivities for page: %d with size: %d were loaded" , searchParameters.getPage(), searchParameters.getSizeOfPage()));

        return ResponseEntity.ok(previews);
    }

    @GetMapping("/getActivity")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getActivity(
            @RequestParam long id) {
        log.info(String.format("Loading activity with id: %d", id));

        TheatreActivityResponse theatreActivityResponse = this.theatreActivityService.getTheatreActivity(id);

        log.info(String.format("Sending activity with id: %d", id));

        return ResponseEntity.ok(theatreActivityResponse);
    }

    @GetMapping("/getActivitiesDetails")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getActivitiesDetails(
            ActivitiesDetailsRequest activitiesDetailsRequest) {
        log.info(String.format("Loading activity details for ids: %s", activitiesDetailsRequest.getIds()));

        List<TheatreActivityDetailsResponse> theatreActivityDetailsResponse = this.theatreActivityService.getTheatreActivityDetails(activitiesDetailsRequest.getIds());

        log.info(String.format("Sending activity details for ids: %s", activitiesDetailsRequest.getIds()));

        return ResponseEntity.ok(theatreActivityDetailsResponse);
    }

    @GetMapping("/getDivisions")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getAllDivisions() {
        log.info("Loading all division");

        List<String> divisions = this.theatreActivityService.getAllDivisions();

        log.info("Sending all divisions");

        return ResponseEntity.ok(divisions);
    }

    @PostMapping("/activityLiked")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addLikedActivity(
            @RequestBody ActivityActionRequest request) {
        log.info(String.format("Adding a liked activity with id: %d", request.idActivity()));

        TheatreActivityActionResponse response = theatreActivityService.addLikedActivityToUser(request.idActivity());

        log.info(String.format("Liked activity with id: %d was added", request.idActivity()));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/activityDisliked")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addDislikedActivity(
            @RequestBody ActivityActionRequest request) {
        log.info(String.format("Adding a disliked activity with id: %d", request.idActivity()));

        TheatreActivityActionResponse response = this.theatreActivityService.addDislikedActivityToUser(request.idActivity());

        log.info(String.format("Disliked activity with id: %d was added", request.idActivity()));

        return ResponseEntity.ok(response);
    }
}