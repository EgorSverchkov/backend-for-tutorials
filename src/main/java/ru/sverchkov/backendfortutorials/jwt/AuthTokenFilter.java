package ru.sverchkov.backendfortutorials.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.sverchkov.backendfortutorials.exception.TokenRefreshException;
import ru.sverchkov.backendfortutorials.service.impl.UserDetailsServiceImpl;

import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String HEADER_PREFIX = "Bearer ";
    private static final int BEGIN_INDEX = 7;

    private final UserDetailsServiceImpl userDetailsService;

    private final JwtUtils jwtUtils;

    public AuthTokenFilter(UserDetailsServiceImpl userDetailsService, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = null;
        String authToken = parseJwt(request);

        if (authToken != null) {
            try {
                username = jwtUtils.getUserNameFromJwtToken(authToken);
            } catch (IllegalArgumentException | ExpiredJwtException e) {
                throw new TokenRefreshException(authToken, e.getMessage());
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtils.validateJwtToken(authToken, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(AUTHORIZATION_HEADER);

        return (StringUtils.hasText(headerAuth) && headerAuth.startsWith(HEADER_PREFIX)) ?
                headerAuth.substring(BEGIN_INDEX) : null;
    }
}
