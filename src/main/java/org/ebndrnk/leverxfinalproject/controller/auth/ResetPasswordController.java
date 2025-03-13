package org.ebndrnk.leverxfinalproject.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.auth.UserResponse;
import org.ebndrnk.leverxfinalproject.model.dto.password.ForgotPasswordRequest;
import org.ebndrnk.leverxfinalproject.model.dto.password.PasswordResetRequest;
import org.ebndrnk.leverxfinalproject.model.dto.password.ResetCodeVerificationRequest;
import org.ebndrnk.leverxfinalproject.model.dto.password.ResetCodeVerificationResponse;
import org.ebndrnk.leverxfinalproject.service.auth.password.ResetPasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Reset Password Controller
 *
 * <p>
 * This controller handles password reset operations, including sending reset codes,
 * verifying codes, and setting new passwords. It provides endpoints for secure password management.
 * </p>
 */
@RestController
@Validated
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Password Reset", description = "Endpoints for password recovery and reset")
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    /**
     * Send a password reset code to the user's email.
     *
     * @param request the request containing the user's email.
     * @return ResponseEntity with HTTP status 200 if the reset code is sent successfully.
     */
    @PostMapping("/forgot_password")
    @Operation(summary = "Send password reset code",
            description = "Generates and sends a password reset code to the specified email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset code sent successfully"),
            @ApiResponse(responseCode = "404", description = "User with this email not found")
    })
    public ResponseEntity<Void> receiveResetCode(@Valid @RequestBody ForgotPasswordRequest request) {
        resetPasswordService.receiveResetCode(request);
        return ResponseEntity.ok().build();
    }

    /**
     * Reset the user's password using the reset code.
     *
     * @param request the request containing the user's email, reset code, and new password.
     * @return ResponseEntity containing the updated user information.
     */
    @PostMapping("/reset")
    @Operation(summary = "Reset password",
            description = "Resets the user's password if the reset code is valid.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid reset code or expired code"),
            @ApiResponse(responseCode = "404", description = "User with this email not found")
    })
    public ResponseEntity<UserResponse> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        return ResponseEntity.ok(resetPasswordService.resetPassword(request));
    }

    /**
     * Verify the password reset code.
     *
     * @param request the request containing the user's email and reset code.
     * @return ResponseEntity containing the result of the verification (valid/invalid).
     */
    @PostMapping("/check_code")
    @Operation(summary = "Verify password reset code",
            description = "Checks if the provided password reset code is valid for the given email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset code verification result"),
            @ApiResponse(responseCode = "400", description = "Invalid reset code"),
            @ApiResponse(responseCode = "404", description = "User with this email not found")
    })
    public ResponseEntity<ResetCodeVerificationResponse> verifyVerificationCode(@Valid @RequestBody ResetCodeVerificationRequest request) {
        return ResponseEntity.ok(resetPasswordService.verifyVerificationCode(request));
    }
}
