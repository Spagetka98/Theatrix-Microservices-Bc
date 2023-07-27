package cz.osu.authenticationservice.utility;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class JwtUtility {
    /**
     * Generate a JWT token
     *
     * @param subject    the information to be set in JWT
     * @param key        the key for encryption
     * @param expiration date when the token expires
     * @return encrypted JWT token
     * @throws IllegalArgumentException if passed parameters are null or empty
     */
    public static String generateJwtToken(String subject, String key, Date expiration) {
        ExceptionUtility.checkInput(subject, "Parameter Subject in JwtUtility.generateJwtToken cannot be null or empty !");
        ExceptionUtility.checkInput(key, "Parameter Key in JwtUtility.generateJwtToken cannot be null or empty !");
        ExceptionUtility.checkInput(expiration, "Parameter Expiration in JwtUtility.generateJwtToken cannot be null !");

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    /**
     * Get subject from JWT
     *
     * @param token the token that contains information
     * @param key   the key for decryption
     * @return subject from JWT
     * @throws IllegalArgumentException if passed parameters are null or empty
     */
    public static String getUsernameFromJwtToken(String token, String key) {
        ExceptionUtility.checkInput(token, "Parameter Token in JwtUtility.generateJwtToken cannot be null or empty !");
        ExceptionUtility.checkInput(key, "Parameter Key in JwtUtility.generateJwtToken cannot be null or empty !");

        return Jwts.parser().setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Attempts to decrypt the token using the key
     *
     * @param authToken the token to be decrypted
     * @param key       the key that is used for decryption
     * @return true if token was decrypted, otherwise false
     * @throws IllegalArgumentException if passed key is null or empty
     */
    public static boolean validateJwtToken(String authToken, String key) {
        ExceptionUtility.checkInput(key, "Parameter Key in JwtUtility.validateJwtToken cannot be null or empty !");

        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            log.error(String.format("Invalid JWT signature: %s", e.getMessage()));
        } catch (MalformedJwtException e) {
            log.error(String.format("Invalid JWT token: %s", e.getMessage()));
        } catch (ExpiredJwtException e) {
            log.error(String.format("JWT token is expired: %s", e.getMessage()));
        } catch (UnsupportedJwtException e) {
            log.error(String.format("JWT token is unsupported: %s", e.getMessage()));
        } catch (IllegalArgumentException e) {
            log.error(String.format("JWT claims string is empty: %s", e.getMessage()));
        }

        return false;
    }
}
