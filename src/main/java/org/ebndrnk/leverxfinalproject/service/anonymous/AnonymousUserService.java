package org.ebndrnk.leverxfinalproject.service.anonymous;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebndrnk.leverxfinalproject.model.entity.anonymous.AnonymousUser;
import org.ebndrnk.leverxfinalproject.repository.anonymous.AnonymousUserRepository;
import org.ebndrnk.leverxfinalproject.util.exception.dto.CommentAuthorNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnonymousUserService {

    private final AnonymousUserRepository anonymousUserRepository;
    private static final String ANONYMOUS_COOKIE_NAME = "anonymousId";

    private String getAnonymousIdFromCookies(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (ANONYMOUS_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public AnonymousUser getOrCreateAnonymousUser(HttpServletRequest request) {
        String anonymousId = getAnonymousIdFromCookies(request);
        if (anonymousId == null) {
            throw new CommentAuthorNotFoundException("Не найден anonymousId в куках");
        }

        return anonymousUserRepository.findByAnonymousId(anonymousId)
                .orElseGet(() -> createNewAnonymousUser(anonymousId));
    }

    private AnonymousUser createNewAnonymousUser(String anonymousId) {
        AnonymousUser anonymousUser = new AnonymousUser();
        anonymousUser.setAnonymousId(anonymousId);
        return anonymousUserRepository.save(anonymousUser);
    }
}
