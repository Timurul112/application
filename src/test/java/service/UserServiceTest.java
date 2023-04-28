package service;

import dto.UserDto;
import entity.User;
import mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;


    @Test
    void deleteById() throws IOException {


    }


    @Test
    void getById() {
        User timur = User.builder()
                .id(1)
                .name("Timur")
                .build();

        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Timur")
                .build();
        doReturn(Optional.of(timur)).when(userRepository).getById(1);

        doReturn(userDto).when(userMapper).mapToDto(timur);

        Optional<UserDto> actualResult = userService.getById(timur.getId());
        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(userDto);
    }

    @Test
    void create() {
        User timur = User.builder()
                .name("Timur")
                .build();
        UserDto userDto = UserDto.builder()
                .name("Timur")
                .build();

        doReturn(timur).when(userRepository).save(timur);

        User actual = userService.create(userDto);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(timur);
    }

    @Test
    void getAll() {

        ArrayList<User> users = new ArrayList<>();
        users.add(User.builder().name("Артем").build());
        users.add(User.builder().name("Тимур").build());
        users.add(User.builder().name("Светлана").build());

        doReturn(users).when(userRepository).getAll();

        List<UserDto> actualList = userService.getAll();


        assertThat(actualList).isNotEmpty();
        assertThat(actualList).hasSize(users.size());
    }

    @Test
    void update() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Артем")
                .build();

        User userRepo = User.builder()
                .id(1)
                .name("Артем")
                .build();

        doReturn(userRepo).when(userRepository).update(userRepo);
        doReturn(userRepo).when(userMapper).mapToEntity(userDto);
        doReturn(userDto).when(userMapper).mapToDto(userRepo);

        UserDto actualResult = userService.update(userDto);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(userDto);
    }


    @Test
    void failTestUpdate() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Артем")
                .build();

        User userRepo = User.builder()
                .id(1)
                .name("Артем")
                .build();

        User userRepoFail = User.builder()
                .id(2)
                .name("Пес")
                .build();

        UserDto userDtoFail = UserDto.builder()
                .id(2)
                .name("Пес")
                .build();

        doReturn(userRepo).when(userMapper).mapToEntity(userDto);
        doReturn(userRepoFail).when(userRepository).update(userRepo);
        doReturn(userDtoFail).when(userMapper).mapToDto(userRepoFail);

        UserDto actualResult = userService.update(userDto);

        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(userDtoFail);
    }
}