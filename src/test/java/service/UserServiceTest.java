package service;

import dto.UserDto;
import entity.User;
import mapper.UserMapper;
import org.assertj.core.api.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.UserRepository;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


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
                .id(1)
                .name("Timur")
                .build();

        UserDto timurDto = UserDto.builder()
                .id(1)
                .name("Timur")
                .build();

        User stub = doReturn(timur).when(userRepository).save(timur);

        User actual = userService.create(timurDto);

        assertThat(actual).isNotNull();
        assertThat(actual).isEqualTo(timur);
    }

    @Test
    void getAll() {
    }

    @Test
    void update() {
    }

    @Test
    void getInstance() {
    }
}