package in.lokeshkaushik.authapp.mapper;

import in.lokeshkaushik.authapp.dtos.UserDto;
import in.lokeshkaushik.authapp.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<User, UserDto> {
}
