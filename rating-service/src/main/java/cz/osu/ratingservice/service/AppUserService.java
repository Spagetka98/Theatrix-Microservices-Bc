package cz.osu.ratingservice.service;

import cz.osu.ratingservice.model.pojo.UserDetails;

public interface AppUserService {
    /**
     * Get user information from security context
     *
     * @return user's information (userID, username)
     */
    UserDetails getUserDetails();
}
