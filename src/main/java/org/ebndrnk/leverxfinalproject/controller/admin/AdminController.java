package org.ebndrnk.leverxfinalproject.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.comment.CommentResponse;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileResponse;
import org.ebndrnk.leverxfinalproject.service.admin.AdminService;
import org.ebndrnk.leverxfinalproject.service.comment.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing administrative operations on users.
 *
 * <p>
 * This REST controller provides endpoints for performing administrative tasks related to users, such as:
 * <ul>
 *   <li>Retrieving a list of all users and users not confirmed by an administrator</li>
 *   <li>Confirming an individual user or all users</li>
 *   <li>Cancelling confirmation for an individual user</li>
 * </ul>
 * The controller demonstrates best practices, including:
 * <ul>
 *   <li>Correct usage of HTTP status codes</li>
 *   <li>Clear separation of concerns through the service layer</li>
 *   <li>Detailed API documentation using Swagger/OpenAPI annotations</li>
 * </ul>
 * </p>
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final CommentService commentService;



    /**
     * Retrieves a list of users not confirmed by an administrator.
     *
     * <p>
     * This endpoint returns users whose admin confirmation flag is set to false.
     * </p>
     *
     * @return ResponseEntity containing the list of users not confirmed by an administrator.
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
     * Confirms an individual user.
     *
     * <p>
     * This endpoint updates the user's confirmation flag to true, indicating that the user has been confirmed by an administrator.
     * </p>
     *
     * @param userId the identifier of the user to be confirmed.
     * @return ResponseEntity containing the updated UserDto.
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
     * Cancels the admin confirmation for a user.
     *
     * <p>
     * This endpoint updates the user's confirmation flag to false, thereby canceling the previously set confirmation.
     * </p>
     *
     * @param userId the identifier of the user for which the confirmation needs to be canceled.
     * @return ResponseEntity containing the updated UserDto.
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

    @GetMapping("/comments/not-confirmed")
    public ResponseEntity<List<CommentResponse>> getAllUnconfirmed(){
        return ResponseEntity.ok(commentService.getAllUnconfirmed());
    }

    @PatchMapping("/comments/{commentId}/confirm")
    public ResponseEntity<CommentResponse> confirmComment(@PathVariable(name = "commentId") Long commentId){
        return ResponseEntity.ok(commentService.confirm(commentId));
    }

    @PatchMapping("/comments/{commentId}/decline")
    public ResponseEntity<CommentResponse> declineComment(@PathVariable(name = "commentId") Long commentId){
        return ResponseEntity.ok(commentService.decline(commentId));
    }
}
