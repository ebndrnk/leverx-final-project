package org.ebndrnk.leverxfinalproject.service.profile;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileDto;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.ebndrnk.leverxfinalproject.repository.pofile.ProfileRepository;
import org.ebndrnk.leverxfinalproject.util.exception.dto.ProfileNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{
    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;

    @Override
    public ProfileDto getProfileById(Long profileId){
        return modelMapper.map(profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile not found for this profile")), ProfileDto.class);
    }

    @Override
    public ProfileDto saveProfileInfo(ProfileDto profileDto){
        return modelMapper.map(profileRepository.save(modelMapper.map(profileDto, Profile.class)), ProfileDto.class);
    }

    @Override
    public ProfileDto getProfileByUsername(String username){
        return modelMapper.map(profileRepository.findByUsername(username)
                .orElseThrow(() -> new ProfileNotFoundException("Profile with this username not found")), ProfileDto.class);
    }

    @Override
    public ProfileDto getProfileByEmail(String email){
        return modelMapper.map(profileRepository.findByEmail(email)
                .orElseThrow(() -> new ProfileNotFoundException("Profile with this email not found")), ProfileDto.class);
    }
}
