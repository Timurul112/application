package mapper;

import dto.UserDto;
import entity.User;

public class UserMapper implements Mapper<User, UserDto> {

    private UserMapper() {
    }

    private static final UserMapper INSTANCE = new UserMapper();

    @Override
    public UserDto mapToDto(User entity) {
        return UserDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }


    @Override
    public User mapToEntity(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }


    public static UserMapper getInstance() {
        return INSTANCE;
    }
}
