package cz.osu.ratingservice.model.pojo;

import cz.osu.ratingservice.utility.ExceptionUtility;

public record UserDetails(String userId,
                          String username) {
    public UserDetails {
        ExceptionUtility.checkInput(userId, "Parameter userId in record UserDetails cannot be null or empty !");
        ExceptionUtility.checkInput(username, "Parameter username in record UserDetails cannot be null or empty !");
    }

}
