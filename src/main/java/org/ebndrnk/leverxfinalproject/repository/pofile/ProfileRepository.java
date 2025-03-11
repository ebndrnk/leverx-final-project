package org.ebndrnk.leverxfinalproject.repository.pofile;

import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {
    Optional<Profile> findByUsername(String username);
    Optional<Profile> findByEmail(String email);

    @Query("SELECT p from Profile p where p.isConfirmedByAdmin = false")
    List<Profile> findAllNotConfirmedProfiles();

    @Query("SELECT p from Profile p where p.isConfirmedByAdmin = true")
    List<Profile> findAllConfirmedProfiles();

    @Query(value = "SELECT * FROM profile WHERE rating IS NOT NULL ORDER BY rating DESC LIMIT :limit", nativeQuery = true)
    List<Profile> findTopSellersWithLimit(@Param("limit") int limit);


}
