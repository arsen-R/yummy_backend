package com.arsenr.yummy.config;

import com.arsenr.yummy.jwt.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        StringBuilder jwtToken = new StringBuilder();
        StringBuilder username = new StringBuilder();

        if (authHeader != null && authHeader.startsWith("Bearer")) {
            jwtToken.append(authHeader.substring(7));
            try {
                username.append(jwtService.extractUsername(jwtToken.toString()));
            } catch (NullPointerException e) {
                log.error("Null pointer exception: ", e);
            } catch (IllegalArgumentException e) {
                log.error("Illegal Argument while fetching the username!!");
            } catch (ExpiredJwtException e) {
                log.error("Given jwt token is expired!!");
            } catch (MalformedJwtException e) {
                log.error("Some changed has done in token!! Invalid Token");
            } catch (Exception e) {
                log.error("An exception occurred while fetching the username !!");
            }
        } else {
            log.error("Invalid Header Value!");
        }

        if (!username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username.toString());
            if (jwtService.validateToken(jwtToken.toString(), userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
