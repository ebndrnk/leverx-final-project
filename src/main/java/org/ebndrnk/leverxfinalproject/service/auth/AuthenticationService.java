package org.ebndrnk.leverxfinalproject.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebndrnk.leverxfinalproject.model.dto.auth.*;
import org.ebndrnk.leverxfinalproject.model.entity.auth.Role;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.ebndrnk.leverxfinalproject.repository.auth.UserRepository;
import org.ebndrnk.leverxfinalproject.service.auth.user.JwtService;
import org.ebndrnk.leverxfinalproject.service.auth.user.UserServiceImpl;
import org.ebndrnk.leverxfinalproject.service.mail.EmailServiceImpl;
import org.ebndrnk.leverxfinalproject.util.exception.dto.NotConfirmedException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthenticationService
 * <p>
 * This service handles user registration and authentication.
 * It leverages UserService, JwtService, PasswordEncoder, and AuthenticationManager
 * to create new users and authenticate existing ones, returning a JWT token upon success.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {


    private final UserServiceImpl userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EmailServiceImpl emailService;

    /**
     * Registers a new user and returns a JWT authentication response.
     *
     * @param request SignUpRequest containing the user registration data.
     * @return JwtAuthenticationResponse containing the generated JWT token.
     */
    @Transactional
    public RegistrationResponse signUp(SignUpRequest request) {
        log.info("Attempting to register user with email: {}", request.getEmail());

        var user = modelMapper.map(request, User.class);
        var profile = modelMapper.map(request, Profile.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ROLE_SELLER);
        user.setProfile(profile);

        userService.create(modelMapper.map(user, UserDto.class));
        log.info("User registered successfully with email: {}", user.getEmail());
        try {
            emailService.sendConfirmationEmail(user.getEmail());
            log.info("Mail sent to email: {}", user.getEmail());
        }catch (MailException e){
            log.error("Mail exception: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        return new RegistrationResponse();
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

        if(!userdb.isEmailConfirmed()){
            log.info("User email not confirmed!");
            throw new NotConfirmedException("Please confirm you email");
        }
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
