package org.ebndrnk.leverxfinalproject.service.profile;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileDto;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileResponse;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.ebndrnk.leverxfinalproject.repository.pofile.ProfileRepository;
import org.ebndrnk.leverxfinalproject.repository.specification.ProfileSpecification;
import org.ebndrnk.leverxfinalproject.service.rating.RatingService;
import org.ebndrnk.leverxfinalproject.util.exception.dto.ProfileNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService{
    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;
    private final RatingService ratingService;

    @Override
    public ProfileDto getProfileDtoById(Long profileId){
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

    @Override
    public List<ProfileResponse> getAll() {
        return profileRepository.findAll().stream()
                .map(profile -> modelMapper.map(profile, ProfileResponse.class))
                .toList();
    }

    @Override
    public ProfileResponse getProfileResponseById(Long profileId) {
        return modelMapper.map(profileRepository.findById(profileId), ProfileResponse.class);
    }

    @Override
    public List<ProfileResponse> getTopSellers(int count) {
        return profileRepository.findTopSellersWithLimit(count).stream()
                .map(profile -> modelMapper.map(profile, ProfileResponse.class))
                .toList();
    }

    @Override
    public List<ProfileResponse> findProfilesByRating(Byte minRating, Byte maxRating) {
        Specification<Profile> spec = ProfileSpecification.hasRatingBetween(minRating, maxRating);
        return profileRepository.findAll(spec).stream()
                .map(profile -> modelMapper.map(profile, ProfileResponse.class))
                .toList();
    }
}
