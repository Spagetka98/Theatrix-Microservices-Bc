package cz.osu.ratingservice.service;

import cz.osu.ratingservice.error.exception.*;
import cz.osu.ratingservice.model.database.Rating;
import cz.osu.ratingservice.model.database.Review;
import cz.osu.ratingservice.model.request.RatingParamRequest;
import cz.osu.ratingservice.model.response.RatingUserResponse;
import cz.osu.ratingservice.model.response.RatingsCountResponse;
import org.springframework.dao.DuplicateKeyException;

import java.time.Instant;
import java.util.Map;

public interface RatingService {

    /**
     * Attempts to add the user's review to the database under transaction
     *
     * @param idActivity id of the activity to which the review will be added
     * @param rating     the value of given review
     * @param title      the title of review
     * @param text       the text body of review
     * @throws RatingNotFoundException     if rating with idActivity was not found
     * @throws RatingNotAvailableException if rating is not ready for evaluation
     * @throws AlreadyRatedException       if the user has already created a review for the performance
     * @throws RatingCreationException     if an error occurred during the creation of the review
     * @throws IllegalArgumentException    if passed parameters are null or empty
     */
    void addReview(long idActivity, int rating, String title, String text);

    /**
     * Attempts to update the user's review
     *
     * @param idActivity id of the activity whose review will be updated
     * @param rating     the value of given review
     * @param title      the title of review
     * @param text       the text body of review
     * @throws RatingNotFoundException     if rating with idActivity was not found
     * @throws RatingNotAvailableException if rating is not ready for evaluation
     * @throws ReviewNotFoundException     if the review that should have been changed was not found
     * @throws RatingUpdatingException     if an error occurred during the updating of the review
     * @throws IllegalArgumentException    if passed parameters are null or empty
     */
    void updateReview(long idActivity, int rating, String title, String text);

    /**
     * Moves the review based on rating value to the appropriate set and updates rating and review under transaction
     *
     * @param rating      rating of performance which must be recalculated and it's sets updated
     * @param review      review to be modified
     * @param title       the title of review
     * @param text        the text body of review
     * @param ratingValue the value of given review
     * @throws RatingUpdatingException  if an error occurred during the updating of the review or rating
     * @throws IllegalArgumentException if passed parameters are null or empty
     */
    void changeRatingSet(Rating rating, Review review, String title, String text, int ratingValue);

    /**
     * Attempts to update the user's review
     *
     * @param idActivity id of the activity whose review will be deleted
     * @throws RatingNotFoundException if rating with idActivity was not found
     * @throws ReviewNotFoundException if the review that should have been changed was not found
     * @throws RatingDeletingException if an error occurred during the deleting of the review
     */
    void deleteReview(long idActivity);

    /**
     * Returns all information about performance ratings (e.g. total size of each sets with reviews).
     *
     * @param idActivity id of the activity about which information will be sent
     * @return information about total rating and size of each set of value
     * @throws RatingNotFoundException if rating for idActivity was not found
     */
    RatingsCountResponse getRatingsOfActivity(long idActivity);

    /**
     * Retrieves the user's personal review for the certain performance.
     *
     * @param idActivity id of the activity about which information will be sent
     * @return user's review
     * @throws RatingNotFoundException if rating for idActivity was not found
     */
    RatingUserResponse getRatingOfUser(long idActivity);

    /**
     * Find reviews in database based on search criteria.
     *
     * @param ratingParamRequest criteria that will be used for searching
     * @return list of searched reviews saved under key "previews" and number of pages under key "totalPages"
     * @throws RatingNotFoundException if rating for idActivity was not found
     */
    Map<String, Object> getReviews(RatingParamRequest ratingParamRequest);

    /**
     * A Theatre Activity record is created/modified based on the received message.
     * However, if the isErasingMessage flag is set to true in the message, the record is deleted.
     *
     * @param idActivity       id of the activity for which the rating will be created
     * @param startDate        date when the performance will be available
     * @param isErasingMessage if true then the Rating service will delete the corresponding record
     * @throws DuplicateKeyException if a rating already exists for the activity with the given id
     */
    void checkRatingRecord(long idActivity, Instant startDate, boolean isErasingMessage);

    /**
     * Retrieves user's reviews for page.
     *
     * @param username    name of the user whose ratings will be searched
     * @param currentPage the page used for the search
     * @param sizeOfPage  number of reviews the page will contain
     * @return list of searched reviews saved under key "previews" and number of pages under key "totalPages"
     */
    Map<String, Object> getUserRatings(String username, int currentPage, int sizeOfPage);

    /**
     * Fall back method, which will be called after trying all attempts at the @Retryable annotation
     *
     * @param e handed exception
     */
    void getRetriesFallback(RuntimeException e);
}
