package org.ebndrnk.leverxfinalproject.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.ebndrnk.leverxfinalproject.service.account.user.JwtService;
import org.ebndrnk.leverxfinalproject.service.account.user.UserService;
import org.ebndrnk.leverxfinalproject.util.exception.ErrorInfo;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UserNotFoundException;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	public static final String BEARER_PREFIX = "Bearer ";
	public static final String HEADER_NAME = "Authorization";
	private final JwtService jwtService;
	private final UserService userService;
	private final ObjectMapper objectMapper;

	/**
	 * Filters incoming HTTP requests to check for a valid JWT token.
	 * If a valid token is found, the user is authenticated.
	 *
	 * @param request the incoming HTTP request
	 * @param response the outgoing HTTP response
	 * @param filterChain the filter chain
	 * @throws ServletException in case of servlet errors
	 * @throws IOException in case of I/O errors
	 */
	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,
									@NonNull HttpServletResponse response,
									@NonNull FilterChain filterChain) throws ServletException, IOException {
		// Retrieve the token from the Authorization header
		var authHeader = request.getHeader(HEADER_NAME);
		if (StringUtils.isEmpty(authHeader) || !StringUtils.startsWith(authHeader, BEARER_PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}
		// Remove the prefix and extract the username from the token
		var jwt = authHeader.substring(BEARER_PREFIX.length());
		var username = jwtService.extractUserName(jwt);
		if (username == null) {
			handleErrorResponse(response, new ErrorInfo(LocalDateTime.now(), HttpServletResponse.SC_FORBIDDEN,
					"FORBIDDEN", "token is not valid", request.getRequestURI()));
			return;
		}
		if (StringUtils.isNotEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails;
			try {
				userDetails = userService.userDetailsService().loadUserByUsername(username);
			} catch (UserNotFoundException ex) {
				handleErrorResponse(response, new ErrorInfo(LocalDateTime.now(), HttpServletResponse.SC_NOT_FOUND,
						"User Not Found", ex.getMessage(), request.getRequestURI()));
				return;
			}
			// If the token is valid, authenticate the user
			if (jwtService.isTokenValid(jwt, userDetails)) {
				SecurityContext context = SecurityContextHolder.createEmptyContext();
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
						null, userDetails.getAuthorities());
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				context.setAuthentication(authToken);
				SecurityContextHolder.setContext(context);

				if(!userService.isEmailConfirmed(username)){
					handleErrorResponse(response, new ErrorInfo(LocalDateTime.now(),
							HttpServletResponse.SC_FORBIDDEN,
							"FORBIDDEN",
							"Email is not confirmed",
							request.getRequestURI()));
					return;
				};
			} else {
				handleErrorResponse(response, new ErrorInfo(LocalDateTime.now(), HttpServletResponse.SC_FORBIDDEN,
						"FORBIDDEN", "token is not valid", request.getRequestURI()));
				return;
			}
		}
		filterChain.doFilter(request, response);
	}

	/**
	 * Writes an error response with the given ErrorInfo.
	 *
	 * @param response the HTTP response
	 * @param error the error details to be written
	 */
	private void handleErrorResponse(HttpServletResponse response, ErrorInfo error) {
		try {
			String message = objectMapper.writeValueAsString(error);
			response.getWriter().write(message);
			response.setStatus(error.getStatus());
		} catch (Exception e) {
			log.error("Could not convert error object to string or write it", e);
		}
	}
}
