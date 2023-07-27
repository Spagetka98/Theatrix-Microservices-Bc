package cz.osu.authenticationservice.model.response;

import java.util.Set;

public record GatewayValidationResponse(String username,String userID,Set<String> roles) {
}
