package cz.osu.ratingservice.security.filter;

import cz.osu.ratingservice.model.enums.EHeaders;
import cz.osu.ratingservice.model.pojo.UserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        authenticateRequest(request);
        filterChain.doFilter(request, response);
    }

    private void authenticateRequest(HttpServletRequest request) {
        try {
            String headerUsername = request.getHeader(EHeaders.USERNAME.getValue());
            String headerUserId = request.getHeader(EHeaders.USER_ID.getValue());
            String headerRoles = request.getHeader(EHeaders.ROLES.getValue());

            if (headerUserId == null || headerRoles == null || headerUsername == null)
                throw new BadCredentialsException(String.format("Missing headers! Current headers are %s -> %s, %s -> %s, %s -> %s",
                        EHeaders.USERNAME.getValue(), headerUsername,
                        EHeaders.USER_ID.getValue(), headerUserId,
                        EHeaders.ROLES.getValue(), headerRoles
                ));

            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = Arrays.stream(headerRoles.replace("[", "")
                            .replace("]", "").split(",")).distinct()
                    .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(new UserDetails(headerUserId, headerUsername), null,
                    simpleGrantedAuthorities);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            log.error(String.format("Error due to user authentication: %s", e.getMessage()));
        }
    }
}
