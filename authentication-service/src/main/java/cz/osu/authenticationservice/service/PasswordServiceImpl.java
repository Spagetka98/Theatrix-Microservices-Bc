package cz.osu.authenticationservice.service;

import cz.osu.authenticationservice.model.enums.EStatusErrors;
import cz.osu.authenticationservice.model.database.AppUser;
import cz.osu.authenticationservice.repository.AppUserRepository;
import cz.osu.authenticationservice.utility.ExceptionUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordServiceImpl implements PasswordService{
    private final AppUserRepository appUserRepository;
    private final AppUserService appUserServiceImpl;
    private final ValidationService validationServiceImpl;
    private final PasswordEncoder encoder;

    /**
     * @see PasswordService#changePassword(String, String)
     */
    @Override
    public void changePassword(String currentPassword, String newPassword) {
        ExceptionUtility.checkInput(currentPassword,"Parameter currentPassword in AppUserServiceImpl.changePassword cannot be null or empty !");
        ExceptionUtility.checkInput(newPassword,"Parameter newPassword in AppUserServiceImpl.changePassword cannot be null or empty !");

        this.validationServiceImpl.passwordCheck(newPassword);

        AppUser user = appUserServiceImpl.getAuthenticatedUser();

        if(encoder.matches(currentPassword,user.getPassword())){
            String newPass = encoder.encode(newPassword);

            user.setPassword(newPass);

            this.appUserRepository.save(user);
        }else {
            throw new BadCredentialsException(EStatusErrors.INCORRECT_PASSWORD.getValue());
        }
    }
}
