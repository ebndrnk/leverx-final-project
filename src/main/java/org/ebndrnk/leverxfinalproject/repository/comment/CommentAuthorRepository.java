package org.ebndrnk.leverxfinalproject.repository.comment;

import org.ebndrnk.leverxfinalproject.model.entity.comment.CommentAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentAuthorRepository extends JpaRepository<CommentAuthor, Long> {
    Optional<CommentAuthor> findByIdentifier(String identifier);
}
