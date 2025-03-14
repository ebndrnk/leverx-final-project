package org.ebndrnk.leverxfinalproject.repository.auth;

import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByEmail (String email);


    @Query("SELECT" +
            " CASE " +
                "WHEN COUNT(u) > 0 THEN true ELSE false" +
            " END" +
            " FROM User u WHERE u.username = :username AND u.isEmailConfirmed = true")
    boolean isEmailConfirmed(@Param("username") String username);


}
