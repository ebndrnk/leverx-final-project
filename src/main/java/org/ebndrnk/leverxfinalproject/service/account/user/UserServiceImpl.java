package org.ebndrnk.leverxfinalproject.service.account.user;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.auth.UserDto;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.auth.UserPrincipalImpl;
import org.ebndrnk.leverxfinalproject.repository.auth.UserRepository;
import org.ebndrnk.leverxfinalproject.util.exception.dto.EmailAlreadyExistsException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UserNotFoundException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UsernameAlreadyExistsException;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * UserService
 * <p>
 * This service provides operations for managing users, such as saving,
 * creating new users with uniqueness checks, retrieving user details for Spring Security,
 * and fetching the currently authenticated user.
 */
@Service
@Primary
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * Saves the given user.
     *
     * @param userDto the user entity to be saved
     * @return the saved user entity
     */
    @Override
    public UserDto save(UserDto userDto) {
        return modelMapper.map(userRepository.save(modelMapper.map(userDto, User.class)), UserDto.class);
    }


    @Override
    public UserDto getById(Long userId) {
        return modelMapper.map(userRepository.findById(userId), UserDto.class);
    }

    /**
     * Creates a new user after checking for unique username and email.
     *
     * @param userDto the user entity to be created
     * @return the created user entity
     * @throws UsernameAlreadyExistsException if a user with the same username exists
     * @throws EmailAlreadyExistsException if a user with the same email exists
     */
    public UserDto create(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UsernameAlreadyExistsException("A user with the same name already exists");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new EmailAlreadyExistsException("A user with this email already exists");
        }

        return save(userDto);
    }

    /**
     * Retrieves a user by username and wraps it in a UserPrincipalImpl.
     *
     * @param username the username to search for
     * @return a UserPrincipalImpl representing the found user
     * @throws UserNotFoundException if the user is not found
     */
    @Override
    public UserPrincipalImpl getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User is not found"));
        return new UserPrincipalImpl(user);
    }

    /**
     * Provides a UserDetailsService implementation required by Spring Security.
     * This method delegates the retrieval of user details by username.
     *
     * @return a UserDetailsService implementation
     */
    @Override
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @return the user entity of the currently authenticated user
     * @throws UserNotFoundException if the current user cannot be found
     */
    @Override
    public UserDto getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return modelMapper.map(userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username)), UserDto.class);
    }

    @Override
    public UserDto getByUserEmail(String email) {
        return modelMapper.map(userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email: " + email + " not found")), UserDto.class);
    }

    @Override
    public boolean isEmailConfirmed(String username){
        return userRepository.isEmailConfirmed(username);
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User with this email not found"));
    }

}
