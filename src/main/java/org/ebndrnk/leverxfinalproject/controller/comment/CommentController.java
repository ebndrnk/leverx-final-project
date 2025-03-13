package org.ebndrnk.leverxfinalproject.controller.comment;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentRequest;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentResponse;
import org.ebndrnk.leverxfinalproject.model.dto.comment.seller.SellerFromCommentRequest;
import org.ebndrnk.leverxfinalproject.service.comment.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Comment Controller
 *
 * <p>
 * This controller handles all operations related to comments, such as adding, editing, deleting, and retrieving comments.
 * It also provides endpoints for managing comments related to sellers, including the option to register a seller if they do not exist.
 * </p>
 *
 * <p>
 * All comment logic is delegated to {@link CommentService}.
 * </p>
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
@Tag(name = "Comment Controller", description = "API for managing comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * Add a comment to an existing seller.
     *
     * <p>
     * This endpoint allows users to add a comment to an existing seller.
     * </p>
     *
     * @param userId the ID of the user posting the comment.
     * @param commentRequest the comment data to be added.
     * @param request the HTTP request for additional context (e.g., user authentication).
     * @return the response containing the added comment.
     */
    @Operation(
            summary = "Add a comment to a seller",
            description = "Adds a comment to an existing seller.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment successfully added"),
                    @ApiResponse(responseCode = "400", description = "Invalid comment data"),
                    @ApiResponse(responseCode = "404", description = "Seller not found")
            }
    )
    @PostMapping
    public ResponseEntity<CommentResponse> addCommentToSeller(
            @RequestParam(name = "userId") Long userId,
            @RequestBody CommentRequest commentRequest,
            HttpServletRequest request) {

        return ResponseEntity.ok(commentService.addComment(commentRequest, userId, request));
    }

    /**
     * Add a comment and register a new seller (if they don't exist).
     *
     * <p>
     * This endpoint allows users to add a comment and, if the seller does not exist, it will register them.
     * </p>
     *
     * @param sellerFromCommentRequest the seller data along with the comment.
     * @param request the HTTP request for additional context.
     * @return the response containing the added comment and seller.
     */
    @Operation(
            summary = "Add a comment and register a seller",
            description = "Adds a comment and, if the seller does not exist, registers them.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment and seller successfully added"),
                    @ApiResponse(responseCode = "400", description = "Invalid comment or seller data")
            }
    )
    @PostMapping("/register-seller")
    public ResponseEntity<CommentResponse> addCommentToSeller(
            @RequestBody @Valid SellerFromCommentRequest sellerFromCommentRequest,
            HttpServletRequest request) {

        return ResponseEntity.ok(commentService.addComment(sellerFromCommentRequest, request));
    }

    /**
     * Delete a comment (available to the author or the seller).
     *
     * <p>
     * This endpoint allows deleting a comment by its ID. The operation is available only to the author of the comment or the seller.
     * </p>
     *
     * @param commentId the ID of the comment to be deleted.
     * @param request the HTTP request for authentication context.
     * @return a response indicating the successful deletion.
     */
    @Operation(
            summary = "Delete a comment",
            description = "Deletes a comment by ID. Available only to the author of the comment or the seller.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Comment successfully deleted"),
                    @ApiResponse(responseCode = "403", description = "Insufficient permissions to delete the comment"),
                    @ApiResponse(responseCode = "404", description = "Comment not found")
            }
    )
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteCommentById(
            @PathVariable(name = "commentId") Long commentId,
            HttpServletRequest request) {

        commentService.deleteById(commentId, request);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get the list of comments for a seller.
     *
     * <p>
     * This endpoint returns a list of all comments for a specific seller.
     * </p>
     *
     * @param sellerId the ID of the seller whose comments are to be retrieved.
     * @return the list of comments for the seller.
     */
    @Operation(
            summary = "Get a list of comments for a seller",
            description = "Returns a list of all comments for a specific seller.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comments list successfully fetched"),
                    @ApiResponse(responseCode = "404", description = "Seller not found")
            }
    )
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<CommentResponse>> getListOfSellersComments(@PathVariable Long sellerId) {
        return ResponseEntity.ok(commentService.getSellersComments(sellerId));
    }

    /**
     * Get a comment by its ID.
     *
     * <p>
     * This endpoint retrieves the comment identified by its ID.
     * </p>
     *
     * @param commentId the ID of the comment to be retrieved.
     * @return the comment details.
     */
    @Operation(
            summary = "Get a comment by ID",
            description = "Returns the comment identified by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment successfully fetched"),
                    @ApiResponse(responseCode = "404", description = "Comment not found")
            }
    )
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> getCommentById(@PathVariable(name = "commentId") Long commentId) {
        return ResponseEntity.ok(commentService.getCommentById(commentId));
    }

    /**
     * Edit a comment (available only to its author).
     *
     * <p>
     * This endpoint allows the author of the comment to edit it.
     * </p>
     *
     * @param commentId the ID of the comment to be edited.
     * @param commentRequest the updated comment data.
     * @param request the HTTP request for authentication context.
     * @return the updated comment details.
     */
    @Operation(
            summary = "Edit a comment",
            description = "Edits a comment by ID. Available only to the author of the comment.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment successfully edited"),
                    @ApiResponse(responseCode = "400", description = "Invalid comment data for editing"),
                    @ApiResponse(responseCode = "404", description = "Comment not found")
            }
    )
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> editComment(
            @PathVariable(name = "commentId") Long commentId,
            @RequestBody CommentRequest commentRequest,
            HttpServletRequest request) {

        return ResponseEntity.ok(commentService.editComment(commentId, commentRequest, request));
    }

    /**
     * Get all confirmed comments.
     *
     * <p>
     * This endpoint returns a list of all confirmed comments.
     * </p>
     *
     * @return the list of confirmed comments.
     */
    @Operation(
            summary = "Get all confirmed comments",
            description = "Returns all comments that have been confirmed.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Confirmed comments list successfully fetched")
            }
    )
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments() {
        return ResponseEntity.ok(commentService.getAllConfirmed());
    }
}
