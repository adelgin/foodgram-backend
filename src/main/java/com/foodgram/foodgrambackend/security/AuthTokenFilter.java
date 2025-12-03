package com.foodgram.foodgrambackend.security;

import com.foodgram.foodgrambackend.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private final UserService userDetailsService;

    private final TokenUtils tokenUtils;

    public AuthTokenFilter(UserService userDetailsService) {
        this.userDetailsService = userDetailsService;
        this.tokenUtils = new TokenUtils(); // если TokenUtils — бин, лучше внедрять через конструктор или @Autowired
    }

    // Если TokenUtils — бин, то лучше так:
    /*
    @Autowired
    public AuthTokenFilter(UserService userDetailsService, TokenUtils tokenUtils) {
        this.userDetailsService = userDetailsService;
        this.tokenUtils = tokenUtils;
    }
    */

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && tokenUtils.validateToken(jwt)) {
                String email = tokenUtils.getEmailFromToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication", e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Token ")) {
            return headerAuth.substring(6);
        }

        return null;
    }
}
