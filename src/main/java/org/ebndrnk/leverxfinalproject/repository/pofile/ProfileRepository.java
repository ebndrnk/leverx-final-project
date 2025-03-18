package org.ebndrnk.leverxfinalproject.repository.pofile;

import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.ebndrnk.leverxfinalproject.model.projection.ProfilePreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long>, JpaSpecificationExecutor<Profile> {
    Optional<Profile> findByUsername(String username);
    Optional<Profile> findByEmail(String email);

    @Query("SELECT p from Profile p where p.isConfirmedByAdmin = false")
    List<Profile> findAllNotConfirmedProfiles();

    @Query("SELECT p from Profile p where p.isConfirmedByAdmin = true")
    List<Profile> findAllConfirmedProfiles();

    @Query("SELECT p FROM Profile p "
            + "LEFT JOIN FETCH p.comment "
            + "WHERE p.rating IS NOT NULL "
            + "ORDER BY p.rating DESC")
    List<Profile> findTopSellersWithLimit(Pageable pageable);





    @Query("SELECT p.username AS username," +
            " p.firstname AS firstname," +
            " p.lastname AS lastname," +
            " p.email AS email," +
            "p.isConfirmedByAdmin AS confirmedByAdmin,"+
            "p.rating AS rating,"+
            "p.id AS id"+
            " FROM Profile p")
    Page<ProfilePreview> findAllProjectedProfiles(Pageable pageable);

}
