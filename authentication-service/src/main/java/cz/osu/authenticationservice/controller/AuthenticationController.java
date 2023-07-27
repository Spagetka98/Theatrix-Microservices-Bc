package cz.osu.authenticationservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import cz.osu.authenticationservice.model.request.LoginRequest;
import cz.osu.authenticationservice.model.request.RegistrationRequest;
import cz.osu.authenticationservice.model.response.LoginResponse;
import cz.osu.authenticationservice.service.AuthenticationService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        log.info(String.format("Logging user with username: %s", loginRequest.username()));

        LoginResponse response = authenticationService.authUser(loginRequest.username(), loginRequest.password());

        log.info("User was logged successfully !");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequest registrationRequest) {
        log.info(String.format("Registration user with username: %s , email: %s", registrationRequest.username(), registrationRequest.email()));

        authenticationService.registerUser(registrationRequest.username(), registrationRequest.email(), registrationRequest.password());

        log.info("User was registered !");

        return ResponseEntity.ok().build();
    }

    @GetMapping("/signout")
    public ResponseEntity<?> logout() {
        log.info("User is trying to logout");

        authenticationService.logout();

        log.info("User was logout !");

        return ResponseEntity.ok().build();
    }
}
