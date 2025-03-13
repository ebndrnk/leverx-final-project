package org.ebndrnk.leverxfinalproject.service.profile;

import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileDto;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProfileService {
    ProfileDto getProfileDtoById(Long profileId);

    ProfileDto saveProfileInfo(ProfileDto profileDto);

    ProfileDto getProfileByUsername(String username);

    ProfileDto getProfileByEmail(String email);

    Page<?> getAll(boolean isProjection, Pageable pageable);

    ProfileResponse getProfileResponseById(Long profileId);

    Page<ProfileResponse> getTopSellers(int count, Pageable pageable);

    Page<ProfileResponse> findProfilesByRating(Byte minRating, Byte maxRating, Pageable pageable);

}
