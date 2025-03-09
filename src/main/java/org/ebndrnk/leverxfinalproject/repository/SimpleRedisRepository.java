package org.ebndrnk.leverxfinalproject.repository;

import java.time.Duration;

public interface SimpleRedisRepository<T, ID> {
    void save(ID key, T entity, Duration TTL);
    void delete(ID key);
    T getByKey(ID key);
}
