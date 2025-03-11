package org.ebndrnk.leverxfinalproject.controller.comment;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentRequest;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentResponse;
import org.ebndrnk.leverxfinalproject.model.dto.comment.seller.SellerFromCommentRequest;
import org.ebndrnk.leverxfinalproject.service.comment.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    /**
     * Добавление комментария к существующему продавцу.
     */
    @PostMapping
    public ResponseEntity<CommentResponse> addCommentToSeller(
            @RequestParam(name = "userId") Long userId,
            @RequestBody CommentRequest commentRequest,
            HttpServletRequest request) {

        return ResponseEntity.ok(commentService.addComment(commentRequest, userId, request));
    }

    /**
     * Добавление комментария и регистрация нового продавца (если его нет).
     */
    @PostMapping("/register-seller")
    public ResponseEntity<CommentResponse> addCommentToSeller(
            @RequestBody SellerFromCommentRequest sellerFromCommentRequest,
            HttpServletRequest request) {

        return ResponseEntity.ok(commentService.addComment(sellerFromCommentRequest, request));
    }

    /**
     * Удаление комментария (доступно автору или продавцу).
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentById(
            @PathVariable(name = "commentId") Long commentId,
            HttpServletRequest request) {

        commentService.deleteById(commentId, request);
        return ResponseEntity.noContent().build();
    }

    /**
     * Получение списка комментариев продавца.
     */
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<CommentResponse>> getListOfSellersComments(@PathVariable Long sellerId) {
        return ResponseEntity.ok(commentService.getSellersComments(sellerId));
    }

    /**
     * Получение комментария по ID.
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable(name = "commentId") Long commentId) {
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }

    /**
     * Редактирование комментария (доступно только его автору).
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> editComment(
            @PathVariable(name = "commentId") Long commentId,
            @RequestBody CommentRequest commentRequest,
            HttpServletRequest request) {

        return ResponseEntity.ok(commentService.editComment(commentId, commentRequest, request));
    }

    /**
     * Получение всех подтверждённых комментариев.
     */
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments() {
        return ResponseEntity.ok(commentService.getAllConfirmed());
    }
}
