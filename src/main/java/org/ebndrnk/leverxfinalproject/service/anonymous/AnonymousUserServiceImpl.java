package org.ebndrnk.leverxfinalproject.service.anonymous;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebndrnk.leverxfinalproject.model.entity.anonymous.AnonymousUser;
import org.ebndrnk.leverxfinalproject.repository.anonymous.AnonymousUserRepository;
import org.ebndrnk.leverxfinalproject.exception.dto.CommentAuthorNotFoundException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Service for handling anonymous users.
 * Manages the creation and retrieval of anonymous users based on cookies.
 */
@Service
@Slf4j
@Primary
@RequiredArgsConstructor
public class AnonymousUserServiceImpl implements AnonymousUserService {

    private final AnonymousUserRepository anonymousUserRepository;
    private static final String ANONYMOUS_COOKIE_NAME = "anonymousId";

    /**
     * Retrieves the anonymous user ID from the cookies.
     *
     * @param request HTTP request
     * @return anonymous user ID, or null if not found
     */
    @Override
    public String getAnonymousIdFromCookies(HttpServletRequest request) {
        log.debug("Searching for cookie with name: {}", ANONYMOUS_COOKIE_NAME);
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (ANONYMOUS_COOKIE_NAME.equals(cookie.getName())) {
                    log.debug("Found cookie with ID: {}", cookie.getValue());
                    return cookie.getValue();
                }
            }
        }
        log.warn("Cookie with name {} not found", ANONYMOUS_COOKIE_NAME);
        return null;
    }

    /**
     * Retrieves or creates an anonymous user based on cookies.
     *
     * @param request HTTP request
     * @return the anonymous user object
     * @throws CommentAuthorNotFoundException if the anonymous user is not found in cookies
     */
    @Override
    public AnonymousUser getOrCreateAnonymousUser(HttpServletRequest request) {
        String anonymousId = getAnonymousIdFromCookies(request);
        if (anonymousId == null) {
            log.error("Anonymous user not found in cookies");
            throw new CommentAuthorNotFoundException("anonymous user not found in cookies");
        }

        log.info("Searching for anonymous user with ID: {}", anonymousId);
        return anonymousUserRepository.findByAnonymousId(anonymousId)
                .orElseGet(() -> {
                    log.info("Anonymous user not found, creating new user with ID: {}", anonymousId);
                    return createNewAnonymousUser(anonymousId);
                });
    }

    /**
     * Creates a new anonymous user with the specified ID.
     *
     * @param anonymousId the anonymous user ID
     * @return the created anonymous user object
     */
    @Override
    public AnonymousUser createNewAnonymousUser(String anonymousId) {
        log.info("Creating new anonymous user with ID: {}", anonymousId);
        AnonymousUser anonymousUser = new AnonymousUser();
        anonymousUser.setAnonymousId(anonymousId);
        return anonymousUserRepository.save(anonymousUser);
    }
}
