package com.bank.banksystem;

import java.io.IOException;
import java.util.List;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();

        // skill auth for login, actuator and error paths
        if (path.startsWith("/auth/login") || path.startsWith("/actuator") || path.startsWith("/error")) {
            System.out.println("[JwtAuthFilter] Skipping JWT filter for: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("[JwtAuthFilter] Checking JWT for: " + path);

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("[JwtAuthFilter] Missing or invalid Authorization header");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid Authorization header");
            return;
        }

        try {
            String token = authHeader.substring(7);
            System.out.println("[JwtAuthFilter] Token received: " + token.substring(0, Math.min(20, token.length())) + "...");
            
            Claims claims = JwtUtil.parseToken(token);

            // username
            String username = claims.getSubject();
            System.out.println("[JwtAuthFilter] Token validated for user: " + username);

            // add role user
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            System.out.println("[JwtAuthFilter] Authentication set in SecurityContext");

        } catch (JwtException e) {
            System.out.println("[JwtAuthFilter] JWT validation failed: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token: " + e.getMessage());
            return;
        } catch (Exception e) {
            System.out.println("[JwtAuthFilter] Unexpected error: " + e.getMessage());
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication error");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
