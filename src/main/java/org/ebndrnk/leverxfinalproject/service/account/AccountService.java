package org.ebndrnk.leverxfinalproject.service.account;

import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.model.dto.auth.JwtAuthenticationResponse;
import org.ebndrnk.leverxfinalproject.model.dto.auth.RegistrationResponse;
import org.ebndrnk.leverxfinalproject.model.dto.auth.SignInRequest;
import org.ebndrnk.leverxfinalproject.model.dto.auth.SignUpRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;

    public JwtAuthenticationResponse signIn(SignInRequest request){
        return authenticationService.signIn(request);
    }

    public RegistrationResponse signUp(SignUpRequest request){
        return registrationService.signUp(request);
    }
}
