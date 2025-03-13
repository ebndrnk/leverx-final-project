package org.ebndrnk.leverxfinalproject.service.profile;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileDto;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileResponse;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.ebndrnk.leverxfinalproject.repository.pofile.ProfileRepository;
import org.ebndrnk.leverxfinalproject.repository.specification.ProfileSpecification;
import org.ebndrnk.leverxfinalproject.util.exception.dto.ProfileNotFoundException;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for handling profile-related operations.
 * <p>
 * This service provides methods to retrieve, save, and manage profiles,
 * including operations like getting profiles by ID, username, email,
 * saving profile information, and retrieving top sellers.
 * </p>
 */
@Service
@Slf4j
@Primary
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {


    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;
    private final ProfileCacheService profileCacheService;

    /**
     * Retrieves the profile DTO by its ID.
     * If the profile is not found, a ProfileNotFoundException is thrown.
     *
     * @param profileId the ID of the profile to retrieve.
     * @return the ProfileDto representing the profile.
     */
    @Override
    public ProfileDto getProfileDtoById(Long profileId) {
        log.info("Retrieving profile with ID: {}", profileId);
        return modelMapper.map(profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found for this profile")), ProfileDto.class);
    }

    /**
     * Saves the profile information and returns the saved ProfileDto.
     *
     * @param profileDto the profile data to be saved.
     * @return the saved ProfileDto.
     */
    @Override
    public ProfileDto saveProfileInfo(ProfileDto profileDto) {
        log.info("Saving profile information for: {}", profileDto.getUsername());
        return modelMapper.map(profileRepository.save(modelMapper.map(profileDto, Profile.class)), ProfileDto.class);
    }

    /**
     * Retrieves the profile DTO by its username.
     * If the profile is not found, a ProfileNotFoundException is thrown.
     *
     * @param username the username of the profile to retrieve.
     * @return the ProfileDto representing the profile.
     */
    @Override
    public ProfileDto getProfileByUsername(String username) {
        log.info("Retrieving profile with username: {}", username);
        return modelMapper.map(profileRepository.findByUsername(username)
                .orElseThrow(() -> new ProfileNotFoundException("Profile with this username not found")), ProfileDto.class);
    }

    /**
     * Retrieves the profile DTO by its email.
     * If the profile is not found, a ProfileNotFoundException is thrown.
     *
     * @param email the email of the profile to retrieve.
     * @return the ProfileDto representing the profile.
     */
    @Override
    public ProfileDto getProfileByEmail(String email) {
        log.info("Retrieving profile with email: {}", email);
        if(email == null){
            throw new ProfileNotFoundException("Profile with this email not found");
        }
        return modelMapper.map(profileRepository.findByEmail(email)
                .orElseThrow(() -> new ProfileNotFoundException("Profile with this email not found")), ProfileDto.class);
    }

    /**
     * Retrieves a paginated list of profiles.
     * If 'isProjection' is true, returns a projection of the profiles; otherwise, returns the full profiles.
     *
     * @param isProjection flag to indicate if a projection should be used.
     * @param pageable pagination information.
     * @return a Page of ProfileResponse or projected profiles.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<?> getAll(boolean isProjection, Pageable pageable) {
        if (isProjection) {
            log.info("Retrieving profiles with projection");
            return profileRepository.findAllProjectedProfiles(pageable);
        }
        log.info("Retrieving all profiles");
        return profileRepository.findAll(pageable)
                .map(profile -> modelMapper.map(profile, ProfileResponse.class));
    }

    /**
     * Retrieves a ProfileResponse by profile ID.
     *
     * @param profileId the ID of the profile.
     * @return the ProfileResponse representing the profile.
     */
    @Override
    public ProfileResponse getProfileResponseById(Long profileId) {
        log.info("Retrieving profile response for profile ID: {}", profileId);
        return modelMapper.map(profileRepository.findById(profileId), ProfileResponse.class);
    }

    /**
     * Retrieves the top sellers, either from the cache or the database, based on the count.
     * If the count is 10 and the cache is not empty, the data is fetched from the cache.
     * Otherwise, it fetches data from the database.
     *
     * @param count the number of top sellers to retrieve.
     * @return a Page of ProfileResponse containing top sellers.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProfileResponse> getTopSellers(int count) {
        List<ProfileResponse> profileResponsesFromCache = getTopSellersFromCache();
        if (count == 10 && profileResponsesFromCache != null) {
            log.info("Values received from cache");
            return new PageImpl<>(profileResponsesFromCache);
        }

        log.info("Values received from DB");
        return new PageImpl<>(getTopSellersFromDB(count));
    }

    /**
     * Retrieves the top sellers from the database based on the provided pageable.
     *
     * @param count is count of returning sellers.
     * @return a list of ProfileResponse representing the top sellers.
     */
    private List<ProfileResponse> getTopSellersFromDB(int count) {
        log.debug("Retrieving top sellers from DB with pagination: {}", count);
        return profileRepository.findTopSellersWithLimit(PageRequest.of(0, count))
                .stream()
                .map(profile -> modelMapper.map(profile, ProfileResponse.class))
                .toList();
    }

    /**
     * Retrieves the top sellers from the cache.
     *
     * @return a list of ProfileResponse representing the top sellers from the cache.
     */
    private List<ProfileResponse> getTopSellersFromCache() {
        log.debug("Retrieving top sellers from cache");
        return profileCacheService.getTopSellersFromCache();
    }

    /**
     * Retrieves profiles based on a rating range.
     *
     * @param minRating the minimum rating.
     * @param maxRating the maximum rating.
     * @param pageable pagination information.
     * @return a Page of ProfileResponse containing profiles matching the rating criteria.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProfileResponse> findProfilesByRating(Byte minRating, Byte maxRating, Pageable pageable) {
        log.info("Finding profiles with rating between {} and {}", minRating, maxRating);
        Specification<Profile> spec = ProfileSpecification.hasRatingBetween(minRating, maxRating);
        return profileRepository.findAll(spec, pageable)
                .map(profile -> modelMapper.map(profile, ProfileResponse.class));
    }
}
