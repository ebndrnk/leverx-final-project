package org.ebndrnk.leverxfinalproject.repository.rating;

import org.ebndrnk.leverxfinalproject.model.entity.rating.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllBySeller_Id(Long sellerId);
}
