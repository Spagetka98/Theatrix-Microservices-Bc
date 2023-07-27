package cz.osu.authenticationservice.controller;

import cz.osu.authenticationservice.model.response.GatewayValidationResponse;
import cz.osu.authenticationservice.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/validation")
@RequiredArgsConstructor
public class ValidationController {

    private final ValidationService validationService;

    @GetMapping("/token")
    public ResponseEntity<GatewayValidationResponse> validateRequest(){

        GatewayValidationResponse gatewayValidationResponse = this.validationService.sendServiceJWT();

        return ResponseEntity.ok(gatewayValidationResponse);
    }
}
