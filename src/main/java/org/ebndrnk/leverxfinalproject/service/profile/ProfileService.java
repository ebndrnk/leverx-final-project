package org.ebndrnk.leverxfinalproject.service.profile;

import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileDto;

public interface ProfileService {
    ProfileDto getProfileById(Long profileId);

    ProfileDto saveProfileInfo(ProfileDto profileDto);

    ProfileDto getProfileByUsername(String username);

    ProfileDto getProfileByEmail(String email);
}
