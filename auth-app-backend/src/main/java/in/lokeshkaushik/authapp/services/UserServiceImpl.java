package in.lokeshkaushik.authapp.services;

import in.lokeshkaushik.authapp.dtos.UserDto;
import in.lokeshkaushik.authapp.entities.Provider;
import in.lokeshkaushik.authapp.entities.User;
import in.lokeshkaushik.authapp.mapper.UserMapper;
import in.lokeshkaushik.authapp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {

        if(userDto.email() == null || userDto.email().isBlank())
            throw new IllegalArgumentException("Invalid email provided");

        if(userRepository.existsByEmail(userDto.email()))
            throw new IllegalArgumentException("Email already exists");

        User user = userMapper.toEntity(userDto);
        user.setProvider(userDto.provider() != null ? userDto.provider() : Provider.LOCAL);

        // TODO: role assign here to user for authorization

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        return null;
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        return null;
    }

    @Override
    public void deleteUser(String userId) {

    }

    @Override
    public UserDto getUserById(String userId) {
        return null;
    }

    @Override
    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }
}
