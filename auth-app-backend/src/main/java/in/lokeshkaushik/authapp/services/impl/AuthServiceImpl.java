package in.lokeshkaushik.authapp.services.impl;

import in.lokeshkaushik.authapp.dtos.UserDto;
import in.lokeshkaushik.authapp.services.AuthService;
import in.lokeshkaushik.authapp.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    @Override
    public UserDto registerUser(UserDto userDto) {

        // verify email
        // password verification

        // default roles

        return userService.createUser(userDto);
    }
}
