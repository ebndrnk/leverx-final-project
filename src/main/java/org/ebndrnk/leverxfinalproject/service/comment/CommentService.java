package org.ebndrnk.leverxfinalproject.service.comment;

import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentRequest;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse addComment(CommentRequest commentRequest, Long sellerId);
    void deleteById(Long commentId);

    List<CommentResponse> getSellersComments(Long userId);

    CommentResponse getCommentById(Long commentId);

    CommentResponse editComment(Long commentId, CommentRequest commentRequest);

    List<CommentResponse> getAllUnconfirmed();

    List<CommentResponse> getAllConfirmed();

    CommentResponse confirm(Long commentId);

    CommentResponse decline(Long commentId);
}
