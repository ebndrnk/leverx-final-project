package org.ebndrnk.leverxfinalproject.service.comment;

import jakarta.servlet.http.HttpServletRequest;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentRequest;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentResponse;
import org.ebndrnk.leverxfinalproject.model.dto.comment.seller.SellerFromCommentDto;
import org.ebndrnk.leverxfinalproject.model.dto.comment.seller.SellerFromCommentRequest;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileRequest;

import java.util.List;

public interface CommentService {


    CommentResponse addComment(CommentRequest commentRequest, Long sellerId, HttpServletRequest request);

    CommentResponse addComment(SellerFromCommentRequest sellerFromCommentRequest, HttpServletRequest request);

    void deleteById(Long commentId, HttpServletRequest request);

    void deleteById(Long commentId);

    List<CommentResponse> getSellersComments(Long userId);

    CommentResponse getCommentById(Long commentId);

    CommentResponse editComment(Long commentId, CommentRequest commentRequest, HttpServletRequest request);

    List<CommentResponse> getAllUnconfirmed();

    List<CommentResponse> getAllConfirmed();

    CommentResponse confirm(Long commentId);

    CommentResponse decline(Long commentId);
}
