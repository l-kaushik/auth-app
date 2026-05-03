package in.lokeshkaushik.authapp.services.impl;

import in.lokeshkaushik.authapp.dtos.LoginRequest;
import in.lokeshkaushik.authapp.dtos.TokenResponse;
import in.lokeshkaushik.authapp.dtos.UserDto;
import in.lokeshkaushik.authapp.entities.User;
import in.lokeshkaushik.authapp.mapper.UserMapper;
import in.lokeshkaushik.authapp.repositories.UserRepository;
import in.lokeshkaushik.authapp.security.JwtService;
import in.lokeshkaushik.authapp.services.AuthService;
import in.lokeshkaushik.authapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    public UserDto registerUser(UserDto userDto) {

        // verify email
        // password verification

        // default roles

        return userService.createUser(userDto);
    }

    public TokenResponse loginUser(LoginRequest loginRequest) {
        Authentication authenticate =  authenticate(loginRequest);
        User user = userRepository.findByEmail(loginRequest.email()).orElseThrow(
                () -> new BadCredentialsException("Invalid Username or Password")
        );
        if(!user.isEnable()) {
            throw new DisabledException("User is disabled");
        }

        // generate token
        String accessToken = jwtService.generateAccessToken(user);
        return TokenResponse.of(accessToken, "", jwtService.getAccessTtlSeconds(), userMapper.toDto(user));
    }

    private Authentication authenticate(LoginRequest loginRequest) {
        try{
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        } catch (Exception e) {
            throw new BadCredentialsException("Invalid Username or Password");
        }
    }
}
