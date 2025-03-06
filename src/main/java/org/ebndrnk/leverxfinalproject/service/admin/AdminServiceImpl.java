package org.ebndrnk.leverxfinalproject.service.admin;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.auth.UserDto;
import org.ebndrnk.leverxfinalproject.model.dto.auth.UserResponse;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.repository.auth.UserRepository;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

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
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
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
    public List<UserResponse> getAllNotConfirmedByAdminUsers() {
        return userRepository.findAllNotConfirmedByAdminUsers()
                .stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
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
    public UserResponse confirmUserByAdmin(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with this id not found"));
        user.setConfirmedByAdmin(true);

        return modelMapper.map(userRepository.save(user), UserResponse.class);
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
    public List<UserResponse> confirmAllUsers() {
        List<User> userList = userRepository.findAllNotConfirmedByAdminUsers();
        userList.forEach(user -> user.setConfirmedByAdmin(true));
        userRepository.saveAll(userList);
        return userList.stream()
                .map(user -> modelMapper.map(user, UserResponse.class))
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
    public UserResponse cancelAdminConfirmation(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with this id not found"));
        user.setConfirmedByAdmin(false);

        return modelMapper.map(userRepository.save(user), UserResponse.class);
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
}
