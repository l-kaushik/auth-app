package in.lokeshkaushik.authapp.services;

import in.lokeshkaushik.authapp.dtos.UserDto;
import in.lokeshkaushik.authapp.entities.Provider;
import in.lokeshkaushik.authapp.entities.User;
import in.lokeshkaushik.authapp.exceptions.ResourceNotFoundException;
import in.lokeshkaushik.authapp.mapper.UserMapper;
import in.lokeshkaushik.authapp.repositories.UserRepository;
import in.lokeshkaushik.authapp.utils.UserHelper;
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
            throw new IllegalArgumentException("Email is required");

        if(userRepository.existsByEmail(userDto.email()))
            throw new IllegalArgumentException("User with given email already exists");

        User user = userMapper.toEntity(userDto);
        user.setProvider(userDto.provider() != null ? userDto.provider() : Provider.LOCAL);

        // TODO: role assign here to user for authorization

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User not found with given EmailId"));

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto userDto, String userId) {
        User existingUser = getUserObjectById(userId);

        if(userDto.name() != null) existingUser.setName(userDto.name());
        if(userDto.image() != null) existingUser.setImage(userDto.image());
        if(userDto.provider() != null) existingUser.setProvider(userDto.provider());
        // TODO: Implement with encryption
        if(userDto.password() != null && !existingUser.getPassword().equals(userDto.password())) existingUser.setPassword(userDto.password());
        existingUser.setEnable(userDto.enable());

        return userMapper.toDto(userRepository.save(existingUser));
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        User user = getUserObjectById(userId);
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = getUserObjectById(userId);
        return userMapper.toDto(user);
    }

    private User getUserObjectById(String userId) {
        return userRepository.findById(UserHelper.parseUUID(userId)).orElseThrow(
                () -> new ResourceNotFoundException("User not found with given Id")
        );
    }

    @Override
    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toDto).toList();
    }
}
