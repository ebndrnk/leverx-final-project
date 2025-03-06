package org.ebndrnk.leverxfinalproject.repository.comment;

import org.ebndrnk.leverxfinalproject.model.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
