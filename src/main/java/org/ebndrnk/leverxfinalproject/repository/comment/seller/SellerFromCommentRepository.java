package org.ebndrnk.leverxfinalproject.repository.comment.seller;

import org.ebndrnk.leverxfinalproject.model.entity.comment.seller.SellerFromComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerFromCommentRepository extends JpaRepository<SellerFromComment, Long> {
    Optional<SellerFromComment> findByEmail(String email);
    Optional<SellerFromComment> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
