package org.ebndrnk.leverxfinalproject.repository.pofile;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileResponse;
import org.ebndrnk.leverxfinalproject.model.entity.auth.password.ResetPasswordCodeEntity;
import org.ebndrnk.leverxfinalproject.repository.SimpleRedisRepository;
import org.ebndrnk.leverxfinalproject.util.exception.dto.RedisOperationException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProfileCacheRepository implements SimpleRedisRepository<List<ProfileResponse>, String> {
    private final RedisTemplate<String, List<ProfileResponse>> redisTemplate;

    @Override
    public void save(String key, List<ProfileResponse> entity, Duration TTL) {
        try {
            redisTemplate.opsForValue().set(key, entity, TTL);
        } catch (DataAccessException e) {
            throw new RedisOperationException("Error saving list of best sellers: ", e);
        }
    }

    @Override
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (DataAccessException e) {
            throw new RedisOperationException("Error deleting list of best sellers: " , e);
        }
    }

    @Override
    public List<ProfileResponse> getByKey(String key) {
        try {
            return redisTemplate.opsForValue().get(key);
        } catch (DataAccessException e) {
            throw new RedisOperationException("Error retrieving list of best sellers: " , e);
        }
    }
}
