package org.ebndrnk.leverxfinalproject.config;

import org.ebndrnk.leverxfinalproject.model.entity.auth.verify.VerifyEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * Configuration class for setting up a RedisTemplate
 * to work with {@link VerifyEntity} objects.
 */
@Configuration
public class RedisConfig {

    /**
     * Creates and configures a {@link RedisTemplate} for handling keys of type {@code String}
     * and values of type {@link VerifyEntity}.
     * <p>
     * This method sets up the value serializer using {@link Jackson2JsonRedisSerializer},
     * which converts {@link VerifyEntity} objects to JSON for storage in Redis and
     * converts them back to objects when retrieved.
     * </p>
     *
     * @param connectionFactory the Redis connection factory used to establish a connection to the Redis server.
     * @return a configured {@link RedisTemplate} for managing {@link VerifyEntity} objects.
     */
    @Bean
    public RedisTemplate<String, VerifyEntity> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, VerifyEntity> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        Jackson2JsonRedisSerializer<VerifyEntity> serializer =
                new Jackson2JsonRedisSerializer<>(VerifyEntity.class);

        template.setDefaultSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }
}
