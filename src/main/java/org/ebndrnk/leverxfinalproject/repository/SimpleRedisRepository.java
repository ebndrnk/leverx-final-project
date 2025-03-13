package org.ebndrnk.leverxfinalproject.repository;

import java.time.Duration;

/**
 * This is a repository interface for interacting with Redis.
 * It defines basic operations for saving, deleting, and retrieving entities from Redis.
 *
 * @param <T> The type of entity to be stored in Redis
 * @param <ID> The type of the key used to identify the entity in Redis
 */
public interface SimpleRedisRepository<T, ID> {

    /**
     * Saves the given entity in Redis with the specified key and time-to-live (TTL).
     *
     * @param key The unique key to identify the entity
     * @param entity The entity to be saved
     * @param TTL The time-to-live duration for the entity in Redis
     */
    void save(ID key, T entity, Duration TTL);

    /**
     * Deletes the entity identified by the given key from Redis.
     *
     * @param key The unique key of the entity to delete
     */
    void delete(ID key);

    /**
     * Retrieves the entity identified by the given key from Redis.
     *
     * @param key The unique key of the entity to retrieve
     * @return The entity if found, otherwise null
     */
    T getByKey(ID key);
}
