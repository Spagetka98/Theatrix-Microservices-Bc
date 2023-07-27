package cz.osu.ratingservice.service;

import cz.osu.ratingservice.error.exception.*;
import cz.osu.ratingservice.model.database.Rating;
import cz.osu.ratingservice.model.database.Review;
import cz.osu.ratingservice.model.pojo.UserDetails;
import cz.osu.ratingservice.model.response.UserReviewsResponse;
import cz.osu.ratingservice.repository.RatingRepository;
import cz.osu.ratingservice.repository.ReviewRepository;
import cz.osu.ratingservice.model.request.RatingParamRequest;
import cz.osu.ratingservice.model.response.ReviewsResponse;
import cz.osu.ratingservice.model.response.RatingUserResponse;
import cz.osu.ratingservice.model.response.RatingsCountResponse;
import cz.osu.ratingservice.utility.ExceptionUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.zone.ZoneRulesException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {
    private final RatingRepository ratingRepository;
    private final ReviewRepository reviewRepository;
    private final AppUserService appUserServiceImpl;
    private final MessageService messageServiceImpl;

    /**
     * @see RatingService#addReview(long, int, String, String)
     */
    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = {AlreadyRatedException.class, RatingCreationException.class, OptimisticLockingFailureException.class})
    @Retryable(value = {OptimisticLockingFailureException.class}, maxAttempts = 4, backoff = @Backoff(delay = 1000))
    public void addReview(long idActivity, int ratingValue, String title, String text) {
        ExceptionUtility.checkInput(title, "Parameter Title in RatingServiceImpl.addRating cannot be null or empty !");
        ExceptionUtility.checkInput(text, "Parameter Text in RatingServiceImpl.addRating cannot be null or empty !");

        UserDetails userDetails = this.appUserServiceImpl.getUserDetails();

        Rating rating = this.ratingRepository.findByIdActivity(idActivity)
                .orElseThrow(() -> new RatingNotFoundException(String.format("Rating with idActivity: %d in RatingServiceImpl.addRating was not found !", idActivity)));

        if (this.checkAvailability(rating)) rating.setAvailable(true);
        else
            throw new RatingNotAvailableException(String.format("Rating with idActivity: %d is not available !", rating.getIdActivity()));

        try {
            Review newReview = new Review(ratingValue, rating.getId(), title, text, userDetails.userId(), userDetails.username());

            Set<Review> ratings = this.getRatingSet(rating, ratingValue);

            this.reviewRepository.save(newReview);

            ratings.add(newReview);

            double newRatingValue = this.recountRatingValue(rating);
            rating.setRating(newRatingValue);

            this.ratingRepository.save(rating);

            messageServiceImpl.sendRatingActionNotice(userDetails.userId(), rating.getIdActivity(), newRatingValue, false);
        } catch (DuplicateKeyException exception) {
            throw new AlreadyRatedException(
                    String.format("Cannot add Rating with userID: %s , because user already rated it !", userDetails.userId()));
        } catch (RatingOutOfBoundsException e) {
            throw e;
        } catch (Exception e) {
            throw new RatingCreationException(String.format("Error occurred when creating rating for idActivity: %d and for userID: %s", idActivity, userDetails.userId()));
        }
    }

    /**
     * @see RatingService#updateReview(long, int, String, String)
     */
    @Override
    public void updateReview(long idActivity, int ratingValue, String title, String text) {
        ExceptionUtility.checkInput(title, "Parameter Title in RatingServiceImpl.updateRating cannot be null or empty!");
        ExceptionUtility.checkInput(text, "Parameter Text in RatingServiceImpl.updateRating cannot be null or empty!");

        String userID = this.appUserServiceImpl.getUserDetails().userId();

        Rating rating = this.ratingRepository.findByIdActivity(idActivity)
                .orElseThrow(() -> new RatingNotFoundException(String.format("Rating with idActivity: %d in RatingServiceImpl.updateRating was not found !", idActivity)));

        Review review = this.reviewRepository.findByUserIDAndRatingID(userID, rating.getId())
                .orElseThrow(() -> new ReviewNotFoundException(String.format("Review with userID: %s in RatingServiceImpl.updateRating was not found !", userID)));

        if (ratingValue == review.getRating() && text.equals(review.getText()) && title.equals(review.getTitle()))
            return;

        if (ratingValue == review.getRating()) {
            review.setText(text);
            review.setTitle(title);

            this.reviewRepository.save(review);
        } else {
            this.changeRatingSet(rating, review, title, text, ratingValue);
        }
    }

    /**
     * @see RatingService#changeRatingSet(Rating, Review, String, String, int)
     */
    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = {RatingUpdatingException.class, OptimisticLockingFailureException.class})
    @Retryable(value = {OptimisticLockingFailureException.class}, maxAttempts = 4, backoff = @Backoff(delay = 1000))
    public void changeRatingSet(Rating rating, Review review, String title, String text, int ratingValue) {
        ExceptionUtility.checkInput(rating, "Parameter Rating in RatingServiceImpl.changeRatingSet cannot be null !");
        ExceptionUtility.checkInput(review, "Parameter Review in RatingServiceImpl.changeRatingSet cannot be null !");
        ExceptionUtility.checkInput(title, "Parameter Title in RatingServiceImpl.changeRatingSet cannot be null or empty !");
        ExceptionUtility.checkInput(text, "Parameter Text in RatingServiceImpl.changeRatingSet cannot be null or empty !");

        try {
            Set<Review> ratingSetToRemove = this.getRatingSet(rating, review.getRating());
            ratingSetToRemove.remove(review);

            review.setTitle(title);
            review.setText(text);
            review.setRating(ratingValue);

            Set<Review> ratingSetToAdd = this.getRatingSet(rating, review.getRating());
            ratingSetToAdd.add(review);

            double newRatingValue = this.recountRatingValue(rating);
            rating.setRating(newRatingValue);

            this.reviewRepository.save(review);
            this.ratingRepository.save(rating);

            messageServiceImpl.sendRatingActionNotice(review.getUserID(), rating.getIdActivity(), newRatingValue, true);
        } catch (Exception e) {
            throw new RatingUpdatingException(String.format("Error occurred when updating rating for idActivity: %d and for userID: %s", rating.getIdActivity(), review.getUserID()));
        }

    }

    /**
     * @see RatingService#deleteReview(long)
     */
    @Override
    @Transactional(transactionManager = "transactionManager", rollbackFor = {RatingDeletingException.class, OptimisticLockingFailureException.class})
    @Retryable(value = {OptimisticLockingFailureException.class}, maxAttempts = 4, backoff = @Backoff(delay = 1000))
    public void deleteReview(long idActivity) {
        String userID = this.appUserServiceImpl.getUserDetails().userId();

        Rating rating = this.ratingRepository.findByIdActivity(idActivity)
                .orElseThrow(() -> new RatingNotFoundException(String.format("Rating with idActivity: %d in RatingServiceImpl.deleteRating was not found !", idActivity)));

        Review review = this.reviewRepository.findByUserIDAndRatingID(userID, rating.getId())
                .orElseThrow(() -> new ReviewNotFoundException(String.format("Review with userID: %s in RatingServiceImpl.deleteRating was not found !", userID)));

        try {
            Set<Review> ratingSetToRemove = this.getRatingSet(rating, review.getRating());

            ratingSetToRemove.remove(review);

            double newRatingValue = this.recountRatingValue(rating);

            rating.setRating(newRatingValue);

            this.reviewRepository.delete(review);

            this.ratingRepository.save(rating);

            messageServiceImpl.sendRatingActionNotice(review.getUserID(), rating.getIdActivity(), newRatingValue, false);
        } catch (Exception e) {
            throw new RatingDeletingException(String.format("Error occurred when deleting rating for idActivity: %d and for userID: %s", rating.getIdActivity(), review.getUserID()));
        }
    }

    /**
     * @see RatingService#getRatingsOfActivity(long)
     */
    @Override
    public RatingsCountResponse getRatingsOfActivity(long idActivity) {
        Rating rating = this.ratingRepository.findByIdActivity(idActivity)
                .orElseThrow(() -> new RatingNotFoundException(String.format("Rating with idActivity: %d in RatingServiceImpl.getRatingsOfActivity was not found !", idActivity)));

        return new RatingsCountResponse(rating);
    }

    /**
     * @see RatingService#getRatingOfUser(long)
     */
    @Override
    public RatingUserResponse getRatingOfUser(long idActivity) {
        String userID = this.appUserServiceImpl.getUserDetails().userId();

        Rating rating = this.ratingRepository.findByIdActivity(idActivity)
                .orElseThrow(() -> new RatingNotFoundException(String.format("Rating with idActivity: %d in RatingServiceImpl.getRatingOfUser was not found !", idActivity)));

        Optional<Review> review = this.reviewRepository.findByUserIDAndRatingID(userID, rating.getId());

        return review.map(value -> new RatingUserResponse(true, value.getRating(), value.getTitle(), value.getText()))
                .orElseGet(() -> new RatingUserResponse(false, null, null, null));

    }

    /**
     * @see RatingService#getReviews(RatingParamRequest)
     */
    @Override
    public Map<String, Object> getReviews(RatingParamRequest ratingParamRequest) {
        ExceptionUtility.checkInput(ratingParamRequest, "Parameter RatingParamRequest in RatingServiceImpl.getReviews cannot be null !");

        Rating rating = this.ratingRepository.findByIdActivity(ratingParamRequest.getIdActivity())
                .orElseThrow(() -> new RatingNotFoundException(String.format("Rating with idActivity: %d in RatingServiceImpl.getReviews was not found !", ratingParamRequest.getIdActivity())));

        this.checkRatingAvailability(rating);

        Page<Review> results = this.searchReviews(rating.getId(), ratingParamRequest);

        List<ReviewsResponse> reviewsResponses = results.getContent().stream()
                .map(ReviewsResponse::new).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("previews", reviewsResponses);
        response.put("totalPages", results.getTotalPages());
        return response;
    }

    /**
     * @see RatingService#checkRatingRecord(long, Instant, boolean) 
     */
    @Override
    public void checkRatingRecord(long idActivity, Instant startDate, boolean isErasingMessage) {
        ExceptionUtility.checkInput(startDate, "Parameter startDate in RatingServiceImpl.createNewRating cannot be null !");

        try {
            if (isErasingMessage)
                this.ratingRepository.findByIdActivity(idActivity).ifPresent((this.ratingRepository::delete));
            else
                this.ratingRepository.findByIdActivity(idActivity)
                        .ifPresentOrElse(rating -> {
                            if (!rating.getDate().equals(startDate)) {
                                rating.setDate(startDate);
                                rating.setAvailable(Instant.now().isAfter(rating.getDate()));
                                this.ratingRepository.save(rating);
                            }
                        }, () -> {
                            this.ratingRepository.save(new Rating(idActivity, startDate));
                        });

        } catch (DuplicateKeyException e) {
            log.error(String.format("Rating for TheatreActivity for idActivity: %d is already in database !, error message: %s", idActivity, e.getMessage()));
        } catch (Exception e) {
            log.error(String.format("Error when creating new Rating for TheatreActivity with idActivity: %d, error message: %s", idActivity, e.getMessage()));
        }
    }

    /**
     * @see RatingService#getUserRatings(String, int, int)
     */
    @Override
    public Map<String, Object> getUserRatings(String username, int currentPage, int sizeOfPage) {
        ExceptionUtility.checkInput(username, "Parameter username in RatingServiceImpl.getUserRatings cannot be null or empty !");

        Page<Review> results = this.reviewRepository.findAllByUsernameOrderByCreatedAtAsc(username, PageRequest.of(currentPage, sizeOfPage));

        List<String> ratingIDs = results.stream().map(Review::getRatingID).toList();
        List<Rating> ratings = this.ratingRepository.findAllByIdIn(ratingIDs);

        List<UserReviewsResponse> userReviewsResponse = new ArrayList<>();
        results.forEach(review -> {
            Optional<Rating> matchingRating = ratings.stream().
                    filter(rating -> review.getRatingID().equals(rating.getId())).
                    findFirst();

            if (matchingRating.isEmpty())
                throw new IllegalStateException(String.format("Could not find rating for review with rating id:%s", review.getRatingID()));

            userReviewsResponse.add(new UserReviewsResponse(matchingRating.get().getIdActivity(), review));
        });

        Map<String, Object> response = new HashMap<>();
        response.put("previews", userReviewsResponse);
        response.put("totalPages", results.getTotalPages());

        return response;
    }

    /**
     * @see RatingService#getRetriesFallback(RuntimeException)
     */
    @Recover
    @Override
    public void getRetriesFallback(RuntimeException e) {
        throw new RetryFallException(e.getMessage());
    }

    /**
     * Checks if the rating is available to add the user's review
     *
     * @param rating the rating that will be checked
     * @throws RatingNotAvailableException if the rating is not available
     */
    private void checkRatingAvailability(Rating rating) {
        if (this.checkAvailability(rating)) {
            rating.setAvailable(true);
            this.ratingRepository.save(rating);
        } else
            throw new RatingNotAvailableException(String.format("Rating with idActivity: %d is not available !", rating.getIdActivity()));
    }

    /**
     * Checks the search parameters for query and applied them.
     *
     * @param ratingID           id rating for which the review will be searched
     * @param ratingParamRequest criteria that will be used for searching
     * @return list of filtered reviews
     * @throws IllegalArgumentException if passed parameter is null
     */
    private Page<Review> searchReviews(String ratingID, RatingParamRequest ratingParamRequest) {
        ExceptionUtility.checkInput(ratingID, "Parameter RatingID in RatingServiceImpl.ratingParamRequest cannot be null or empty !");
        ExceptionUtility.checkInput(ratingParamRequest, "Parameter RatingParamRequest in RatingServiceImpl.ratingParamRequest cannot be null !");

        List<Integer> ratings = ratingParamRequest.getRatings();
        if (ratings == null || ratings.size() <= 0) return new PageImpl<>(new ArrayList<>());

        ZoneId userZone = this.getUserZoneId(ratingParamRequest.getZoneID());

        LocalDateTime start = ratingParamRequest.getStartDate() == null ?
                LocalDateTime.of(2000, 12, 31, 0, 0, 0) :
                LocalDateTime.of(ratingParamRequest.getStartDate(), LocalTime.MIN);
        Instant startDate = start.atZone(userZone).toInstant();

        LocalDateTime end = ratingParamRequest.getEndDate() == null ?
                LocalDateTime.of(2500, 12, 31, 23, 59, 59) :
                LocalDateTime.of(ratingParamRequest.getEndDate(), LocalTime.MAX);
        Instant endDate = end.atZone(userZone).toInstant();

        if (startDate.isAfter(endDate))
            throw new IllegalArgumentException(String.format("Incorrect parameters were passed! Parameter startDate-(%s) cannot be after endDate-(%s) !", ratingParamRequest.getStartDate().toString(), ratingParamRequest.getEndDate().toString()));

        if (ratingParamRequest.getPage() < 0 || ratingParamRequest.getSizeOfPage() < 0)
            throw new IllegalArgumentException(String.format("Incorrect parameters were passed! Parameter page-(%d) and sizeOfPage-(%d) cannot be negative !", ratingParamRequest.getPage(), ratingParamRequest.getSizeOfPage()));

        Pageable pageable = PageRequest.of(ratingParamRequest.getPage(), ratingParamRequest.getSizeOfPage());

        return this.reviewRepository.findAllReviews(ratingID, startDate, endDate, ratingParamRequest.getRatings(), pageable);

    }

    /**
     * Selects a set based on a rating value from rating's sets
     *
     * @param rating      rating document from whose sets will be selected
     * @param ratingValue value of evaluation
     * @return the selected set of reviews
     * @throws RatingOutOfBoundsException if the rating value does not match any sets
     * @throws IllegalArgumentException   if passed parameter is null
     */
    private Set<Review> getRatingSet(Rating rating, int ratingValue) {
        ExceptionUtility.checkInput(rating, "Parameter Rating in RatingServiceImpl.getRatingSet cannot be null !");

        return switch (ratingValue) {
            case 1 -> rating.getOneStar();
            case 2 -> rating.getTwoStar();
            case 3 -> rating.getThreeStar();
            case 4 -> rating.getFourStar();
            case 5 -> rating.getFiveStar();
            default ->
                    throw new RatingOutOfBoundsException(String.format("Rating value must be between 1-5 !, inputValue: %d", ratingValue));
        };
    }

    /**
     * Recalculates the overall rating based on all reviews for performance
     *
     * @param rating the rating for which the rating value is to be calculated
     * @return new rating value
     * @throws IllegalArgumentException if passed parameter is null
     */
    private double recountRatingValue(Rating rating) {
        ExceptionUtility.checkInput(rating, "Parameter Rating in RatingServiceImpl.recountRatingValue cannot be null !");

        double totalValueOfRatings = 5 * rating.getFiveStar().size() +
                4 * rating.getFourStar().size() +
                3 * rating.getThreeStar().size() +
                2 * rating.getTwoStar().size() +
                rating.getOneStar().size();

        double totalSumOfRatings = rating.getTotalRatings();

        double rt = totalSumOfRatings == 0 ? 0 : (totalValueOfRatings / totalSumOfRatings);

        return new BigDecimal(rt).setScale(1, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * Checks if the performance can already be evaluated based on date of rating.
     *
     * @param rating the rating which will be checked for availability
     * @return true if already available, otherwise false
     * @throws IllegalArgumentException if passed parameter is null
     */
    private boolean checkAvailability(Rating rating) {
        ExceptionUtility.checkInput(rating, "Parameter Rating in RatingServiceImpl.checkAvailability cannot be null !");

        if (rating.isAvailable()) return true;

        return Instant.now().isAfter(rating.getDate());
    }

    /**
     * It tries to convert the inserted string to zoneID and if it fails it returns UTC.
     * It passed parameter is null, it returns UTC.
     *
     * @param zoneID the string that will be used for converting
     * @return appropriate zoneID
     */
    private ZoneId getUserZoneId(String zoneID) {
        if (zoneID == null || zoneID.trim().isEmpty()) return ZoneId.from(ZoneOffset.UTC);

        try {
            return ZoneId.of(URLDecoder.decode(zoneID, StandardCharsets.UTF_8));
        } catch (ZoneRulesException e) {
            return ZoneId.from(ZoneOffset.UTC);
        }
    }
}
