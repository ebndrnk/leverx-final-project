package org.ebndrnk.leverxfinalproject.service.auth.password;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebndrnk.leverxfinalproject.model.dto.auth.UserResponse;
import org.ebndrnk.leverxfinalproject.model.dto.password.ForgotPasswordRequest;
import org.ebndrnk.leverxfinalproject.model.dto.password.PasswordResetRequest;
import org.ebndrnk.leverxfinalproject.model.dto.password.ResetCodeVerificationRequest;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.repository.auth.UserRepository;
import org.ebndrnk.leverxfinalproject.service.mail.EmailService;
import org.ebndrnk.leverxfinalproject.util.exception.dto.NoAuthorityForActionException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResetPasswordService {

    private final EmailService emailService;
    private final ResetPasswordCodeService resetPasswordCodeService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Transactional
    public void receiveResetCode(ForgotPasswordRequest request) {
        log.info("Processing forgot password for email: {}", request.email());
        userRepository.findByEmail(request.email()).orElseThrow(() -> new UserNotFoundException("User with this email not found"));
        emailService.sendPasswordResetEmail(request.email());
    }

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

    public boolean verifyVerificationCode(ResetCodeVerificationRequest request) {
        return resetPasswordCodeService.verify(request.userEmail(), request.code());
    }

    public boolean verifyVerificationCode(String userEmail, String code) {
        return resetPasswordCodeService.verify(userEmail, code);
    }
}
