package org.ebndrnk.leverxfinalproject.service.auth.verify;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.auth.verify.VerifyEntity;
import org.ebndrnk.leverxfinalproject.repository.auth.UserRepository;
import org.ebndrnk.leverxfinalproject.repository.auth.verify.VerifyCodeRepository;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UserNotFoundException;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

/**
 * Service implementation for handling user verification operations.
 *
 * <p>
 * This service provides functionalities to save verification codes, verify a given code,
 * confirm a user's email, and delete verification records. It interacts with the VerifyCodeRepository
 * for managing verification codes and with the UserRepository for updating user verification status.
 * </p>
 */
@Service
@Primary
@RequiredArgsConstructor
public class VerifyEmailServiceImpl implements VerifyEmailService {

    private final VerifyCodeRepository verifyCodeRepository;
    private final static Duration TIME_TO_LIVE = Duration.ofDays(1);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Saves a verification code associated with the given user email.
     *
     * <p>
     * Creates a new VerifyEntity object with the provided email and verification code,
     * and then stores it in the verification code repository with a predefined time-to-live.
     * </p>
     *
     * @param userEmail  the email of the user for whom the verification code is generated.
     * @param verifyCode the verification code to be saved.
     */
    @Override
    public void save(String userEmail, String verifyCode) {
        VerifyEntity entity = new VerifyEntity();
        entity.setEmail(userEmail);
        entity.setVerifyCode(passwordEncoder.encode(verifyCode));
        verifyCodeRepository.save(userEmail, entity, TIME_TO_LIVE);
    }

    /**
     * Verifies the provided verification code for the given user email.
     *
     * <p>
     * Retrieves the stored VerifyEntity by user email and checks if the provided code matches.
     * If the code is valid, it confirms the user's email and deletes the verification record.
     * The method is transactional to ensure data consistency during the verification process.
     * </p>
     *
     * @param userEmail  the email of the user attempting verification.
     * @param verifyCode the verification code provided by the user.
     * @return true if the verification code is valid and the user's email is confirmed; false otherwise.
     */
    @Override
    @Transactional
    public boolean verify(String userEmail, String verifyCode) {
        VerifyEntity entity = verifyCodeRepository.getByKey(userEmail);
        if (entity != null && passwordEncoder.matches(verifyCode, entity.getVerifyCode())) {
            confirmEmail(userEmail);
            delete(userEmail);
            return true;
        }
        delete(userEmail);
        return false;
    }

    /**
     * Confirms the email of a user by setting the emailConfirmed flag to true.
     *
     * <p>
     * Retrieves the user by email from the repository, updates the emailConfirmed property,
     * and saves the updated user. Throws a UserNotFoundException if the user is not found.
     * </p>
     *
     * @param userEmail the email of the user whose email should be confirmed.
     */
    @Override
    public void confirmEmail(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User with this email not found"));
        user.setEmailConfirmed(true);
        userRepository.save(user);
    }

    /**
     * Deletes the verification record associated with the given user email.
     *
     * <p>
     * Removes the VerifyEntity from the repository to prevent reuse of the verification code.
     * </p>
     *
     * @param userEmail the email of the user whose verification record should be deleted.
     */
    @Override
    public void delete(String userEmail) {
        verifyCodeRepository.delete(userEmail);
    }
}
