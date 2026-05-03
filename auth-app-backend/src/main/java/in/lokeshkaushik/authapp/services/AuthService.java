package in.lokeshkaushik.authapp.services;

import in.lokeshkaushik.authapp.dtos.LoginRequest;
import in.lokeshkaushik.authapp.dtos.TokenResponse;
import in.lokeshkaushik.authapp.dtos.UserDto;

public interface AuthService {
    UserDto registerUser(UserDto userDto);
    TokenResponse loginUser(LoginRequest loginRequest);
}
