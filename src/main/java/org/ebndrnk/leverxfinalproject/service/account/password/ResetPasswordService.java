package org.ebndrnk.leverxfinalproject.service.account.password;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebndrnk.leverxfinalproject.model.dto.auth.UserResponse;
import org.ebndrnk.leverxfinalproject.model.dto.password.ForgotPasswordRequest;
import org.ebndrnk.leverxfinalproject.model.dto.password.PasswordResetRequest;
import org.ebndrnk.leverxfinalproject.model.dto.password.ResetCodeVerificationRequest;
import org.ebndrnk.leverxfinalproject.model.dto.password.ResetCodeVerificationResponse;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.repository.auth.UserRepository;
import org.ebndrnk.leverxfinalproject.service.mail.EmailServiceImpl;
import org.ebndrnk.leverxfinalproject.util.exception.dto.NoAuthorityForActionException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for handling password reset functionality.
 * Handles receiving reset codes, verifying codes, and resetting the user's password.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ResetPasswordService {

    private final EmailServiceImpl emailService;
    private final ResetPasswordCodeService resetPasswordCodeService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    /**
     * Receives a reset code request and sends a password reset email to the user.
     *
     * @param request the request containing the email address for password reset
     */
    @Transactional
    public void receiveResetCode(ForgotPasswordRequest request) {
        log.info("Processing forgot password for email: {}", request.email());
        userRepository.findByEmail(request.email()).orElseThrow(() -> new UserNotFoundException("User with this email not found"));
        emailService.sendPasswordResetEmail(request.email());
    }

    /**
     * Resets the user's password if the provided reset code is valid.
     *
     * @param request the request containing the user's email, reset code, and new password
     * @return the updated user response after password reset
     * @throws NoAuthorityForActionException if the verification code is incorrect
     * @throws UserNotFoundException if the user is not found by email
     */
    @Transactional
    public UserResponse resetPassword(PasswordResetRequest request) {
        String userEmail = request.userEmail();
        String code = request.code();

        log.info("Resetting password for email: {}", userEmail);
        if (!verifyVerificationCode(userEmail, code)) {
            log.warn("Invalid reset code for email: {}", userEmail);
            throw new NoAuthorityForActionException("Verification code incorrect for this email");
        }
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User with this email not found"));
        user.setPassword(passwordEncoder.encode(request.newPassword()));
        resetPasswordCodeService.delete(userEmail);
        log.info("Password reset successful for email: {}", userEmail);
        return modelMapper.map(userRepository.save(user), UserResponse.class);
    }

    /**
     * Verifies the reset code for the given user email.
     *
     * @param request the request containing the user email and reset code to verify
     * @return the response with the result of the verification
     */
    public ResetCodeVerificationResponse verifyVerificationCode(ResetCodeVerificationRequest request) {
        log.debug("Verifying reset code for email: {}", request.userEmail());
        return new ResetCodeVerificationResponse(resetPasswordCodeService.verify(request.userEmail(), request.code()));
    }

    /**
     * Verifies the reset code for the given user email and code.
     *
     * @param userEmail the user email for verification
     * @param code the verification code to check
     * @return true if the verification code is valid, otherwise false
     */
    public boolean verifyVerificationCode(String userEmail, String code) {
        return resetPasswordCodeService.verify(userEmail, code);
    }
}
