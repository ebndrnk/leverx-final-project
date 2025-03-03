package org.ebndrnk.leverxfinalproject.service.auth;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.auth.UserPrincipalImpl;
import org.ebndrnk.leverxfinalproject.repository.auth.UserRepository;
import org.ebndrnk.leverxfinalproject.util.exception.dto.EmailAlreadyExistsException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UsernameAlreadyExistsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UserService
 *
 * This service provides operations for managing users, such as saving,
 * creating new users with uniqueness checks, retrieving user details for Spring Security,
 * and fetching the currently authenticated user.
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Saves the given user.
     *
     * @param user the user entity to be saved
     * @return the saved user entity
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * Creates a new user after checking for unique username and email.
     *
     * @param user the user entity to be created
     * @return the created user entity
     * @throws UsernameAlreadyExistsException if a user with the same username exists
     * @throws EmailAlreadyExistsException if a user with the same email exists
     */
    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyExistsException("A user with the same name already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("A user with this email already exists");
        }

        return save(user);
    }

    /**
     * Retrieves a user by username and wraps it in a UserPrincipalImpl.
     *
     * @param username the username to search for
     * @return a UserPrincipalImpl representing the found user
     * @throws UsernameNotFoundException if the user is not found
     */
    public UserPrincipalImpl getByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User is not found"));
        return new UserPrincipalImpl(user);
    }

    /**
     * Provides a UserDetailsService implementation required by Spring Security.
     * This method delegates the retrieval of user details by username.
     *
     * @return a UserDetailsService implementation
     */
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @return the user entity of the currently authenticated user
     * @throws UsernameNotFoundException if the current user cannot be found
     */
    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
