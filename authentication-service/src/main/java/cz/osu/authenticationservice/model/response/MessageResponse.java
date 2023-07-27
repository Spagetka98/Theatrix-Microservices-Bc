package cz.osu.authenticationservice.model.response;

import org.springframework.http.HttpStatus;


public record MessageResponse(HttpStatus status,String message,String timestamp) {
}
