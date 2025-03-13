package org.ebndrnk.leverxfinalproject.service.rating;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling rating functionality.
 */
@Service
@Slf4j
@Primary
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {


    private final RatingRepository ratingRepository;
    private final AnonymousUserService anonymousUserService;
    private final ProfileRepository profileRepository;
    private final ModelMapper modelMapper;

    /**
     * Evaluate and update the rating for a specific profile by an anonymous user.
     * If the user has already rated the profile, it updates the rating.
     * Otherwise, it creates a new rating.
     *
     * @param ratingRequest The rating details from the user.
     * @param profileId     The ID of the profile being rated.
     * @param request       The HttpServletRequest object to retrieve the anonymous user.
     * @return The updated profile response with the new rating.
     */
    @Override
    public ProfileResponse evaluate(RatingRequest ratingRequest, Long profileId, HttpServletRequest request) {
        // Fetch profile by ID, or throw an exception if not found
        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile with this id not found"));

        AnonymousUser anonymousUser = anonymousUserService.getOrCreateAnonymousUser(request);

        Optional<Rating> existingRating = findPreviousMark(profileId, anonymousUser);

        if (existingRating.isPresent()) {
            Rating rating = existingRating.get();
            rating.setMark(ratingRequest.getMark());
            ratingRepository.save(rating);
            log.info("Updated rating for profileId {} by anonymousUserId {}", profileId, anonymousUser.getAnonymousId());
        } else {
            Rating rating = new Rating();
            rating.setMark(ratingRequest.getMark());
            rating.setAuthor(anonymousUser);
            rating.setSeller(profile);
            ratingRepository.save(rating);
            log.info("Created new rating for profileId {} by anonymousUserId {}", profileId, anonymousUser.getAnonymousId());
        }

        profile.setRating(calculateRating(profileId));

        ProfileResponse profileResponse = modelMapper.map(profileRepository.save(profile), ProfileResponse.class);
        log.info("ProfileId {} rating updated to {}", profileId, profile.getRating());
        return profileResponse;
    }

    /**
     * Calculates the average rating of a profile based on the ratings given by users.
     *
     * @param profileId The ID of the profile for which the rating is to be calculated.
     * @return The calculated average rating as a byte value.
     */
    private byte calculateRating(Long profileId) {
        List<Rating> ratings = ratingRepository.findAllBySeller_Id(profileId);
        if (ratings.isEmpty()) {
            log.warn("No ratings found for profileId {}", profileId);
            return 0;
        }

        int total = ratings.stream().mapToInt(Rating::getMark).sum();
        byte averageRating = (byte) (total / ratings.size());
        log.info("Calculated average rating for profileId {}: {}", profileId, averageRating);
        return averageRating;
    }

    /**
     * Finds a previous rating by the anonymous user for the given profile.
     *
     * @param profileId The profile ID.
     * @param anonymousUser The anonymous user who might have already rated the profile.
     * @return An Optional containing the existing rating if found.
     */
    private Optional<Rating> findPreviousMark(Long profileId, AnonymousUser anonymousUser) {
        return ratingRepository.findAllBySeller_Id(profileId).stream()
                .filter(rating -> rating.getAuthor().getAnonymousId().equals(anonymousUser.getAnonymousId()))
                .findFirst();
    }
}
