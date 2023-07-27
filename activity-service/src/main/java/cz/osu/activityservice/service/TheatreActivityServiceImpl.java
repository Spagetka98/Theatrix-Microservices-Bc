package cz.osu.activityservice.service;

import cz.osu.activityservice.dataLoader.XmlLoader;
import cz.osu.activityservice.error.exception.RetryFallException;
import cz.osu.activityservice.model.database.embedded.Rating;
import cz.osu.activityservice.error.exception.TheatreActivityNotFoundException;
import cz.osu.activityservice.model.database.TheatreActivity;
import cz.osu.activityservice.model.enums.ETheatreActivitySchemaless;
import cz.osu.activityservice.model.pojo.UserDetails;
import cz.osu.activityservice.model.response.*;
import cz.osu.activityservice.repository.TheatreActivityRepository;
import cz.osu.activityservice.model.request.ActivityParamRequest;
import cz.osu.activityservice.model.response.TheatreActivityDetailsResponse;
import cz.osu.activityservice.utility.ExceptionUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.*;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Slf4j
public class TheatreActivityServiceImpl implements TheatreActivityService {
    private final AppUserService appUserServiceImpl;
    private final MessageService messageServiceImpl;
    private final TheatreActivityRepository theatreActivityRepository;

    /**
     * @see TheatreActivityService#saveAllNewActivities()
     */
    @Override
    public void saveAllNewActivities() {
        try {
            XmlLoader xmlLoader = new XmlLoader();

            xmlLoader.getTheatreActivity()
                    .forEach((foundedActivity -> this.theatreActivityRepository.findByIdActivity(foundedActivity.getIdActivity())
                            .ifPresentOrElse(
                                    currentActivity -> {
                                        if (checkForUpdateActivity(currentActivity, foundedActivity))
                                            saveLoadedActivity(currentActivity);
                                    },
                                    () -> saveLoadedActivity(foundedActivity)
                            )
                    ));

            this.checkForCancelledActivities(xmlLoader);
        } catch (Exception e) {
            log.error("An error occurred while loading new Theatre activities");
            log.error("Error message: " + e);
        }
    }

    /**
     * @see TheatreActivityService#checkTheatreActivityAvailability(TheatreActivity)
     */
    @Override
    public void checkTheatreActivityAvailability(TheatreActivity theatreActivity) {
        ExceptionUtility.checkInput(theatreActivity, "Parameter TheatreActivity in TheatreActivityServiceImpl.checkTheatreActivityAvailability cannot be null !");

        if (theatreActivity.isAvailable()) return;

        if (theatreActivity.getEndTimeOfActivity().isBefore(Instant.now())) {
            theatreActivity.setAvailable(true);
            this.theatreActivityRepository.save(theatreActivity);
        }
    }

    /**
     * @see TheatreActivityService#getActivitiesPreviewResponse(ActivityParamRequest)
     */
    @Override
    public Map<String, Object> getActivitiesPreviewResponse(ActivityParamRequest searchParameters) {
        ExceptionUtility.checkInput(searchParameters, "Parameter searchParameters in TheatreActivityServiceImpl.getActivitiesPreviewResponse cannot be null !");

        UserDetails user = this.appUserServiceImpl.getUser();

        Page<TheatreActivity> pageOfActivities = this.getActivitiesForPage(searchParameters, user);

        pageOfActivities.forEach((this::checkTheatreActivityAvailability));
        List<TheatreActivityPreviewResponse> previewResponses = pageOfActivities.getContent().stream()
                .map((theatreActivity -> new TheatreActivityPreviewResponse(
                        theatreActivity,
                        theatreActivity.getLikedByUsers().contains(user.userId()),
                        theatreActivity.getDislikedByUsers().contains(user.userId()),
                        theatreActivity.getRatedByUsers().contains(user.userId()),
                        Duration.between(Instant.now(), theatreActivity.getEndTimeOfActivity()).toMillis())))
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("previews", previewResponses);
        response.put("totalPages", pageOfActivities.getTotalPages());

        return response;
    }

    /**
     * @see TheatreActivityService#addLikedActivityToUser(long)
     */
    @Override
    @Retryable(value = {OptimisticLockingFailureException.class}, maxAttempts = 4, backoff = @Backoff(delay = 1000))
    public TheatreActivityActionResponse addLikedActivityToUser(long idActivity) {
        UserDetails user = this.appUserServiceImpl.getUser();

        TheatreActivity theatreActivity = this.theatreActivityRepository.findByIdActivity(idActivity)
                .orElseThrow(() -> new TheatreActivityNotFoundException(String.format("Theatre activity in TheatreActivityServiceImpl.addLikedActivityToUser with idActivity %d was not found !", idActivity)));

        this.checkTheatreActivityAvailability(theatreActivity);

        if (theatreActivity.getLikedByUsers().contains(user.userId())) {
            theatreActivity.getLikedByUsers().removeAll(Collections.singleton(user.userId()));
        } else {
            theatreActivity.getLikedByUsers().add(user.userId());
            theatreActivity.getDislikedByUsers().removeAll(Collections.singleton(user.userId()));
        }

        this.theatreActivityRepository.save(theatreActivity);

        return new TheatreActivityActionResponse(user, theatreActivity);
    }

    /**
     * @see TheatreActivityService#addDislikedActivityToUser(long)
     */
    @Override
    @Retryable(value = {OptimisticLockingFailureException.class}, maxAttempts = 4, backoff = @Backoff(delay = 1000))
    public TheatreActivityActionResponse addDislikedActivityToUser(long idActivity) {
        UserDetails user = this.appUserServiceImpl.getUser();

        TheatreActivity theatreActivity = this.theatreActivityRepository.findByIdActivity(idActivity)
                .orElseThrow(() -> new TheatreActivityNotFoundException(String.format("Theatre activity in TheatreActivityServiceImpl.addDislikedActivityToUser with idActivity %d was not found !", idActivity)));

        this.checkTheatreActivityAvailability(theatreActivity);

        if (theatreActivity.getDislikedByUsers().contains(user.userId())) {
            theatreActivity.getDislikedByUsers().removeAll(Collections.singleton(user.userId()));
        } else {
            theatreActivity.getDislikedByUsers().add(user.userId());
            theatreActivity.getLikedByUsers().removeAll(Collections.singleton(user.userId()));
        }

        this.theatreActivityRepository.save(theatreActivity);

        return new TheatreActivityActionResponse(user, theatreActivity);
    }

    /**
     * @see TheatreActivityService#getAllDivisions()
     */
    @Override
    public List<String> getAllDivisions() {
        return this.theatreActivityRepository.findDistinctDivision()
                .stream().sorted().collect(Collectors.toList());
    }

    /**
     * @see TheatreActivityService#getTheatreActivity(long)
     */
    @Override
    public TheatreActivityResponse getTheatreActivity(long idActivity) {
        UserDetails user = this.appUserServiceImpl.getUser();

        TheatreActivity theatreActivity = this.theatreActivityRepository.findByIdActivity(idActivity)
                .orElseThrow(() -> new TheatreActivityNotFoundException(String.format("Theatre activity in TheatreActivityServiceImpl.getTheatreActivity with idActivity %d was not found !", idActivity)));

        this.checkTheatreActivityAvailability(theatreActivity);

        long timeLeft = Duration.between(Instant.now(), theatreActivity.getEndTimeOfActivity()).toMillis();

        return new TheatreActivityResponse(theatreActivity, timeLeft, theatreActivity.getLikedByUsers().contains(user.userId()), theatreActivity.getDislikedByUsers().contains(user.userId()));
    }

    /**
     * @see TheatreActivityService#getTopTheatreActivities()
     */
    @Override
    public List<TheatreActivityTopResponse> getTopTheatreActivities() {
        List<TheatreActivity> topActivities = this.theatreActivityRepository.findTop3Activities();

        return topActivities.stream().map((TheatreActivityTopResponse::new)).toList();
    }

    /**
     * @see TheatreActivityService#getTheatreActivitiesForCurrentDay()
     */
    @Override
    public List<TheatreActivityCurrentDayResponse> getTheatreActivitiesForCurrentDay() {
        List<TheatreActivity> theatreActivitiesList = this.theatreActivityRepository
                .findAllByStartDateBetweenOrderByStartDateAsc(
                        LocalDateTime.of(LocalDate.now(), LocalTime.MIN).atZone(ZoneId.systemDefault()).toInstant(),
                        LocalDateTime.of(LocalDate.now(), LocalTime.MAX).atZone(ZoneId.systemDefault()).toInstant()
                );

        return theatreActivitiesList.stream()
                .map((theatreActivity -> {
                    long timeLeft = Duration.between(Instant.now(), theatreActivity.getEndTimeOfActivity()).toMillis();
                    return new TheatreActivityCurrentDayResponse(theatreActivity, timeLeft);
                }))
                .collect(Collectors.toList());
    }

    /**
     * @see TheatreActivityService#ratingActionPerformed(long, String, boolean, double, String)
     */
    @Override
    @Retryable(value = {OptimisticLockingFailureException.class}, maxAttempts = 4, backoff = @Backoff(delay = 1000))
    public void ratingActionPerformed(long idActivity, String userID, boolean isUpdate, double ratingValue, String timeOfSend) {
        ExceptionUtility.checkInput(userID, "Parameter userID in TheatreActivityServiceImpl.ratingActionPerformed cannot be null or empty !");
        ExceptionUtility.checkInput(timeOfSend, "Parameter timeOfSend in TheatreActivityServiceImpl.ratingActionPerformed cannot be null or empty !");

        TheatreActivity theatreActivity = this.theatreActivityRepository.findByIdActivity(idActivity)
                .orElseThrow(() -> new TheatreActivityNotFoundException(String.format("TheatreActivity with idActivity: %d was not found in TheatreActivityServiceImpl.changeRatingValue", idActivity)));

        Instant sendTime = Instant.parse(timeOfSend);

        if (sendTime.isAfter(theatreActivity.getRating().getTimeOfUpdate()))
            theatreActivity.setRating(new Rating(ratingValue));

        if (!isUpdate) {
            if (theatreActivity.getRatedByUsers().contains(userID))
                theatreActivity.getRatedByUsers().remove(userID);
            else
                theatreActivity.getRatedByUsers().add(userID);
        }

        this.theatreActivityRepository.save(theatreActivity);
    }

    /**
     * @see TheatreActivityService#getTheatreActivityDetails(List)
     */
    @Override
    public List<TheatreActivityDetailsResponse> getTheatreActivityDetails(List<Long> ids) {
        List<TheatreActivity> activities = this.theatreActivityRepository.findAllByIdActivityIn(ids);

        return activities.stream()
                .map(TheatreActivityDetailsResponse::new).toList();
    }

    /**
     * @see TheatreActivityService#getRetriesFallback(RuntimeException)
     */
    @Recover
    @Override
    public void getRetriesFallback(RuntimeException e) {
        throw new RetryFallException(e.getMessage());
    }

    /**
     * Checks the search parameters and selects the appropriate query.
     *
     * @param searchParameters the search parameters that will be used for searching in database
     * @param user             the user which is searching (e.g. in his liked activities)
     * @return searched performances that match the parameters
     * @throws IllegalArgumentException if passed parameters are null
     */
    private Page<TheatreActivity> getActivitiesForPage(ActivityParamRequest searchParameters, UserDetails user) {
        ExceptionUtility.checkInput(searchParameters, "Parameter searchParameters in TheatreActivityServiceImpl.getActivitiesForPage cannot be null !");
        ExceptionUtility.checkInput(user, "Parameter user in TheatreActivityServiceImpl.getActivitiesForPage cannot be null !");

        List<String> divisions = searchParameters.getDivisions();
        if (divisions == null || divisions.size() <= 0) return new PageImpl<>(new ArrayList<>());

        divisions = divisions
                .stream().map(division -> URLDecoder.decode(division, StandardCharsets.UTF_8))
                .toList();

        Pageable paging = PageRequest.of(searchParameters.getPage(), searchParameters.getSizeOfPage());

        String activityName = URLDecoder.decode(String.format("^.*%s.*$", ofNullable(searchParameters.getActivityName()).orElse("")), StandardCharsets.UTF_8);

        String authorName = URLDecoder.decode(String.format("^.*%s.*$", ofNullable(searchParameters.getAuthorName()).orElse("")), StandardCharsets.UTF_8);
        boolean isAuthorSearched = !(searchParameters.getAuthorName() == null || searchParameters.getAuthorName().isBlank());

        ZoneId userZoneId = getUserZoneId(searchParameters.getZoneID());

        LocalDateTime start = searchParameters.getStartDate() == null ?
                LocalDateTime.of(2000, 12, 31, 0, 0, 0) :
                LocalDateTime.of(searchParameters.getStartDate(), LocalTime.MIN);
        Instant startDate = start.atZone(userZoneId).toInstant();

        LocalDateTime end = searchParameters.getEndDate() == null ?
                LocalDateTime.of(2200, 12, 31, 23, 59, 59) :
                LocalDateTime.of(searchParameters.getEndDate(), LocalTime.MAX);
        Instant endDate = end.atZone(userZoneId).toInstant();

        if (startDate.isAfter(endDate))
            throw new IllegalArgumentException(String.format("Incorrect parameters were passed! Parameter startDate-(%s) cannot be after endDate-(%s) !", searchParameters.getStartDate().toString(), searchParameters.getEndDate().toString()));

        if (searchParameters.getPage() < 0 || searchParameters.getSizeOfPage() < 0)
            throw new IllegalArgumentException(String.format("Incorrect parameters were passed! Parameter page-(%d) and sizeOfPage-(%d) cannot be negative !", searchParameters.getPage(), searchParameters.getSizeOfPage()));

        if (searchParameters.getLiked() != null && searchParameters.getLiked())
            return this.theatreActivityRepository.searchLikedTheatreActivities(activityName, authorName, isAuthorSearched, startDate, endDate, divisions, user.userId(), paging);
        else if (searchParameters.getDisliked() != null && searchParameters.getDisliked())
            return this.theatreActivityRepository.searchDislikedTheatreActivities(activityName, authorName, isAuthorSearched, startDate, endDate, divisions, user.userId(), paging);
        else if (searchParameters.getRated() != null && searchParameters.getRated())
            return this.theatreActivityRepository.searchRatedTheatreActivities(activityName, authorName, isAuthorSearched, startDate, endDate, divisions, user.userId(), paging);
        else
            return this.theatreActivityRepository.searchTheatreActivities(activityName, authorName, isAuthorSearched, startDate, endDate, divisions, paging);

    }

    /**
     * It tries to convert the inserted string to zoneID and if it fails it returns UTC.
     * It passed parameter is null, it returns UTC.
     *
     * @param zoneID the string that will be used for converting
     * @return appropriate zoneId
     */
    private ZoneId getUserZoneId(String zoneID) {
        if (zoneID == null || zoneID.trim().isEmpty()) return ZoneId.from(ZoneOffset.UTC);
        try {
            return ZoneId.of(URLDecoder.decode(zoneID, StandardCharsets.UTF_8));
        } catch (Exception e) {
            return ZoneId.from(ZoneOffset.UTC);
        }
    }

    /**
     * See if the activity attributes have changed since the last update.
     *
     * @param currentActivity theatre performance in the database
     * @param foundedActivity loaded theatre performances from the NDM website
     * @return true if there has been a change and the theatre performance needs to be updated
     */
    private boolean checkForUpdateActivity(TheatreActivity currentActivity, TheatreActivity foundedActivity) {
        ExceptionUtility.checkInput(currentActivity, "Parameter currentActivity in TheatreActivityServiceImpl.checkForUpdateActivity cannot be null !");
        ExceptionUtility.checkInput(foundedActivity, "Parameter foundedActivity in TheatreActivityServiceImpl.checkForUpdateActivity cannot be null !");

        boolean readyForUpdate = false;

        if (!currentActivity.getName().equals(foundedActivity.getName())) {
            currentActivity.setName(foundedActivity.getName());
            readyForUpdate = true;
        }

        if (!currentActivity.getStage().equals(foundedActivity.getStage())) {
            currentActivity.setStage(foundedActivity.getStage());
            readyForUpdate = true;
        }

        if (!currentActivity.getDivision().equals(foundedActivity.getDivision())) {
            currentActivity.setDivision(foundedActivity.getDivision());
            readyForUpdate = true;
        }

        if (!currentActivity.getStartDate().equals(foundedActivity.getStartDate())) {
            currentActivity.setStartDate(foundedActivity.getStartDate());
            readyForUpdate = true;
        }

        if (checkSchemalessData(currentActivity, foundedActivity, ETheatreActivitySchemaless.AUTHOR)) {
            readyForUpdate = true;
        }
        if (checkSchemalessData(currentActivity, foundedActivity, ETheatreActivitySchemaless.URL)) {
            readyForUpdate = true;
        }
        if (checkSchemalessData(currentActivity, foundedActivity, ETheatreActivitySchemaless.DESCRIPTION)) {
            readyForUpdate = true;
        }

        if (checkEndTime(currentActivity, foundedActivity)) {
            readyForUpdate = true;
        }

        currentActivity.setAvailable(currentActivity.getEndTimeOfActivity().isBefore(Instant.now()));

        return readyForUpdate;

    }

    /**
     * It'll check the end time of the theatre performance for a change
     *
     * @param currentActivity theatre performance in the database
     * @param foundedActivity loaded theatre performances from the NDM website
     * @return true if there has been a change and the theatre performance needs to be updated
     */
    private boolean checkEndTime(TheatreActivity currentActivity, TheatreActivity foundedActivity) {
        ExceptionUtility.checkInput(currentActivity, "Parameter currentActivity in TheatreActivityServiceImpl.checkEndTime cannot be null !");
        ExceptionUtility.checkInput(foundedActivity, "Parameter foundedActivity in TheatreActivityServiceImpl.checkEndTime cannot be null !");

        Instant currentEndTime = currentActivity.getEndTimeOfActivity();
        Instant foundedEndTime = foundedActivity.getEndTimeOfActivity();

        if (currentActivity.getSchemalessData().containsKey(ETheatreActivitySchemaless.END.getAttribute()) &&
                !foundedActivity.getSchemalessData().containsKey(ETheatreActivitySchemaless.END.getAttribute())) {
            currentActivity.getSchemalessData().remove(ETheatreActivitySchemaless.END.getAttribute());
            return true;
        } else if ((!currentActivity.getSchemalessData().containsKey(ETheatreActivitySchemaless.END.getAttribute()) &&
                foundedActivity.getSchemalessData().containsKey(ETheatreActivitySchemaless.END.getAttribute())) || !currentEndTime.equals(foundedEndTime)) {
            currentActivity.setSchemalessData(ETheatreActivitySchemaless.END.getAttribute(), foundedActivity.getEndTimeOfActivity());
            return true;
        }

        return false;
    }

    /**
     * Checks individual non-schematic data of theatre performance
     *
     * @param currentActivity theatre performance in the database
     * @param foundedActivity loaded theatre performances from the NDM website
     * @param key             to access individual data
     * @return true if there has been a change and the theatre performance needs to be updated
     */
    private boolean checkSchemalessData(TheatreActivity currentActivity, TheatreActivity foundedActivity, ETheatreActivitySchemaless key) {
        ExceptionUtility.checkInput(currentActivity, "Parameter currentActivity in TheatreActivityServiceImpl.checkStringSchemalessData cannot be null !");
        ExceptionUtility.checkInput(foundedActivity, "Parameter foundedActivity in TheatreActivityServiceImpl.checkStringSchemalessData cannot be null !");
        ExceptionUtility.checkInput(key, "Parameter key in TheatreActivityServiceImpl.checkStringSchemalessData cannot be null !");

        if (currentActivity.getSchemalessData() != null && foundedActivity.getSchemalessData() != null) {
            boolean isKeyForCurrentActivity = currentActivity.getSchemalessData().containsKey(key.getAttribute());
            boolean isKeyForFoundedActivity = foundedActivity.getSchemalessData().containsKey(key.getAttribute());

            if (isKeyForCurrentActivity && !isKeyForFoundedActivity) {
                currentActivity.getSchemalessData().remove(key.getAttribute());
                return true;
            } else if (isKeyForFoundedActivity && !isKeyForCurrentActivity) {
                currentActivity.getSchemalessData().put(key.getAttribute(), foundedActivity.getSchemalessData().get(key.getAttribute()).toString());
                return true;
            } else if (isKeyForCurrentActivity) {
                if (!currentActivity.getSchemalessData().get(key.getAttribute()).toString().equals(foundedActivity.getSchemalessData().get(key.getAttribute()).toString())) {
                    currentActivity.getSchemalessData().replace(key.getAttribute(), foundedActivity.getSchemalessData().get(key.getAttribute()).toString());
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Saves the theatrical performance and sends a message to the RabbitMQ queue
     *
     * @param theatreActivity Processed theatre performance
     */
    private void saveLoadedActivity(TheatreActivity theatreActivity) {
        log.info(String.format("Saving Theatre activity with id: %s", theatreActivity.getIdActivity()));
        try {
            this.theatreActivityRepository.save(theatreActivity);
            this.messageServiceImpl.sendNewTheatreActivity(theatreActivity,false);
        } catch (Exception ex) {
            log.error("An error occurred while saving new Theatre activity");
            log.error("Error message: " + ex);
        }
    }

    /**
     * Compares the IDs of the loaded theatre activities with the IDs of the theatre activities in the database for a certain time period (from the time of the first loaded theatre activity to the last one).
     * Deletes performances that are not in the given time period.
     * @param xmlLoader instance with loaded theatre activities
     */
    private void checkForCancelledActivities(XmlLoader xmlLoader){
        Instant maxDate = xmlLoader.getTheatreActivity().stream().map(TheatreActivity::getStartDate)
                .max(Comparator.comparing(Instant::toEpochMilli))
                .orElseThrow(() -> new IllegalArgumentException("variable maxDate in checkForCancelledActivities cannot be null !"));

        Instant minDate = xmlLoader.getTheatreActivity().stream().map(TheatreActivity::getStartDate)
                .min(Comparator.comparing(Instant::toEpochMilli))
                .orElseThrow(() -> new IllegalArgumentException("variable minDate in checkForCancelledActivities cannot be null !"));

        List<TheatreActivity> foundedActivitiesForPeriod = this.theatreActivityRepository.searchDateBetween(minDate,maxDate);

        List<Long> idsInDatabase = foundedActivitiesForPeriod.stream()
                .map(TheatreActivity::getIdActivity).toList();

        List<Long> idsFound = xmlLoader.getTheatreActivity().stream()
                .map(TheatreActivity::getIdActivity).toList();

        List<Long> idsOfCancelledActivities = idsInDatabase.stream()
                .filter(element -> !idsFound.contains(element)).toList();

        if(idsOfCancelledActivities.size() == 0 || idsOfCancelledActivities.size() == idsInDatabase.size()) return;

        try {
            List<TheatreActivity> cancelledActivities = this.theatreActivityRepository.findAllByIdActivityIn(idsOfCancelledActivities);
            log.info(String.format("%d Theatre Activities were deleted !",cancelledActivities.size()));
            this.theatreActivityRepository.deleteAll(cancelledActivities);
            cancelledActivities.forEach(activity -> this.messageServiceImpl.sendNewTheatreActivity(activity,true));
        } catch (Exception ex) {
            log.error("An error occurred while deleting old Theatre activities");
            log.error("Error message: " + ex);
        }

        System.out.println(idsOfCancelledActivities);
    }

}
