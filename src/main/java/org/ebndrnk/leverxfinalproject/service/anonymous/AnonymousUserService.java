package org.ebndrnk.leverxfinalproject.service.anonymous;

import jakarta.servlet.http.HttpServletRequest;
import org.ebndrnk.leverxfinalproject.model.entity.anonymous.AnonymousUser;

public interface AnonymousUserService {
    String getAnonymousIdFromCookies(HttpServletRequest request);

    AnonymousUser getOrCreateAnonymousUser(HttpServletRequest request);

    AnonymousUser createNewAnonymousUser(String anonymousId);
}
