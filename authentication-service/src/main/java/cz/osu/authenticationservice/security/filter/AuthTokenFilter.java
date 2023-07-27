package cz.osu.authenticationservice.security.filter;

import cz.osu.authenticationservice.model.enums.EHeaders;
import cz.osu.authenticationservice.properties.TheatrixProperties;
import cz.osu.authenticationservice.service.FilterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {
    private final FilterService filterServiceImpl;
    private final UserDetailsService userDetailsService;
    private final TheatrixProperties theatrixProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!theatrixProperties.excludedUrls().contains(request.getRequestURI()))
            this.authenticateRequest(request);

        filterChain.doFilter(request, response);
    }

    private void authenticateRequest(HttpServletRequest request) {
        try {
            String jwt = getJwtTokenFromRequest(request);

            if (filterServiceImpl.validateUserJWT(jwt)) {
                String username = filterServiceImpl.getNameFromJWT(jwt);
                if (!filterServiceImpl.checkUsernameAndUserJWTAffiliation(username, jwt)) return;

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error(String.format("Error due to user authentication: %s", e.getMessage()));
        }
    }

    private String getJwtTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader(EHeaders.AUTHORIZATION.getValue());
        String headerPrepositions = String.format("%s ", theatrixProperties.jwtPreposition());

        if (StringUtils.hasText(header) && header.startsWith(headerPrepositions)) {
            return header.substring(headerPrepositions.length());
        }

        return null;
    }
}
