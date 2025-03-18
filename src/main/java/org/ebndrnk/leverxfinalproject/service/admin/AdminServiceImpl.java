package org.ebndrnk.leverxfinalproject.service.admin;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.profile.ProfileResponse;
import org.ebndrnk.leverxfinalproject.model.entity.auth.Role;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.ebndrnk.leverxfinalproject.repository.auth.UserRepository;
import org.ebndrnk.leverxfinalproject.repository.comment.CommentRepository;
import org.ebndrnk.leverxfinalproject.repository.game.GameRepository;
import org.ebndrnk.leverxfinalproject.repository.pofile.ProfileRepository;
import org.ebndrnk.leverxfinalproject.exception.dto.NoAuthorityForActionException;
import org.ebndrnk.leverxfinalproject.exception.dto.ProfileNotFoundException;
import org.ebndrnk.leverxfinalproject.exception.dto.UserNotFoundException;
import org.ebndrnk.leverxfinalproject.repository.rating.RatingRepository;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for administrative user operations.
 *
 * <p>
 * This service provides methods for retrieving users, confirming individual users or all unconfirmed users,
 * canceling a user's confirmation, and confirming a user's email. It interacts with the UserRepository to
 * fetch and persist user data and utilizes ModelMapper for converting between User entities and UserDto objects.
 * </p>
 */
@Service
@Primary
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ProfileRepository profileRepository;
    private final GameRepository gameRepository;
    private final CommentRepository commentRepository;
    private final RatingRepository ratingRepository;

    /**
     * Retrieves all users from the repository.
     *
     * <p>
     * Returns a list of UserDto objects representing every user in the system.
     * </p>
     *
     * @return a list of all users as UserDto objects.
     */
    @Override
    public List<ProfileResponse> getAll() {
        return profileRepository.findAll()
                .stream()
                .map(profile -> modelMapper.map(profile, ProfileResponse.class))
                .toList();
    }

    /**
     * Retrieves all users that have not been confirmed by an administrator.
     *
     * <p>
     * Returns a list of UserDto objects for users whose admin confirmation flag is false.
     * </p>
     *
     * @return a list of unconfirmed users as UserDto objects.
     */
    @Override
    public List<ProfileResponse> getAllNotConfirmedByAdminUsers() {
        return profileRepository.findAllNotConfirmedProfiles()
                .stream()
                .map(profile -> modelMapper.map(profile, ProfileResponse.class))
                .toList();
    }

    /**
     * Confirms an individual user by setting the admin confirmation flag to true.
     *
     * <p>
     * Searches for the user by the given ID, updates the confirmation flag, persists the change,
     * and returns the updated UserDto.
     * </p>
     *
     * @param userId the identifier of the user to be confirmed.
     * @return the updated UserDto.
     * @throws UserNotFoundException if no user with the specified ID is found.
     */
    @Override
    public ProfileResponse confirmUserByAdmin(Long userId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Profile with this id not found"));
        profile.setConfirmedByAdmin(true);
        profileRepository.save(profile);

        return modelMapper.map(profile, ProfileResponse.class);
    }

    /**
     * Confirms all users that have not been confirmed by an administrator.
     *
     * <p>
     * Retrieves all users with the admin confirmation flag set to false, updates the flag to true,
     * saves the changes in bulk, and returns a list of the updated UserDto objects.
     * </p>
     *
     * @return a list of confirmed users as UserDto objects.
     */
    @Override
    public List<ProfileResponse> confirmAllUsers() {
        List<Profile> profileList = profileRepository.findAllNotConfirmedProfiles();
        profileList.forEach(profile -> profile.setConfirmedByAdmin(true));
        profileRepository.saveAll(profileList);
        return profileList.stream()
                .map(profile -> modelMapper.map(profile, ProfileResponse.class))
                .toList();
    }

    /**
     * Cancels the administrator's confirmation for a user by setting the confirmation flag to false.
     *
     * <p>
     * Searches for the user by the given ID, updates the confirmation flag to false, persists the change,
     * and returns the updated UserDto.
     * </p>
     *
     * @param userId the identifier of the user whose confirmation is to be cancelled.
     * @return the updated UserDto.
     * @throws UserNotFoundException if no user with the specified ID is found.
     */
    @Override
    public ProfileResponse cancelAdminConfirmation(Long userId) {
        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Profile with this id not found"));
        profile.setConfirmedByAdmin(false);
        profileRepository.save(profile);

        return modelMapper.map(profile, ProfileResponse.class);
    }

    /**
     * Confirms a user's email by setting the email confirmation flag to true.
     *
     * <p>
     * Searches for the user by their email address, updates the email confirmation flag,
     * and persists the change.
     * </p>
     *
     * @param email the email address of the user to be confirmed.
     * @throws UserNotFoundException if no user with the specified email is found.
     */
    @Override
    public void confirmEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with this email not found"));
        user.setEmailConfirmed(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {

        Profile profile = profileRepository.findById(userId)
                .orElseThrow(() -> new ProfileNotFoundException("Profile with this email not found"));

        User user = userRepository.findByEmail(profile.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with this email not found"));

        if (user.getRole() == Role.ROLE_ADMIN){
            throw new NoAuthorityForActionException("Admin user cannot be deleted");
        }

        ratingRepository.deleteAll(ratingRepository.findAllBySeller_Id(userId));
        gameRepository.deleteAll(profile.getGameObjects());
        commentRepository.deleteAll(profile.getComment());
        profileRepository.delete(profile);
        userRepository.delete(user);
    }
}
