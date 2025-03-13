package org.ebndrnk.leverxfinalproject.controller.profile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileResponse;
import org.ebndrnk.leverxfinalproject.model.dto.rating.RatingRequest;
import org.ebndrnk.leverxfinalproject.service.profile.ProfileService;
import org.ebndrnk.leverxfinalproject.service.rating.RatingService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
/**
 * Controller for handling user profile operations.
 * <p>
 * This controller provides REST API endpoints for retrieving all users,
 * fetching individual user profiles, rating a profile, getting top sellers,
 * and searching profiles based on rating.
 * </p>
 */
@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final RatingService ratingService;

    /**
     * Retrieves a list of all users in the system.
     * <p>
     * This method returns a list of all users, with the option to filter using projections.
     * The result is paginated.
     * </p>
     *
     * @param projection a flag to select a projection if only part of the data is needed.
     * @param pageable pagination for the response data.
     * @return ResponseEntity containing the list of users.
     */
    @Operation(summary = "Get all users", description = "Returns a list of all users or a projection if requested")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping()
    public ResponseEntity<Page<?>> getAllProfiles(@RequestParam(required = false) boolean projection,
                                                  Pageable pageable) {
        return ResponseEntity.ok(profileService.getAll(projection, pageable));
    }

    /**
     * Retrieves a user profile by its ID.
     * <p>
     * This method returns the full profile of a user by their unique ID.
     * </p>
     *
     * @param profileId the unique ID of the user profile.
     * @return ResponseEntity containing the user profile data.
     */
    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileResponse> getProfileById(@PathVariable(name = "profileId") Long profileId) {
        return ResponseEntity.ok(profileService.getProfileResponseById(profileId));
    }

    /**
     * Allows a user to rate a profile.
     * <p>
     * This method allows rating a user profile by sending a rating request.
     * </p>
     *
     * @param request the request containing rating data.
     * @param profileId the unique ID of the profile being rated.
     * @param httpServletRequest the request object to get data about the current user.
     * @return ResponseEntity containing the updated profile data after rating.
     */
    @PostMapping("/{profileId}/rating")
    public ResponseEntity<ProfileResponse> evaluate(@Valid @RequestBody RatingRequest request,
                                                    @PathVariable(name = "profileId") Long profileId,
                                                    HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(ratingService.evaluate(request, profileId, httpServletRequest));
    }

    /**
     * Retrieves a list of top sellers.
     * <p>
     * This method returns a list of users with the highest ratings (by sales),
     * limited by the specified count. The result is paginated.
     * </p>
     *
     * @param count the maximum number of top sellers to return.
     * @return ResponseEntity containing the list of top sellers.
     */
    @GetMapping("/top")
    public ResponseEntity<Page<ProfileResponse>> getTopBestSellers(@RequestParam(name = "count", defaultValue = "10") int count){
        return ResponseEntity.ok(profileService.getTopSellers(count));
    }

    /**
     * Searches for user profiles by rating.
     * <p>
     * This method allows searching for user profiles filtered by minimum and maximum rating.
     * </p>
     *
     * @param minRating the minimum rating for filtering.
     * @param maxRating the maximum rating for filtering.
     * @param pageable pagination for the response data.
     * @return ResponseEntity containing the list of user profiles matching the criteria.
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProfileResponse>> searchProfilesByRating(
            @RequestParam(required = false) Byte minRating,
            @RequestParam(required = false) Byte maxRating,
            Pageable pageable) {

        return ResponseEntity.ok(profileService.findProfilesByRating(minRating, maxRating, pageable));
    }
}
