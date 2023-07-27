package cz.osu.authenticationservice.controller;

import cz.osu.authenticationservice.model.request.RefreshTokenRequest;
import cz.osu.authenticationservice.model.response.RefreshTokenResponse;
import cz.osu.authenticationservice.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/expiration")
@RequiredArgsConstructor
public class RefreshController {
    private final TokenService tokenService;

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info(String.format("Validating refresh token: %s", request.refreshToken()));

        String token = tokenService.generateAccessTokenForRefreshToken(request.refreshToken());

        log.info("Sending new access token");

        return ResponseEntity.ok(new RefreshTokenResponse(token));
    }
}