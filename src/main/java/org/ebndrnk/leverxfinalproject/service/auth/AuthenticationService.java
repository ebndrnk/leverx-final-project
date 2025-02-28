package org.ebndrnk.leverxfinalproject.service.auth;


import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.entity.auth.Role;
import org.ebndrnk.leverxfinalproject.model.entity.auth.User;
import org.ebndrnk.leverxfinalproject.model.entity.auth.UserPrincipalImpl;
import org.ebndrnk.leverxfinalproject.model.dto.auth.JwtAuthenticationResponse;
import org.ebndrnk.leverxfinalproject.model.dto.auth.SignInRequest;
import org.ebndrnk.leverxfinalproject.model.dto.auth.SignUpRequest;
import org.ebndrnk.leverxfinalproject.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    
    private final UserRepository userRepository;



    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signUp(SignUpRequest request) {

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .role(Role.ROLE_SELLER)
                .build();

        userService.create(user);
        UserPrincipalImpl userPrincipal = new UserPrincipalImpl(user);

        var jwt = jwtService.generateToken(userPrincipal);
        return new JwtAuthenticationResponse(jwt);
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponse signIn(SignInRequest request) {
    	User userdb = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new AccessDeniedException("dont find user email " + request.getEmail()));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
        		userdb.getUsername(),
                request.getPassword()
        ));
        var user = userService
                .userDetailsService()
                .loadUserByUsername(userdb.getUsername());
        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }




}
