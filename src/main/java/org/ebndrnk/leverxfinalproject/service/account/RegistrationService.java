package org.ebndrnk.leverxfinalproject.service.account;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ebndrnk.leverxfinalproject.model.dto.auth.RegistrationResponse;
import org.ebndrnk.leverxfinalproject.model.dto.auth.SignUpRequest;
import org.ebndrnk.leverxfinalproject.model.dto.auth.UserDto;
import org.ebndrnk.leverxfinalproject.model.entity.auth.Role;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.profile.Profile;
import org.ebndrnk.leverxfinalproject.service.account.user.UserServiceImpl;
import org.ebndrnk.leverxfinalproject.service.mail.EmailServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthenticationService
 * <p>
 * This service handles user registration.
 * It leverages UserService, JwtService, PasswordEncoder, and AuthenticationManager
 * to create new users and authenticate existing ones, returning a JWT token upon success.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class RegistrationService {

    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
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


}
