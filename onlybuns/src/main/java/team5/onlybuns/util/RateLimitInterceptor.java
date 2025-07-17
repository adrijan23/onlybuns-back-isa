package team5.onlybuns.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import team5.onlybuns.security.auth.TokenBasedAuthentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    @Autowired
    private RateLimiter rateLimiter;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object auth = SecurityContextHolder.getContext().getAuthentication();

        if (!(auth instanceof TokenBasedAuthentication)) {
            // nije autentifikovani korisnik
            return true;
        }

        UserDetails userDetails = ((TokenBasedAuthentication) auth).getPrincipal();
        String username = userDetails.getUsername();

        if (!rateLimiter.isAllowed(username)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Previše zahteva. Pokušajte ponovo kasnije.");
            return false;
        }

        return true;
    }
}

