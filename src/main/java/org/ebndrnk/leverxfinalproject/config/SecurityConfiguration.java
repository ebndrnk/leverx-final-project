package org.ebndrnk.leverxfinalproject.config;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.ebndrnk.leverxfinalproject.filter.JwtAuthenticationFilter;
import org.ebndrnk.leverxfinalproject.service.auth.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * SecurityConfiguration
 *
 * This configuration class sets up Spring Security for the application.
 * It configures the security filter chain to disable CSRF, sets up CORS with permissive rules,
 * defines endpoint authorization rules, and establishes stateless session management.
 * Additionally, it configures the authentication provider using a custom UserDetailsService,
 * registers a BCrypt password encoder, and exposes an authentication manager bean.
 * A JWT authentication filter is also added to validate JWT tokens in incoming requests.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final UserServiceImpl userService;

	/**
	 * Configures the security filter chain for HTTP security.
	 *
	 * @param http the HttpSecurity object to configure
	 * @return the built SecurityFilterChain instance
	 * @throws Exception if an error occurs during configuration
	 */
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.cors(cors -> cors.configurationSource(request -> {
					var corsConfiguration = new CorsConfiguration();
					corsConfiguration.setAllowedOriginPatterns(List.of("*"));
					corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
					corsConfiguration.setAllowedHeaders(List.of("*"));
					corsConfiguration.setAllowCredentials(true);
					return corsConfiguration;
				}))
				.authorizeHttpRequests(request -> request
						.requestMatchers("/auth/**").permitAll()
						.requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/comment/**").permitAll()
						.requestMatchers("/verify", "/verify/**").permitAll()
						.requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
						.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
						.anyRequest().authenticated()
				)
				.exceptionHandling(Customizer.withDefaults())
				.sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	/**
	 * Creates a BCryptPasswordEncoder bean for password encoding.
	 *
	 * @return a PasswordEncoder instance using BCrypt
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Configures the AuthenticationProvider using a DAO-based approach with a custom UserDetailsService.
	 *
	 * @return the configured AuthenticationProvider
	 */
	@Bean
	AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userService.userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	/**
	 * Exposes the AuthenticationManager bean.
	 *
	 * @param config the AuthenticationConfiguration to obtain the manager from
	 * @return the AuthenticationManager instance
	 * @throws Exception if an error occurs during retrieval
	 */
	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
}
