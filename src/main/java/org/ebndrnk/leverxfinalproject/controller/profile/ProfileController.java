package org.ebndrnk.leverxfinalproject.controller.profile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
    public ResponseEntity<Page<?>> getAllProfiles(
            @Parameter(description = "If true, returns a projection of user data instead of full profiles")
            @RequestParam(required = false) boolean projection,
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
    @Operation(summary = "Get user profile by ID", description = "Retrieves a full user profile by its unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User profile not found")
    })
    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileResponse> getProfileById(
            @Parameter(description = "Unique ID of the user profile")
            @PathVariable(name = "profileId") Long profileId) {
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
    @Operation(summary = "Rate a user profile", description = "Allows users to rate a profile by sending a rating request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile rated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid rating request")
    })
    @PostMapping("/{profileId}/rating")
    public ResponseEntity<ProfileResponse> evaluate(
            @Valid @RequestBody RatingRequest request,
            @Parameter(description = "Unique ID of the profile to be rated")
            @PathVariable(name = "profileId") Long profileId,
            HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(ratingService.evaluate(request, profileId, httpServletRequest));
    }

    /**
     * Retrieves a list of top sellers from the cache.
     * <p>
     * This method returns a list of users with the highest ratings (by sales),
     * limited by the specified count. The default count is 10, and these results
     * are fetched from Redis cache for performance optimization.
     * </p>
     *
     * @param count the maximum number of top sellers to return (default is 10).
     * @return ResponseEntity containing the list of top sellers.
     */
    @Operation(summary = "Get top-rated sellers", description = "Returns a list of the highest-rated sellers, fetched from Redis cache")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Top sellers retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Error retrieving top sellers")
    })
    @GetMapping("/top")
    public ResponseEntity<Page<ProfileResponse>> getTopBestSellers(
            @Parameter(description = "Maximum number of top sellers to retrieve (default is 10)")
            @RequestParam(name = "count", defaultValue = "10") int count){
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
    @Operation(summary = "Search profiles by rating", description = "Filters profiles based on minimum and maximum rating values")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profiles matching the criteria retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid rating filter range")
    })
    @GetMapping("/search")
    public ResponseEntity<Page<ProfileResponse>> searchProfilesByRating(
            @Parameter(description = "Minimum rating for filtering")
            @RequestParam(required = false) Byte minRating,
            @Parameter(description = "Maximum rating for filtering")
            @RequestParam(required = false) Byte maxRating,
            Pageable pageable) {

        return ResponseEntity.ok(profileService.findProfilesByRating(minRating, maxRating, pageable));
    }
}
