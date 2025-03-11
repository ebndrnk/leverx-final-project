package org.ebndrnk.leverxfinalproject.repository.anonymous;

import org.ebndrnk.leverxfinalproject.model.entity.anonymous.AnonymousUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AnonymousUserRepository extends JpaRepository<AnonymousUser, Long> {
    Optional<AnonymousUser> findByAnonymousId(String anonymousId);
}
