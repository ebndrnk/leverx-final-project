package org.ebndrnk.leverxfinalproject.controller.comment;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.auth.UserDto;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentRequest;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentResponse;
import org.ebndrnk.leverxfinalproject.model.dto.comment.seller.SellerFromCommentDto;
import org.ebndrnk.leverxfinalproject.model.dto.comment.seller.SellerFromCommentRequest;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileRequest;
import org.ebndrnk.leverxfinalproject.service.comment.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;


    @PostMapping
    public ResponseEntity<CommentResponse> addCommentToSeller(@RequestParam(name = "userId") Long userId,
                                                              @RequestBody CommentRequest commentRequest) {
        return ResponseEntity.ok(commentService.addComment(commentRequest, userId));
    }

    @PostMapping("/register-seller")
    public ResponseEntity<CommentResponse> addCommentToSeller(@RequestBody SellerFromCommentRequest sellerFromCommentRequest) {
        return ResponseEntity.ok(commentService.addComment(sellerFromCommentRequest));
    }



    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentById(@PathVariable(name = "commentId") Long commentId){
        commentService.deleteById(commentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getListOfSellersComments(@RequestParam(name = "userId") Long userId){
        return ResponseEntity.ok(commentService.getSellersComments(userId));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable(name = "commentId") Long commentId){
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> editComment(@PathVariable(name = "commentId") Long commentId, @RequestBody CommentRequest commentRequest){
        return ResponseEntity.ok(commentService.editComment(commentId, commentRequest));
    }

    @GetMapping("/get-unconfirmed")
    public ResponseEntity<List<CommentResponse>> getAllUnconfirmed(){
        return ResponseEntity.ok(commentService.getAllUnconfirmed());
    }

    @GetMapping("/get-confirmed")
    public ResponseEntity<List<CommentResponse>> getAllConfirmed(){
        return ResponseEntity.ok(commentService.getAllConfirmed());
    }

    @PatchMapping("/{commentId}/confirm")
    public ResponseEntity<CommentResponse> confirmComment(@PathVariable(name = "commentId") Long commentId){
        return ResponseEntity.ok(commentService.confirm(commentId));
    }

    @PatchMapping("/{commentId}/decline")
    public ResponseEntity<CommentResponse> declineComment(@PathVariable(name = "commentId") Long commentId){
        return ResponseEntity.ok(commentService.decline(commentId));
    }

}
