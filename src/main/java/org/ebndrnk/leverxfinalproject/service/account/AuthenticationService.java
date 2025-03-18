package org.ebndrnk.leverxfinalproject.service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebndrnk.leverxfinalproject.model.dto.auth.JwtAuthenticationResponse;
import org.ebndrnk.leverxfinalproject.model.dto.auth.SignInRequest;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.service.account.user.JwtService;
import org.ebndrnk.leverxfinalproject.service.account.user.UserServiceImpl;
import org.ebndrnk.leverxfinalproject.util.exception.dto.NotConfirmedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * AuthenticationService
 * <p>
 * This service handles user authentication.
 * It leverages UserService, JwtService, PasswordEncoder, and AuthenticationManager
 * to create new users and authenticate existing ones, returning a JWT token upon success.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {


    private final UserServiceImpl userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserServiceImpl userServiceImpl;


    /**
     * Authenticates an existing user and returns a JWT authentication response.
     *
     * @param request SignInRequest containing the user login credentials.
     * @return JwtAuthenticationResponse containing the generated JWT token.
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        log.info("Attempting to authenticate user with email: {}", request.getEmail());


        User userdb = userServiceImpl.findByEmail(request.getEmail());

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
