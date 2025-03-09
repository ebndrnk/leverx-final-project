package org.ebndrnk.leverxfinalproject.controller.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.service.auth.verify.VerifyEmailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for handling user verification requests.
 * <p>
 * This controller exposes an endpoint to verify a user's email using a verification code.
 * </p>
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/verify")
@Tag(name = "Verify",
        description = "Endpoints for verify user email")
public class VerifyController {
    private final VerifyEmailService verificationService;

    /**
     * Verifies a user's email by comparing the provided verification code with the expected value.
     * <p>
     * When a POST request is made to the /verify endpoint, this method checks whether the provided
     * verification code matches the one stored for the given email.
     * </p>
     *
     * @param email      The email address of the user to be verified.
     * @param verifyCode The verification code provided by the user.
     * @return A view name indicating the outcome of the verification:
     *         <ul>
     *             <li>"verifySuccess" - if the verification is successful.</li>
     *             <li>"errorSuccess" - if the verification fails.</li>
     *         </ul>
     *
     */
    @PostMapping
    @Operation(summary = "Verifying user", description = "Verifying user by the email")
    public String verify(@RequestParam(name = "email") String email,
                         @RequestParam(name = "verifyCode") String verifyCode) {
        return verificationService.verify(email, verifyCode) ? "verifySuccess" : "errorSuccess";
    }
}
