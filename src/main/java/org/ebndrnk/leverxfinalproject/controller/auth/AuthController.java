package org.ebndrnk.leverxfinalproject.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.auth.JwtAuthenticationResponse;
import org.ebndrnk.leverxfinalproject.model.dto.auth.RegistrationResponse;
import org.ebndrnk.leverxfinalproject.model.dto.auth.SignInRequest;
import org.ebndrnk.leverxfinalproject.model.dto.auth.SignUpRequest;
import org.ebndrnk.leverxfinalproject.service.auth.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AuthController
 *
 * This controller handles authentication operations such as user registration (sign-up)
 * and user login (sign-in). It delegates the business logic to the AuthenticationService.
 *
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication",
        description = "Endpoints for user registration and authentication")
public class AuthController {

    private final AuthenticationService authenticationService;

    /**
     * Endpoint for user registration.
     *
     * @param request the sign-up request containing user details
     * @return a Registration response containing the welcome text and advice that user should go to email
     */
    @PostMapping("/sign-up")
    @Operation(summary = "Register a new user",
            description = "Creates a new user account and returns a JWT token upon successful registration")
    public RegistrationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    /**
     * Endpoint for user login.
     *
     * @param request the sign-in request containing user credentials
     * @return a JWT authentication response containing the JWT token and additional info
     */
    @PostMapping("/sign-in")
    @Operation(summary = "Authenticate an existing user",
            description = "Authenticates the user and returns a JWT token upon successful authentication")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }
}
