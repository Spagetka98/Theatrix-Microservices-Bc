package cz.osu.authenticationservice.service;

import cz.osu.authenticationservice.model.database.AppUser;
import cz.osu.authenticationservice.model.database.embedded.AccessToken;
import cz.osu.authenticationservice.utility.ExceptionUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilterServiceImpl implements FilterService{
    private final AppUserService appUserServiceImpl;
    private final TokenService tokenServiceImpl;

    /**
     * @see FilterService#validateUserJWT(String)
     */
    public boolean validateUserJWT(String jwt){
        if(jwt != null)
            return tokenServiceImpl.validateUserJWT(jwt);

        return false;
    }

    /**
     * @see FilterService#getNameFromJWT(String) 
     */
    public String getNameFromJWT(String jwt){
        ExceptionUtility.checkInput(jwt,"Parameter JWT in FilterServiceImpl.getNameFromJWT cannot be null or empty!");

        return tokenServiceImpl.getUsernameFromJWT(jwt);
    }

    /**
     * @see FilterService#checkUsernameAndUserJWTAffiliation(String, String)
     */
    public boolean checkUsernameAndUserJWTAffiliation(String username, String jwt){
        ExceptionUtility.checkInput(username,"Parameter Username in FilterServiceImpl.checkUsernameAndJWTAffiliation cannot be null or empty!");
        ExceptionUtility.checkInput(jwt,"Parameter JWT in FilterServiceImpl.checkUsernameAndJWTAffiliation cannot be null or empty!");

        AppUser user = appUserServiceImpl.getUserByUsername(username);

        AccessToken accessToken = user.getAccessToken();

        return accessToken != null && accessToken.getTokenValue().equals(jwt);
    }
}
