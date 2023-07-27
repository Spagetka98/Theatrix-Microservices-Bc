package cz.osu.activityservice.service;

import cz.osu.activityservice.model.pojo.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService {
    /**
     * @see AppUserService#getUser()
     */
    @Override
    public UserDetails getUser() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
