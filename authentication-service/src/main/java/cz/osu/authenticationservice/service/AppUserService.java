package cz.osu.authenticationservice.service;

import cz.osu.authenticationservice.error.exception.AppUserNotFoundException;
import cz.osu.authenticationservice.model.database.AppUser;

public interface AppUserService {

    /**
     * A name of authenticated user from security context is used for searching in database by this username to find a certain user
     *
     * @return authenticated AppUser;
     * @throws AppUserNotFoundException if user was not found in database
     */
    AppUser getAuthenticatedUser();

    /**
     * Return a user from database by his name
     *
     * @param username the username that will be used for the search
     * @return authenticated AppUser;
     * @throws IllegalArgumentException if input parameter username is empty or null
     * @throws AppUserNotFoundException if user was not found in database
     */
    AppUser getUserByUsername(String username);

    /**
     * Return a user from database by his id
     *
     * @param userId the id that will be used for the search
     * @return authenticated AppUser;
     * @throws IllegalArgumentException if input parameter username is empty or null
     * @throws AppUserNotFoundException if user was not found in database
     */
    AppUser getUserById(String userId);
}
