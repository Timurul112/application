package service;

import dto.UserDto;
import entity.User;
import mapper.UserMapper;
import repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class UserService {


    private UserService() {
    }

    private static final UserService INSTANCE = new UserService();
    private UserRepository userRepository = UserRepository.getInstance();
    private  UserMapper  userMapper = UserMapper.getInstance();

    private FileService fileService = FileService.getInstance();


    public void deleteById(Integer id) {
        Optional<User> maybeUser = userRepository.getById(id);
        List<String> listFileNames = maybeUser.get().getEvents().stream().map(event -> event.getFile().getName()).toList();

        listFileNames.forEach(fileName -> {
            try {
                fileService.deleteByName(fileName, id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        maybeUser.ifPresent(user -> userRepository.delete(user.getId()));
    }


    public Optional<UserDto> getById(Integer id) {
        Optional<User> maybeUser = userRepository.getById(id);
        return maybeUser.map(userMapper::mapToDto);
    }


    public User create(UserDto userDto) {
        User userEntity = User.builder()
                .name(userDto.getName())
                .build();
        return userRepository.save(userEntity);
    }

    public List<UserDto> getAll() {
        List<User> users = userRepository.getAll();
        return users.stream().map(userMapper::mapToDto).toList();
    }


    public UserDto update(UserDto userDto) {
        User user = userMapper.mapToEntity(userDto);
        User updateUser = userRepository.update(user);
        return userMapper.mapToDto(updateUser);
    }


    public static UserService getInstance() {
        return INSTANCE;
    }
}
