package in.lokeshkaushik.authapp.services;

import in.lokeshkaushik.authapp.dtos.UserDto;

public interface AuthService {
    UserDto registerUser(UserDto userDto);
    // login user
}
