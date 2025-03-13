package org.ebndrnk.leverxfinalproject.service.profile;

import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProfileCacheService {
    List<ProfileResponse> getTopSellersFromCache();

    void saveTopSellersToCache(List<ProfileResponse> profiles);

    void clearCache();

    @Scheduled(initialDelay = 1000, fixedRate = 24 * 60 * 60 * 1000)
    @Transactional
    void updateTopSellersCache();
}
