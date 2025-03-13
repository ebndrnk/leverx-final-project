package org.ebndrnk.leverxfinalproject.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileResponse;
import org.ebndrnk.leverxfinalproject.model.entity.auth.password.ResetPasswordCodeEntity;
import org.ebndrnk.leverxfinalproject.model.entity.auth.verify.VerifyEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.List;

/**
 * Configuration class for setting up Redis templates for different types of objects.
 * This class provides beans for managing the serialization and storage of entities in Redis.
 */
@Configuration
public class RedisConfig {

    /**
     * Creates and configures a {@link RedisTemplate} for handling keys of type {@code String}
     * and values of type {@link VerifyEntity}.
     * <p>
     * This template is used to store email verification data in Redis.
     * It uses {@link Jackson2JsonRedisSerializer} to serialize {@link VerifyEntity} objects to JSON.
     * </p>
     *
     * @param connectionFactory the Redis connection factory used to establish a connection to the Redis server.
     * @return a configured {@link RedisTemplate} for managing {@link VerifyEntity} objects.
     */
    @Bean
    public RedisTemplate<String, VerifyEntity> redisVerifyEmailTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, VerifyEntity> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        Jackson2JsonRedisSerializer<VerifyEntity> serializer =
                new Jackson2JsonRedisSerializer<>(VerifyEntity.class);

        template.setDefaultSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * Creates and configures a {@link RedisTemplate} for handling keys of type {@code String}
     * and values of type {@link ResetPasswordCodeEntity}.
     * <p>
     * This template is used to store password reset codes in Redis.
     * It employs {@link Jackson2JsonRedisSerializer} for JSON serialization of {@link ResetPasswordCodeEntity} objects.
     * </p>
     *
     * @param connectionFactory the Redis connection factory used to establish a connection to the Redis server.
     * @return a configured {@link RedisTemplate} for managing {@link ResetPasswordCodeEntity} objects.
     */
    @Bean
    public RedisTemplate<String, ResetPasswordCodeEntity> redisResetPasswordTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, ResetPasswordCodeEntity> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        Jackson2JsonRedisSerializer<ResetPasswordCodeEntity> serializer =
                new Jackson2JsonRedisSerializer<>(ResetPasswordCodeEntity.class);

        template.setDefaultSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * Creates and configures a {@link RedisTemplate} for handling keys of type {@code String}
     * and values of type {@code List<ProfileResponse>}.
     * <p>
     * This template is designed to store lists of {@link ProfileResponse} objects under a single Redis key.
     * Since Redis does not natively support generic types, the {@link ObjectMapper} and {@link TypeFactory}
     * are used to properly define the type for serialization.
     * </p>
     *
     * @param connectionFactory the Redis connection factory used to establish a connection to the Redis server.
     * @return a configured {@link RedisTemplate} for managing lists of {@link ProfileResponse} objects.
     */
    @Bean
    public RedisTemplate<String, List<ProfileResponse>> redisProfileResponseTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, List<ProfileResponse>> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        ObjectMapper objectMapper = new ObjectMapper();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        Jackson2JsonRedisSerializer<List<ProfileResponse>> serializer =
                new Jackson2JsonRedisSerializer<>(typeFactory.constructCollectionType(List.class, ProfileResponse.class));

        template.setDefaultSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }
}
