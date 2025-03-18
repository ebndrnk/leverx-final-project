package org.ebndrnk.leverxfinalproject.repository.comment;

import org.ebndrnk.leverxfinalproject.model.entity.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllBySeller_Id(Long id);

    @Modifying
    @Query(value = "SELECT c from Comment c where c.approved = false")
    List<Comment> findAllUnconfirmed();

    @Modifying
    @Query(value = "SELECT c from Comment c where c.approved = true")
    List<Comment> findAllConfirmed();
}
