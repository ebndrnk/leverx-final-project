package org.ebndrnk.leverxfinalproject.service.account.password;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebndrnk.leverxfinalproject.model.entity.auth.password.ResetPasswordCodeEntity;
import org.ebndrnk.leverxfinalproject.repository.auth.UserRepository;
import org.ebndrnk.leverxfinalproject.repository.auth.password.ResetPasswordCodeRepository;
import org.ebndrnk.leverxfinalproject.exception.dto.UserNotFoundException;
import org.ebndrnk.leverxfinalproject.exception.dto.UserWithThisEmailNotDoesntChangePasswordException;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Service for handling the reset password code functionality.
 * Handles saving, verifying, and deleting reset password codes for users.
 * <p>
 * This service is responsible for managing the reset password verification codes:
 * - Saving the verification code when requested.
 * - Verifying the code provided by the user for password reset.
 * - Deleting the code once used or expired.
 * <p>
 * Difference from the {@link ResetPasswordService}:
 * - {@link ResetPasswordService} manages the business logic of password resets, including verifying the reset code and resetting the password.
 * - {@link ResetPasswordCodeServiceImpl} focuses purely on the generation, verification, and deletion of the reset password code itself.
 */
@Service
@Slf4j
@Primary
@RequiredArgsConstructor
public class ResetPasswordCodeServiceImpl implements ResetPasswordCodeService {

    private final ResetPasswordCodeRepository resetPasswordCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Duration TIME_TO_LIVE = Duration.ofDays(1); // 1 day expiration for reset codes
    private final UserRepository userRepository;

    /**
     * Saves the password reset verification code for a user.
     *
     * @param userEmail the email of the user requesting the reset code
     * @param verifyCode the verification code to be saved
     */
    @Override
    public void save(String userEmail, String verifyCode) {
        ResetPasswordCodeEntity entity = new ResetPasswordCodeEntity();
        entity.setEmail(userEmail);
        entity.setCode(passwordEncoder.encode(verifyCode));
        resetPasswordCodeRepository.save(userEmail, entity, TIME_TO_LIVE);
        log.info("Saved reset code for email: {}", userEmail);
    }

    /**
     * Verifies the reset password code for the given email.
     *
     * @param userEmail the email of the user requesting the reset
     * @param verifyCode the code entered by the user
     * @return true if the code is valid and matches, false otherwise
     * @throws UserNotFoundException if the user is not found by email
     * @throws UserWithThisEmailNotDoesntChangePasswordException if no reset code exists for the user
     */
    @Override
    public boolean verify(String userEmail, String verifyCode) {
        userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User with this email not found"));
        ResetPasswordCodeEntity entity = resetPasswordCodeRepository.getByKey(userEmail);
        if (entity == null){
            throw new UserWithThisEmailNotDoesntChangePasswordException("User with this email doesn't change password");
        }
        if (passwordEncoder.matches(verifyCode, entity.getCode())) {
            log.info("Reset code verified for email: {}", userEmail);
            return true;
        }
        log.warn("Reset code invalid or expired for email: {}", userEmail);
        return false;
    }

    /**
     * Deletes the reset code for the given email.
     *
     * @param userEmail the email of the user whose reset code should be deleted
     */
    @Override
    public void delete(String userEmail) {
        resetPasswordCodeRepository.delete(userEmail);
        log.info("Deleted reset code for email: {}", userEmail);
    }
}
