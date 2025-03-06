package org.ebndrnk.leverxfinalproject.service.comment;

import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentDto;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentRequest;

public interface CommentService {
    CommentDto addComment(CommentRequest commentRequest, Long sellerId);
}
