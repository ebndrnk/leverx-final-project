package org.ebndrnk.leverxfinalproject.service.rating;

import jakarta.servlet.http.HttpServletRequest;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileResponse;
import org.ebndrnk.leverxfinalproject.model.dto.rating.RatingRequest;

public interface RatingService {
    ProfileResponse evaluate(RatingRequest ratingRequest, Long profileId, HttpServletRequest request);
}
