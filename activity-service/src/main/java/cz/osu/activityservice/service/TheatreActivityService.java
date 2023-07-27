package cz.osu.activityservice.service;

import cz.osu.activityservice.error.exception.TheatreActivityNotFoundException;
import cz.osu.activityservice.model.database.TheatreActivity;
import cz.osu.activityservice.model.request.ActivityParamRequest;
import cz.osu.activityservice.model.response.TheatreActivityActionResponse;
import cz.osu.activityservice.model.response.TheatreActivityCurrentDayResponse;
import cz.osu.activityservice.model.response.TheatreActivityResponse;
import cz.osu.activityservice.model.response.TheatreActivityTopResponse;
import cz.osu.activityservice.model.response.TheatreActivityDetailsResponse;
import org.springframework.retry.annotation.Recover;

import java.util.List;
import java.util.Map;

public interface TheatreActivityService {
    /**
     * Load activities from XML and save them to the database.
     * After the activity is saved, a message is sent to the rabbitMQ that inform another service about creation.
     */
    void saveAllNewActivities();

    /**
     * Check theatre activity if it is available and update it.
     * Retrieves information about the time when the performance ends and compares it with the current time (UTC).
     *
     * @param theatreActivity the performance to be checked
     * @throws IllegalArgumentException - if passed parameters are null or empty
     */
    void checkTheatreActivityAvailability(TheatreActivity theatreActivity);

    /**
     * searches the database based on the search criteria and creates the appropriate answer with the searched performances.
     *
     * @param searchParameters the parameters that will be used for searching
     * @return list of searched performances saved under key "previews" and number of pages under key "totalPages"
     */
    Map<String, Object> getActivitiesPreviewResponse(ActivityParamRequest searchParameters);

    /**
     * Attempts to search for a performance by idActivity and assigns the user's id to the performance's set.
     * If user's id is already in set, it removes it.
     *
     * @param idActivity id of performance that will be used for searching
     * @return updated performance information (e.g. total number of likes,dislikes,ratings)
     * @throws TheatreActivityNotFoundException idActivity of performance was not found in database
     */
    TheatreActivityActionResponse addLikedActivityToUser(long idActivity);

    /**
     * Attempts to search for a performance by idActivity and assigns the user's id to the performance's set.
     * If user's id is already in set, it removes it.
     *
     * @param idActivity id of performance that will be used for searching
     * @return updated performance information (e.g. total number of likes,dislikes,ratings)
     * @throws TheatreActivityNotFoundException idActivity of performance was not found in database
     */
    TheatreActivityActionResponse addDislikedActivityToUser(long idActivity);

    /**
     * Search databases and load all unique divisions of activities.
     *
     * @return list of all divisions found in database
     */
    List<String> getAllDivisions();

    /**
     * Searches for a performance in the database by idActivity and returns information about the activity, including the time left until the end of the performance and information about the actions the user has performed.
     *
     * @param idActivity id of performance that will be searched in database.
     * @return information about activity with time left and user actions
     */
    TheatreActivityResponse getTheatreActivity(long idActivity);

    /**
     * Finds the top three performances that have the highest ratings and have the highest number of user reviews.
     *
     * @return a list of information about performances
     */
    List<TheatreActivityTopResponse> getTopTheatreActivities();

    /**
     * Loads all performances for current day.
     *
     * @return list of performances with time remaining until the performance is available
     */
    List<TheatreActivityCurrentDayResponse> getTheatreActivitiesForCurrentDay();

    /**
     * Finds a theatre activity by idActivity and assign the user's id among other users who have rated the activity. If the user's id is already in the set, it removes it.
     *
     * @param idActivity  id of performance that will be used for searching
     * @param userID      id of the user who performed the action
     * @param isUpdate    user has not added or removed a rating, no need to go through the set
     * @param ratingValue overall evaluation of the performance
     * @param timeOfSend  the time when the message was sent
     * @throws TheatreActivityNotFoundException if performance was not found in database
     * @throws IllegalArgumentException         if parameters are null or empty
     */
    void ratingActionPerformed(long idActivity, String userID,boolean isUpdate, double ratingValue, String timeOfSend);

    /**
     * Retrieves basic activity information according to available IDs
     * @param ids   List of ids that will be used for the search
     * @return      Basic information about the Theatre activity (idActivity,name,startDate)
     */
    List<TheatreActivityDetailsResponse> getTheatreActivityDetails(List<Long> ids);

    /**
     * Fall back method, which will be called after trying all attempts at the @Retryable annotation
     * @param e handed exception
     */
    void getRetriesFallback(RuntimeException e);
}