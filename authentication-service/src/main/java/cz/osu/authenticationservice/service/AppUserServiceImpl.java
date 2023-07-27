package cz.osu.authenticationservice.service;

import cz.osu.authenticationservice.model.database.AppUser;
import cz.osu.authenticationservice.repository.AppUserRepository;
import cz.osu.authenticationservice.utility.ExceptionUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import cz.osu.authenticationservice.error.exception.AppUserNotFoundException;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;

    /**
     * @see AppUserService#getAuthenticatedUser()
     */
    @Override
    public AppUser getAuthenticatedUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        return this.appUserRepository.findByUsername(username)
                .orElseThrow(() -> new AppUserNotFoundException(String.format("User with username: %s was not found in AppUserServiceImpl.getCurrentUser !", username)));
    }

    /**
     * @see AppUserService#getUserByUsername(String)
     */
    @Override
    public AppUser getUserByUsername(String username) {
        ExceptionUtility.checkInput(username,"Parameter username in AppUserServiceImpl.getUserByUsername cannot be null or empty !");

        return this.appUserRepository.findByUsername(username)
                .orElseThrow(() -> new AppUserNotFoundException(String.format("User with username: %s was not found in AppUserServiceImpl.getUserByUsername !", username)));
    }

    /**
     * @see AppUserService#getUserById(String)
     */
    @Override
    public AppUser getUserById(String userId) {
        ExceptionUtility.checkInput(userId,"userId username in AppUserServiceImpl.getUserById cannot be null or empty !");

        return this.appUserRepository.findById(userId)
                .orElseThrow(() -> new AppUserNotFoundException(String.format("User with id: %s was not found in AppUserServiceImpl.getUserById !", userId)));
    }

}
