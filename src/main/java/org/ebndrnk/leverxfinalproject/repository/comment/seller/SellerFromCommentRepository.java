package org.ebndrnk.leverxfinalproject.repository.comment.seller;

import org.ebndrnk.leverxfinalproject.model.entity.comment.seller.SellerFromComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerFromCommentRepository extends JpaRepository<SellerFromComment, Long> {
    Optional<SellerFromComment> findByEmail(String email);
    Optional<SellerFromComment> findByUsername(String username);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
