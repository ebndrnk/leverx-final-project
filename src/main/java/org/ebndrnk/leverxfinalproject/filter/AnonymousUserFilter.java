package org.ebndrnk.leverxfinalproject.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class AnonymousUserFilter extends OncePerRequestFilter {

    private static final String ANONYMOUS_COOKIE_NAME = "anonymousId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        boolean hasAnonymousId = false;
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (ANONYMOUS_COOKIE_NAME.equals(cookie.getName())) {
                    hasAnonymousId = true;
                    break;
                }
            }
        }

        if (!hasAnonymousId) {
            String anonymousId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(ANONYMOUS_COOKIE_NAME, anonymousId);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // Поставь true, если используешь HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 30); // 30 дней

            response.addCookie(cookie);
        }

        filterChain.doFilter(request, response);
    }
}
