package cz.osu.authenticationservice.model.response;

import java.util.List;

public record LoginResponse(String username,String email,List<String> roles,String accessToken,String refreshToken) {
}
