package org.ebndrnk.leverxfinalproject.service.auth.password;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebndrnk.leverxfinalproject.model.entity.auth.password.ResetPasswordCodeEntity;
import org.ebndrnk.leverxfinalproject.repository.auth.UserRepository;
import org.ebndrnk.leverxfinalproject.repository.auth.password.ResetPasswordCodeRepository;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UserNotFoundException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UserWithThisEmailNotDoesntChangePasswordException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResetPasswordCodeServiceImpl implements ResetPasswordCodeService {

    private final ResetPasswordCodeRepository resetPasswordCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private static final Duration TIME_TO_LIVE = Duration.ofDays(1);
    private final UserRepository userRepository;

    @Override
    public void save(String userEmail, String verifyCode) {
        ResetPasswordCodeEntity entity = new ResetPasswordCodeEntity();
        entity.setEmail(userEmail);
        entity.setCode(passwordEncoder.encode(verifyCode));
        resetPasswordCodeRepository.save(userEmail, entity, TIME_TO_LIVE);
        log.info("Saved reset code for email: {}", userEmail);
    }

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

    @Override
    public void delete(String userEmail) {
        resetPasswordCodeRepository.delete(userEmail);
        log.info("Deleted reset code for email: {}", userEmail);
    }
}
