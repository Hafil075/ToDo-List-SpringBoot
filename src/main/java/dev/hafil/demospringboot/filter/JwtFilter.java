package dev.hafil.demospringboot.filter;

import dev.hafil.demospringboot.model.User;
import dev.hafil.demospringboot.service.UserService;
import dev.hafil.demospringboot.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // Only process if Authorization header exists and no authentication is set
        if (authHeader != null && authHeader.startsWith("Bearer ") &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            String token = authHeader.substring(7);

            try {
                if (jwtUtil.validateToken(token)) {
                    String userName = jwtUtil.extractUsername(token);
                    Optional<User> userOpt = userService.findUserByUsername(userName);

                    // Only authenticate if user exists
                    if (userOpt.isPresent()) {
                        User user = userOpt.get();
                        var auth = new UsernamePasswordAuthenticationToken(user, null, List.of());
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                }
            } catch (Exception e) {
                // Log error but don't break the filter chain
                // User will remain unauthenticated
            }
        }

        filterChain.doFilter(request, response);
    }
}