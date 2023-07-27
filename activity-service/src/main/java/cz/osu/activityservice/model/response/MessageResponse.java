package cz.osu.activityservice.model.response;

import org.springframework.http.HttpStatus;

public record MessageResponse(HttpStatus status,
                              String message,
                              String timestamp) {
}