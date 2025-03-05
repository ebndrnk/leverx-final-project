package org.ebndrnk.leverxfinalproject.service.auth.verify;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.auth.verify.VerifyEntity;
import org.ebndrnk.leverxfinalproject.repository.auth.verify.VerifyCodeRepository;
import org.ebndrnk.leverxfinalproject.service.auth.UserService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@Primary
@RequiredArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    private final UserService userService;
    private final VerifyCodeRepository verifyCodeRepository;
    private final static Duration TIME_TO_LIVE = Duration.ofDays(1);


    @Override
    public void save(String userEmail, String verifyCode) {
        VerifyEntity entity = new VerifyEntity();
        entity.setEmail(userEmail);
        entity.setVerifyCode(verifyCode);
        verifyCodeRepository.save(userEmail, entity, TIME_TO_LIVE);
    }

    @Override
    @Transactional
    public boolean verify(String userEmail, String verifyCode) {
        VerifyEntity entity = verifyCodeRepository.getByKey(userEmail);
        if (entity != null && verifyCode.equals(entity.getVerifyCode())) {
            User user = userService.getByUserEmail(userEmail);
            user.setConfirmed(true);
            userService.save(user);
            delete(userEmail);
            return true;
        }
        return false;
    }

    public void delete(String userEmail){
        verifyCodeRepository.delete(userEmail);
    }
}
