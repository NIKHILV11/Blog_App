package com.blog.blog.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import com.blog.blog.domain.entities.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.blog.blog.services.AuthenticationService;


@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

   private final AuthenticationService authenticationService;
   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
           throws ServletException, IOException {
            try{
            String token = extractToken(request);
            if(token != null)
            {
                UserDetails userDetails = authenticationService.validateToken(token);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                if(userDetails instanceof BlogUserDetails) {
                    User user = ((BlogUserDetails) userDetails).getUser();
                    request.setAttribute("userId", user.getId());
                }
            }}
            catch (Exception e) {
                // Handle token validation failure
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
                return;
            }
           
                filterChain.doFilter(request, response); // Continue the filter chain
            
           }
    private String extractToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

   
}
