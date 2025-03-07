package org.ebndrnk.leverxfinalproject.repository.auth;

import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByEmail (String email);

    @Modifying
    @Query("SELECT u from User u where u.isConfirmedByAdmin = false ")
    List<User> findAllNotConfirmedByAdminUsers();
 }
