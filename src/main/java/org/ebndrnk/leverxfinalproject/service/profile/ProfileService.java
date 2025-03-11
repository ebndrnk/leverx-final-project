package org.ebndrnk.leverxfinalproject.service.profile;

import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileDto;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileResponse;
import org.ebndrnk.leverxfinalproject.model.dto.rating.RatingRequest;

import java.util.List;

public interface ProfileService {
    ProfileDto getProfileDtoById(Long profileId);

    ProfileDto saveProfileInfo(ProfileDto profileDto);

    ProfileDto getProfileByUsername(String username);

    ProfileDto getProfileByEmail(String email);

    List<ProfileResponse> getAll();

    ProfileResponse getProfileResponseById(Long profileId);

    List<ProfileResponse> getTopSellers(int count);

    List<ProfileResponse> findProfilesByRating(Byte minRating, Byte maxRating);
}
