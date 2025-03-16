package org.ebndrnk.leverxfinalproject.service.profile;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileResponse;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.ebndrnk.leverxfinalproject.repository.pofile.ProfileCacheRepository;
import org.ebndrnk.leverxfinalproject.repository.pofile.ProfileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@Primary
@RequiredArgsConstructor
public class ProfileCacheServiceImpl implements ProfileCacheService{
    private final ProfileRepository profileRepository;
    private static final Duration TIME_TO_LIVE = Duration.ofDays(1);
    private final ProfileCacheRepository profileCacheRepository;
    private static final String TOP_SELLERS_KEY = "top_sellers";
    private static final int COUNT_OF_PROFILES_IN_CASHE = 10;
    private final ModelMapper modelMapper;


    @Override
    public List<ProfileResponse> getTopSellersFromCache() {
        return profileCacheRepository.getByKey(TOP_SELLERS_KEY);
    }

    @Override
    public void saveTopSellersToCache(List<ProfileResponse> profiles) {
        profileRepository.findTopSellersWithLimit(
                PageRequest.of(0, COUNT_OF_PROFILES_IN_CASHE)
        );

        profileCacheRepository.save(TOP_SELLERS_KEY, profiles, TIME_TO_LIVE);
    }

    @Override
    public void clearCache() {
        profileCacheRepository.delete(TOP_SELLERS_KEY);
    }

    @Scheduled(initialDelay = 1000, fixedRate = 5 * 60 * 1000)
    @Transactional
    @Override
    public void updateTopSellersCache() {
        List<Profile> profiles = profileRepository.findTopSellersWithLimit(
                PageRequest.of(0, COUNT_OF_PROFILES_IN_CASHE)
        );

        List<ProfileResponse> profileResponses = profiles.stream()
                .map(profile -> modelMapper.map(profile, ProfileResponse.class))
                .toList();

        profileCacheRepository.save(TOP_SELLERS_KEY, profileResponses, TIME_TO_LIVE);
    }
}
