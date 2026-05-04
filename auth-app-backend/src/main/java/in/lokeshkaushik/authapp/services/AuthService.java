package in.lokeshkaushik.authapp.services;

import in.lokeshkaushik.authapp.dtos.LoginRequest;
import in.lokeshkaushik.authapp.dtos.RefreshTokenRequest;
import in.lokeshkaushik.authapp.dtos.TokenResponse;
import in.lokeshkaushik.authapp.dtos.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    UserDto registerUser(UserDto userDto);
    TokenResponse loginUser(LoginRequest loginRequest, HttpServletResponse response);
    TokenResponse refreshToken(RefreshTokenRequest body, HttpServletRequest request, HttpServletResponse response);
    ResponseEntity<Object> logout(HttpServletRequest request, HttpServletResponse response);
}
