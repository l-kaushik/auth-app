package in.lokeshkaushik.authapp.configs;

import in.lokeshkaushik.authapp.dtos.ApiError;
import in.lokeshkaushik.authapp.security.JwtAuthenticationFilter;
import in.lokeshkaushik.authapp.security.OAuth2SuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final AuthenticationSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){

        http.csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(
                authorizeHttpRequest ->
                        authorizeHttpRequest
                                .requestMatchers(AppConstants.AUTH_PUBLIC_URLS).permitAll()
                                .requestMatchers(HttpMethod.GET).hasRole(AppConstants.GUEST_ROLE)
                                .requestMatchers("/api/v1/users/**").hasRole(AppConstants.ADMIN_ROLE)
                                .anyRequest().authenticated()
                )

                // NOTE: Frontend will access through this url http://localhost:8080/oauth2/authorization/google
                
                .oauth2Login(oauth2 ->
                        oauth2.successHandler(successHandler)
                                .failureHandler(null)
                )
                .logout(AbstractHttpConfigurer::disable)
                .exceptionHandling( ex -> ex.authenticationEntryPoint((request, response, authenticationException) -> {
                    filterChainExceptionHandler(401, authenticationException, request, response, HttpStatus.UNAUTHORIZED.toString());
                }).accessDeniedHandler((request, response, accessDeniedException) -> {
                            filterChainExceptionHandler(403, accessDeniedException, request, response, HttpStatus.FORBIDDEN.toString());
                        })
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private void filterChainExceptionHandler(int status, Exception ex, HttpServletRequest request, HttpServletResponse response, String httpStatus) throws IOException {
        logger.error(ex.getMessage());
        response.setStatus(status);
        response.setContentType("application/json");
        String error = request.getAttribute("error") != null
                ? request.getAttribute("error").toString()
                : null;
        String message = error != null ? error : ex.getMessage();
        System.out.println(message);
        var objectMapper = new ObjectMapper();
        var apiError = ApiError.of(httpStatus, message, request.getRequestURI());
        response.getWriter().write(objectMapper.writeValueAsString(apiError));
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
