package cz.osu.activityservice.service;

import cz.osu.activityservice.model.pojo.UserDetails;

public interface AppUserService {
    /**
     * Get user information from security context
     *
     * @return user's information (userID, username)
     */
    UserDetails getUser();
}
