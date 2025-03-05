package org.ebndrnk.leverxfinalproject.repository.auth.verify;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.entity.auth.verify.VerifyEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class VerifyCodeRepository implements SimpleRedisRepository<VerifyEntity, String>{
    private final RedisTemplate<String, VerifyEntity> redisTemplate;

    @Override
    public void save(String key, VerifyEntity entity, Duration TTL) {
        redisTemplate.opsForValue().set(key, entity, TTL);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public VerifyEntity getByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
