package org.ebndrnk.leverxfinalproject.repository.specification;

import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.springframework.data.jpa.domain.Specification;

/**
 * This class provides specifications for querying the Profile entity.
 * It allows filtering profiles based on specific criteria such as ratings.
 */
public class ProfileSpecification {

    /**
     * Creates a Specification to filter profiles by rating range.
     * It returns profiles whose ratings are within the specified range.
     * If either minRating or maxRating is null, the filter will adjust accordingly:
     * - If minRating is null, it filters by less than or equal to maxRating.
     * - If maxRating is null, it filters by greater than or equal to minRating.
     * - If both are provided, it filters profiles with a rating between the two values.
     *
     * @param minRating The minimum rating to filter by. Can be null to disable the lower bound.
     * @param maxRating The maximum rating to filter by. Can be null to disable the upper bound.
     * @return The Specification for filtering profiles by rating.
     */
    public static Specification<Profile> hasRatingBetween(Byte minRating, Byte maxRating) {
        return (root, query, cb) -> {
            if (minRating == null && maxRating == null) {
                return null;
            }

            if (minRating == null) {
                return cb.lessThanOrEqualTo(root.get("rating"), maxRating);
            }

            if (maxRating == null) {
                return cb.greaterThanOrEqualTo(root.get("rating"), minRating);
            }

            return cb.between(root.get("rating"), minRating, maxRating);
        };
    }
}
