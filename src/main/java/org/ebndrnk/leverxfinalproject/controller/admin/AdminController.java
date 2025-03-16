package org.ebndrnk.leverxfinalproject.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentResponse;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileResponse;
import org.ebndrnk.leverxfinalproject.service.admin.AdminService;
import org.ebndrnk.leverxfinalproject.service.comment.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing administrative operations on users and comments.
 * <p>
 * This REST controller provides endpoints for performing administrative tasks, such as:
 * <ul>
 *   <li>Retrieving and confirming/unconfirming users</li>
 *   <li>Managing comment confirmations</li>
 *   <li>Deleting users and comments</li>
 * </ul>
 * </p>
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "Operations for managing users and comments by admin.")
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final CommentService commentService;

    /**
     * Retrieves a list of users who have not been confirmed by an administrator.
     *
     * @return {@link ResponseEntity} containing a list of {@link ProfileResponse} objects for unconfirmed users.
     */
    @Operation(summary = "Get unconfirmed users", description = "Returns a list of users not confirmed by an administrator.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping("/users/not-confirmed")
    public ResponseEntity<List<ProfileResponse>> getAllNotConfirmedByAdminUsers() {
        return ResponseEntity.ok(adminService.getAllNotConfirmedByAdminUsers());
    }

    /**
     * Confirms a user by setting their confirmation flag to true.
     *
     * @param userId the ID of the user to confirm.
     * @return {@link ResponseEntity} containing the updated {@link ProfileResponse}.
     */
    @Operation(summary = "Confirm user", description = "Confirms a user by setting the confirmation flag to true.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User confirmed successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/users/{userId}/confirmation")
    public ResponseEntity<ProfileResponse> confirmUser(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(adminService.confirmUserByAdmin(userId));
    }

    /**
     * Deletes a user from the system.
     *
     * @param userId the ID of the user to delete.
     * @return {@link ResponseEntity} with status 200 if the user was deleted.
     */
    @Operation(summary = "Delete user", description = "Deletes a user from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "userId") Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    /**
     * Cancels the confirmation of a user by setting their confirmation flag to false.
     *
     * @param userId the ID of the user whose confirmation should be canceled.
     * @return {@link ResponseEntity} containing the updated {@link ProfileResponse}.
     */
    @Operation(summary = "Cancel user confirmation", description = "Cancels the admin confirmation by setting the confirmation flag to false.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Confirmation canceled successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/users/{userId}/decline")
    public ResponseEntity<ProfileResponse> cancelConfirmation(@PathVariable(name = "userId") Long userId) {
        return ResponseEntity.ok(adminService.cancelAdminConfirmation(userId));
    }

    /**
     * Retrieves a list of comments that have not been confirmed by an administrator.
     *
     * @return {@link ResponseEntity} containing a list of unconfirmed {@link CommentResponse} objects.
     */
    @Operation(summary = "Get unconfirmed comments", description = "Returns a list of comments not confirmed by an administrator.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comments retrieved successfully")
    })
    @GetMapping("/comments/not-confirmed")
    public ResponseEntity<List<CommentResponse>> getAllUnconfirmed() {
        return ResponseEntity.ok(commentService.getAllUnconfirmed());
    }

    /**
     * Confirms a comment by setting its confirmation flag to true.
     *
     * @param commentId the ID of the comment to confirm.
     * @return {@link ResponseEntity} containing the updated {@link CommentResponse}.
     */
    @Operation(summary = "Confirm comment", description = "Confirms a comment by setting the confirmation flag to true.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment confirmed successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PatchMapping("/comments/{commentId}/confirm")
    public ResponseEntity<CommentResponse> confirmComment(@PathVariable(name = "commentId") Long commentId) {
        return ResponseEntity.ok(commentService.confirm(commentId));
    }

    /**
     * Declines a comment by setting its confirmation flag to false.
     *
     * @param commentId the ID of the comment to decline.
     * @return {@link ResponseEntity} containing the updated {@link CommentResponse}.
     */
    @Operation(summary = "Decline comment", description = "Declines a comment by setting the confirmation flag to false.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment declined successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @PatchMapping("/comments/{commentId}/decline")
    public ResponseEntity<CommentResponse> declineComment(@PathVariable(name = "commentId") Long commentId) {
        return ResponseEntity.ok(commentService.decline(commentId));
    }

    /**
     * Deletes a comment from the system.
     *
     * @param commentId the ID of the comment to delete.
     * @return {@link ResponseEntity} with status 200 if the comment was deleted.
     */
    @Operation(summary = "Delete comment", description = "Deletes a comment from the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Comment not found")
    })
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.deleteById(commentId);
        return ResponseEntity.ok().build();
    }
}
