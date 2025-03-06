package org.ebndrnk.leverxfinalproject.controller.user;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentDto;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentRequest;
import org.ebndrnk.leverxfinalproject.service.comment.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final CommentService commentService;

    @PostMapping("/{userId}/comments")
    public ResponseEntity<CommentDto> addCommentToUser(@PathVariable Long userId,
                                                       @RequestBody CommentRequest commentRequest) {
        return ResponseEntity.ok(commentService.addComment(commentRequest, userId));
    }
}