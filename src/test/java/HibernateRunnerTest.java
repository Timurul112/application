import dto.UserDto;
import entity.User;
import org.junit.jupiter.api.Test;
import repository.UserRepository;
import service.UserService;

import java.util.List;

class HibernateRunnerTest {
    private final UserService userService = UserService.getInstance();
    UserRepository userRepository = UserRepository.getInstance();


    @Test
    void createUserService() {
        UserDto userCreateDto = UserDto.builder()
                .name("Тимур")
                .build();
        userService.create(userCreateDto);
    }



    @Test
    void saveAndGetUser() {
        User user1 = User.builder().name("Тимур").build();
        User user2 = User.builder().name("Света").build();
        User saveUser = userRepository.save(user1);
        User saveUser2 = userRepository.save(user2);
        List<User> users = userRepository.getAll();
        List<Integer> integers = users.stream().map(User::getId).toList();
        System.out.println(integers);

    }

    @Test
    void getAllUserAndTransformationToId() {
        User user1 = User.builder().name("Тимур").build();
        User user2 = User.builder().name("Света").build();
        User saveUser = userRepository.save(user1);
        User saveUser2 = userRepository.save(user2);
        List<UserDto> list = userService.getAll();
        List<Integer> integers = list.stream().map(UserDto::getId).toList();
        System.out.println(integers);
    }


}




