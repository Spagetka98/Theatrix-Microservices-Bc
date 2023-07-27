package cz.osu.cloudgateway.model.response;

import java.util.Set;
public record GatewayValidationResponse(String username,String userID,Set<String> roles) {
}
