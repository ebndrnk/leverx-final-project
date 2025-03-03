package org.ebndrnk.leverxfinalproject.service.auth;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.entity.auth.Role;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.auth.UserPrincipalImpl;
import org.ebndrnk.leverxfinalproject.model.dto.auth.JwtAuthenticationResponse;
import org.ebndrnk.leverxfinalproject.model.dto.auth.SignInRequest;
import org.ebndrnk.leverxfinalproject.model.dto.auth.SignUpRequest;
import org.ebndrnk.leverxfinalproject.repository.auth.UserRepository;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * AuthenticationService
 *
 * This service handles user registration and authentication.
 * It leverages UserService, JwtService, PasswordEncoder, and AuthenticationManager
 * to create new users and authenticate existing ones, returning a JWT token upon success.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    /**
     * Registers a new user and returns a JWT authentication response.
     *
     * @param request SignUpRequest containing the user registration data.
     * @return JwtAuthenticationResponse containing the generated JWT token.
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        log.info("Attempting to register user with email: {}", request.getEmail());

        var user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_SELLER);

        userService.create(user);
        log.info("User registered successfully with email: {}", user.getEmail());

        UserPrincipalImpl userPrincipal = new UserPrincipalImpl(user);
        var jwt = jwtService.generateToken(userPrincipal);
        log.info("JWT token generated for user: {}", user.getEmail());

        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Authenticates an existing user and returns a JWT authentication response.
     *
     * @param request SignInRequest containing the user login credentials.
     * @return JwtAuthenticationResponse containing the generated JWT token.
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        log.info("Attempting to authenticate user with email: {}", request.getEmail());

        User userdb = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User with this email was not found: " + request.getEmail()));

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userdb.getUsername(),
                    request.getPassword()
            ));
            log.info("Authentication successful for user: {}", userdb.getUsername());
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for user: {} due to bad credentials", userdb.getUsername());
            throw new BadCredentialsException("Bad credentials!");
        }

        var user = userService.userDetailsService().loadUserByUsername(userdb.getUsername());
        var jwt = jwtService.generateToken(user);
        log.info("JWT token generated for user: {}", userdb.getUsername());

        return new JwtAuthenticationResponse(jwt);
    }
}
