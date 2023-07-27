package cz.osu.authenticationservice.controller;

import cz.osu.authenticationservice.model.request.PasswordRequest;
import cz.osu.authenticationservice.service.PasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AppUserController {
    private final PasswordService passwordService;

    @PutMapping("/changePassword")
    public ResponseEntity<?> changePassword(@Valid @RequestBody PasswordRequest passwordRequest){
        log.info("Password change request");

        this.passwordService.changePassword(passwordRequest.currentPassword(),passwordRequest.newPassword());

        log.info("Password successfully changed");

        return ResponseEntity.ok().build();
    }
}