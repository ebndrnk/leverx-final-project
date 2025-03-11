package org.ebndrnk.leverxfinalproject.controller.profile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileResponse;
import org.ebndrnk.leverxfinalproject.model.dto.rating.RatingRequest;
import org.ebndrnk.leverxfinalproject.service.profile.ProfileService;
import org.ebndrnk.leverxfinalproject.service.rating.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;
    private final RatingService ratingService;

    /**
     * Retrieves a list of all users in the system.
     *
     * <p>
     * Returns a list of UserDto objects representing all users.
     * </p>
     *
     * @return ResponseEntity containing the list of all users.
     */
    @Operation(summary = "Get all users", description = "Returns a list of all users in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @GetMapping()
    public ResponseEntity<List<ProfileResponse>> getAllProfiles() {
        return ResponseEntity.ok(profileService.getAll());
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<ProfileResponse> getProfileById(@PathVariable(name = "profileId") Long profileId) {
        return ResponseEntity.ok(profileService.getProfileResponseById(profileId));
    }

    @PostMapping("/{profileId}/rating")
    public ResponseEntity<ProfileResponse> evaluate(@RequestBody RatingRequest request,
                                                    @PathVariable(name = "profileId") Long profileId,
                                                    HttpServletRequest httpServletRequest){
        return ResponseEntity.ok(ratingService.evaluate(request, profileId, httpServletRequest));
    }

    @GetMapping("/top")
    public ResponseEntity<List<ProfileResponse>> getTopBestSellers(@RequestParam(name = "count", defaultValue = "10") int count){
        return ResponseEntity.ok(profileService.getTopSellers(count));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProfileResponse>> searchProfilesByRating(
            @RequestParam(required = false) Byte minRating,
            @RequestParam(required = false) Byte maxRating) {

        return ResponseEntity.ok(profileService.findProfilesByRating(minRating, maxRating));
    }

}
