package in.lokeshkaushik.authapp.services.impl;

import in.lokeshkaushik.authapp.dtos.LoginRequest;
import in.lokeshkaushik.authapp.dtos.TokenResponse;
import in.lokeshkaushik.authapp.dtos.UserDto;
import in.lokeshkaushik.authapp.entities.RefreshToken;
import in.lokeshkaushik.authapp.entities.User;
import in.lokeshkaushik.authapp.mapper.UserMapper;
import in.lokeshkaushik.authapp.repositories.RefreshTokenRepository;
import in.lokeshkaushik.authapp.repositories.UserRepository;
import in.lokeshkaushik.authapp.security.CookieService;
import in.lokeshkaushik.authapp.security.JwtService;
import in.lokeshkaushik.authapp.services.AuthService;
import in.lokeshkaushik.authapp.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieService cookieService;

    @Override
    public UserDto registerUser(UserDto userDto) {

        // verify email
        // password verification

        // default roles

        return userService.createUser(userDto);
    }

    public TokenResponse loginUser(LoginRequest loginRequest, HttpServletResponse response) {
        Authentication authenticate =  authenticate(loginRequest);
        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(
                () -> new BadCredentialsException("Invalid Username or Password")
        );
        if(!user.isEnable()) {
            throw new DisabledException("User is disabled");
        }

        String jti = UUID.randomUUID().toString();
        var refreshTokenObject = RefreshToken.builder()
                .jti(jti)
                .user(user)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(jwtService.getRefreshTtlSeconds()))
                .revoked(false)
                .build();

        RefreshToken saved = refreshTokenRepository.save(refreshTokenObject);

        // generate token
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(refreshTokenObject.getUser(), refreshTokenObject.getJti());

        // use cookie service to attach refresh token in cookie
        cookieService.attachRefreshCookie(response, refreshToken, (int) jwtService.getRefreshTtlSeconds());
        cookieService.addNoStoreHeaders(response);

        return TokenResponse.of(accessToken, refreshToken, jwtService.getAccessTtlSeconds(), userMapper.toDto(user));
    }

    private Authentication authenticate(LoginRequest loginRequest) {
        try{
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }
}
