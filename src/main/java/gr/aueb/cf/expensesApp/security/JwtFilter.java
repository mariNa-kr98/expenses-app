package gr.aueb.cf.expensesApp.security;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

        private static final Logger LOGGER = LoggerFactory.getLogger(JwtFilter.class);
        private final JwtService jwtService;
        private final UserDetailsService userDetailsService;

        @Override
        protected void doFilterInternal(
                HttpServletRequest request,
                HttpServletResponse response,
                FilterChain filterChain) throws ServletException, IOException {

            String authHeader = request.getHeader("Authorization");
            String jwt;
            String username;
//            String userRole;

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring(7);

            try {
                username = jwtService.extractSubject(jwt);
//                userRole = jwtService.getStringClaim(jwt, "role");
                LOGGER.info("Username extracted from jwt: {}", username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    LOGGER.info("Loaded user: {}", userDetails.getUsername());

                    if (jwtService.isTokenValid(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                        LOGGER.info("Authentication set for user: {}", username);
                    } else {
                        LOGGER.warn("Token is not valid" + request.getRequestURI());
                        LOGGER.info("Token is invalid for user: {}", username);
                    }
                }
            } catch (ExpiredJwtException e) {
                LOGGER.warn("WARN: Expired token {}", e.getMessage());
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.setContentType("application/json");
                String jsonBody = "{\"code\": \"expired_token\", \"message\": \"" + e.getMessage() + "\"}";
                response.getWriter().write(jsonBody);
                return;
            } catch (Exception e) {
                LOGGER.warn("WARN: Something went wrong while parsing JWT {}", e.getMessage());
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType("application/json");
                String jsonBody = "{\"code\": \"expired_token\", \"message\": \"" + e.getMessage() + "\"}";
                response.getWriter().write(jsonBody);
                return;
            }
            filterChain.doFilter(request, response);
        }
}
