package travelbot.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import travelbot.demo.entities.User;
import travelbot.demo.exceptions.UnauthorizedException;
import travelbot.demo.services.UserService;
import travelbot.demo.tools.JWTTools;

import java.io.IOException;

@Component

public class JWTCheckerFilter extends OncePerRequestFilter {


    @Autowired
    private JWTTools jwtTools;
    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnauthorizedException("Please enter the token in the Authorization Header in the correct format!");

        String accessToken = authHeader.replace("Bearer ", "");

        jwtTools.verifyToken(accessToken);

        String userId = jwtTools.extractIdFromToken(accessToken);
        User currentUser = this.userService.findById(Long.parseLong(userId));

        Authentication authentication = new UsernamePasswordAuthenticationToken
                (currentUser, null, currentUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);

    }

    // Disabilitiamo questo filtro per determinati endpoints tipo /auth/login e /auth/register
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());

    }
}
