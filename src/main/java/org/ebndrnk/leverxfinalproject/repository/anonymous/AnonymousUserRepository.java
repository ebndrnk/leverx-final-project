package org.ebndrnk.leverxfinalproject.repository.anonymous;

import org.ebndrnk.leverxfinalproject.model.entity.anonymous.AnonymousUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnonymousUserRepository extends JpaRepository<AnonymousUser, Long> {
    Optional<AnonymousUser> findByAnonymousId(String anonymousId);
}
