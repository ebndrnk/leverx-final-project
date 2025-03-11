package org.ebndrnk.leverxfinalproject.repository.rating;

import org.ebndrnk.leverxfinalproject.model.entity.rating.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllBySeller_Id(Long sellerId);
}
