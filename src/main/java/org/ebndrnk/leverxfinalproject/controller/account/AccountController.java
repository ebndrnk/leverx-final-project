package org.ebndrnk.leverxfinalproject.controller.account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.auth.JwtAuthenticationResponse;
import org.ebndrnk.leverxfinalproject.model.dto.auth.RegistrationResponse;
import org.ebndrnk.leverxfinalproject.model.dto.auth.SignInRequest;
import org.ebndrnk.leverxfinalproject.model.dto.auth.SignUpRequest;
import org.ebndrnk.leverxfinalproject.service.account.AccountService;
import org.ebndrnk.leverxfinalproject.service.account.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication Controller
 *
 * <p>
 * This controller handles authentication-related operations, such as user registration (sign-up) and login (sign-in).
 * It provides endpoints to create new user accounts and generate JWT tokens for authentication.
 * </p>
 *
 * <p>
 * All authentication logic is delegated to {@link AuthenticationService}.
 * </p>
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and authentication")
public class AccountController {

    private final AccountService accountService;

    /**
     * Register a new user.
     *
     * <p>
     * This endpoint creates a new user account and sends a confirmation email.
     * </p>
     *
     * @param request the sign-up request containing user details.
     * @return a response containing a welcome message and a note to check the email for verification.
     */
    @PostMapping("/sign-up")
    @Operation(summary = "Register a new user",
            description = "Creates a new user account and returns a registration response with confirmation details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
    })
    public ResponseEntity<RegistrationResponse> signUp(@RequestBody @Valid SignUpRequest request) {
        return ResponseEntity.ok(accountService.signUp(request));
    }

    /**
     * Authenticate an existing user.
     *
     * <p>
     * This endpoint verifies user credentials and returns a JWT token upon successful authentication.
     * </p>
     *
     * @param request the sign-in request containing user credentials (email and password).
     * @return a JWT authentication response containing the access token and additional user information.
     */
    @PostMapping("/sign-in")
    @Operation(summary = "Authenticate an existing user",
            description = "Authenticates the user and returns a JWT token upon successful login.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials or unconfirmed email"),
            @ApiResponse(responseCode = "404", description = "User not found"),
    })
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return accountService.signIn(request);
    }
}
