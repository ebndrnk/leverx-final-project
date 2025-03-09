package org.ebndrnk.leverxfinalproject.repository.auth.verify;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.entity.auth.verify.VerifyEntity;
import org.ebndrnk.leverxfinalproject.repository.SimpleRedisRepository;
import org.ebndrnk.leverxfinalproject.util.exception.dto.RedisOperationException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class VerifyCodeRepository implements SimpleRedisRepository<VerifyEntity, String> {
    private final RedisTemplate<String, VerifyEntity> redisTemplate;

    @Override
    public void save(String key, VerifyEntity entity, Duration TTL) {
        try {
            redisTemplate.opsForValue().set(key, entity, TTL);
        }catch (DataAccessException e){
            throw new RedisOperationException("Error saving reset password code for key: " + key, e);
        }
    }

    @Override
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (DataAccessException e) {
            throw new RedisOperationException("Error deleting reset password code for key: " + key, e);
        }
    }

    @Override
    public VerifyEntity getByKey(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (DataAccessException e) {
            throw new RedisOperationException("Error deleting reset password code for key: " + key, e);
        }
    }
}
