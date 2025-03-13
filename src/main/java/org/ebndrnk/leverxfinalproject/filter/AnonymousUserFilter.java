package org.ebndrnk.leverxfinalproject.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class AnonymousUserFilter extends OncePerRequestFilter {

    private static final String ANONYMOUS_COOKIE_NAME = "anonymousId";

    @Value("${cookie.secure}")
    private boolean isSecure;

    /**
     * The method that handles filtering incoming requests. It checks if the user has an anonymous ID cookie,
     * and if not, generates a new anonymous ID and sends it in the response.
     * <p>
     * If the request does not contain an anonymous ID, a new one is created and added to the response as a cookie.
     * </p>
     *
     * @param request The HTTP request.
     * @param response The HTTP response.
     * @param filterChain The filter chain to pass the request along.
     * @throws ServletException if an error occurs during filtering.
     * @throws IOException if an I/O error occurs.
     */
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

        // If the user doesn't have an anonymous ID cookie, create a new one.
        if (!hasAnonymousId) {
            String anonymousId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(ANONYMOUS_COOKIE_NAME, anonymousId);
            cookie.setHttpOnly(true);
            cookie.setSecure(isSecure);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 30);

            response.addCookie(cookie);
            log.info("Assigned new anonymous ID: {}", anonymousId);
        }

        filterChain.doFilter(request, response);
    }
}
