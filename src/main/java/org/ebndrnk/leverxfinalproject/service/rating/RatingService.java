package org.ebndrnk.leverxfinalproject.service.rating;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileResponse;
import org.ebndrnk.leverxfinalproject.model.dto.rating.RatingRequest;
import org.ebndrnk.leverxfinalproject.model.entity.anonymous.AnonymousUser;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.ebndrnk.leverxfinalproject.model.entity.rating.Rating;
import org.ebndrnk.leverxfinalproject.repository.pofile.ProfileRepository;
import org.ebndrnk.leverxfinalproject.repository.rating.RatingRepository;
import org.ebndrnk.leverxfinalproject.service.anonymous.AnonymousUserService;
import org.ebndrnk.leverxfinalproject.util.exception.dto.ProfileNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final AnonymousUserService anonymousUserService;
    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;


    public ProfileResponse evaluate(RatingRequest ratingRequest, Long profileId, HttpServletRequest request) {
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile with this id not found"));

        AnonymousUser anonymousUser = anonymousUserService.getOrCreateAnonymousUser(request);

        Optional<Rating> existingRating = findPreviousMark(profileId, anonymousUser);

        if (existingRating.isPresent()) {
            Rating rating = existingRating.get();
            rating.setMark(ratingRequest.getMark());
            ratingRepository.save(rating);
        } else {
            Rating rating = new Rating();
            rating.setMark(ratingRequest.getMark());
            rating.setAuthor(anonymousUser);
            rating.setSeller(profile);
            ratingRepository.save(rating);
        }

        profile.setRating(calculateRating(profileId));
        return modelMapper.map(profileRepository.save(profile), ProfileResponse.class);
    }


    private byte calculateRating(Long profileId) {
        List<Rating> ratings = ratingRepository.findAllBySeller_Id(profileId);
        if (ratings.isEmpty()) {
            return 0;
        }

        int total = ratings.stream().mapToInt(Rating::getMark).sum();
        return (byte) (total / ratings.size());
    }


    private Optional<Rating> findPreviousMark(Long profileId, AnonymousUser anonymousUser) {
        return ratingRepository.findAllBySeller_Id(profileId).stream()
                .filter(rating -> rating.getAuthor().getAnonymousId().equals(anonymousUser.getAnonymousId()))
                .findFirst();
    }
}
