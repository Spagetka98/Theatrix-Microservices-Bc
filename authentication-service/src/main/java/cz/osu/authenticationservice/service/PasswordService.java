package cz.osu.authenticationservice.service;

import org.springframework.security.authentication.BadCredentialsException;

public interface PasswordService {
    /**
     * It will change current user password with new one
     *
     * @param currentPassword the current password of user that is used for authentication
     * @param newPassword     the new password that will replace the old one.
     * @throws IllegalArgumentException if passed parameter is null or empty
     * @throws BadCredentialsException  if passed password (currentPassword) will be not match with user's current password
     */
    void changePassword(String currentPassword, String newPassword);
}
