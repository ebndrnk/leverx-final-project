package org.ebndrnk.leverxfinalproject.repository.specification;

import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.springframework.data.jpa.domain.Specification;

public class ProfileSpecification {

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
