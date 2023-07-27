package cz.osu.authenticationservice.service;

import cz.osu.authenticationservice.model.enums.ERole;
import cz.osu.authenticationservice.model.enums.EStatusErrors;
import cz.osu.authenticationservice.error.exception.RoleNotFoundException;
import cz.osu.authenticationservice.error.exception.UserInformationTakenException;
import cz.osu.authenticationservice.model.database.AppUser;
import cz.osu.authenticationservice.model.database.Role;
import cz.osu.authenticationservice.model.database.embedded.AccessToken;
import cz.osu.authenticationservice.model.database.embedded.RefreshToken;
import cz.osu.authenticationservice.repository.AppUserRepository;
import cz.osu.authenticationservice.repository.RoleRepository;
import cz.osu.authenticationservice.model.response.LoginResponse;
import cz.osu.authenticationservice.security.userDetails.UserDetailsImpl;
import cz.osu.authenticationservice.utility.ExceptionUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService{
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenServiceImpl;
    private final AppUserService appUserServiceImpl;
    private final ValidationService validationServiceImpl;
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

    /**
     * @see AuthenticationService#authUser(String, String)
     */
    @Override
    public LoginResponse authUser(String username, String password) {
        ExceptionUtility.checkCredentials(username,"Parameter username in AuthenticationServiceImpl.authUser cannot be empty or null!");
        ExceptionUtility.checkCredentials(password,"Parameter password in AuthenticationServiceImpl.authUser cannot be empty or null!");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        RefreshToken refreshToken = tokenServiceImpl.createRefreshTokenForUser(userDetails.getId());
        AccessToken accessToken = tokenServiceImpl.createAccessTokenForUser(userDetails.getId());

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new LoginResponse(
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                accessToken.getTokenValue(),
                refreshToken.getUuidTokenValue());
    }

    /**
     * @see AuthenticationService#registerUser(String, String, String)
     */
    @Override
    public void registerUser(String username, String email, String password) {
        ExceptionUtility.checkInput(username,"Parameter username in AuthenticationServiceImpl.registerUser cannot be null or empty!");
        ExceptionUtility.checkInput(email,"Parameter email in AuthenticationServiceImpl.registerUser cannot be null or empty!");
        ExceptionUtility.checkInput(password,"Parameter password in AuthenticationServiceImpl.registerUser cannot be null or empty!");

        this.validationServiceImpl.usernameCheck(username);
        this.validationServiceImpl.emailCheck(email);
        this.validationServiceImpl.passwordCheck(password);

        if (appUserRepository.existsByUsername(username))
            throw new UserInformationTakenException(EStatusErrors.NAME_TAKEN.getValue());

        if (appUserRepository.existsByEmail(email))
            throw new UserInformationTakenException(EStatusErrors.EMAIL_TAKEN.getValue());

        AppUser user = new AppUser(
                username,
                email,
                encoder.encode(password)
        );

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RoleNotFoundException(String.format("Role %s was not found in AuthenticationServiceImpl.registerUser",ERole.ROLE_USER)));

        user.getRoles().add(userRole);

        this.appUserRepository.save(user);
    }

    /**
     * @see AuthenticationService#logout()
     */
    @Override
    public void logout() {
        AppUser user = this.appUserServiceImpl.getAuthenticatedUser();

        user.setAccessToken(new AccessToken());
        user.setRefreshToken(new RefreshToken());

        this.appUserRepository.save(user);
    }
}
