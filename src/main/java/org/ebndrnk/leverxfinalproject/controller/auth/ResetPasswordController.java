package org.ebndrnk.leverxfinalproject.controller.auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebndrnk.leverxfinalproject.model.dto.auth.UserResponse;
import org.ebndrnk.leverxfinalproject.model.dto.password.ForgotPasswordRequest;
import org.ebndrnk.leverxfinalproject.model.dto.password.PasswordResetRequest;
import org.ebndrnk.leverxfinalproject.model.dto.password.ResetCodeVerificationRequest;
import org.ebndrnk.leverxfinalproject.service.auth.password.ResetPasswordService;
import org.ebndrnk.leverxfinalproject.util.exception.dto.NoAuthorityForActionException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @PostMapping("/forgot_password")
    public ResponseEntity<Void> receiveResetCode(@Valid @RequestBody ForgotPasswordRequest request) {
        log.info("Received forgot password request for email: {}", request.email());
        resetPasswordService.receiveResetCode(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset")
    public ResponseEntity<UserResponse> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        log.info("Attempting to reset password for email: {}", request.userEmail());
        return ResponseEntity.ok(resetPasswordService.resetPassword(request));
    }

    @GetMapping("/check_code")
    public ResponseEntity<Boolean> verifyVerificationCode(@Valid @RequestBody ResetCodeVerificationRequest request) {
        log.info("Verifying reset code for email: {}", request.userEmail());
        boolean isValid = resetPasswordService.verifyVerificationCode(request);
        return ResponseEntity.ok(isValid);
    }
}
