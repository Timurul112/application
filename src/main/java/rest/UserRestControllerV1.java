package rest;


import dto.UserDto;
import entity.User;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.UserService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static util.utillity.ServletUtils.setJsonResponse;

@WebServlet("/api/v1/users")
public class UserRestControllerV1 extends HttpServlet {
    private final UserService userService = UserService.getInstance();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        if (name == null) {
            throw new RuntimeException("Введите имя пользователя");
        }
        UserDto userDto = UserDto.builder()
                .name(name)
                .build();
        User createdUser = userService.create(userDto);
        setJsonResponse(createdUser, response);
    }


    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maybeId = request.getParameter("user_id");
        String newName = request.getParameter("new_name");
        if (maybeId == null) {
            throw new RuntimeException("Вы не ввели id пользователя");
        }
        Integer userId = Integer.valueOf(maybeId);
        UserDto userDto = userService.getById(userId).orElseThrow((() -> new RuntimeException("Пользователя не существует")));
        if (newName == null) {
            throw new RuntimeException("Вы не ввели новое имя пользователя");
        }
        userDto.setName(newName);
        UserDto updatedUser = userService.update(userDto);
        setJsonResponse(updatedUser, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        Integer id = Integer.valueOf(request.getParameter("user_id"));
        userService.getById(id).orElseThrow((() -> new RuntimeException("Пользователя не существует")));
        userService.deleteById(id);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding(StandardCharsets.UTF_8.name());
        String maybeUserId = request.getParameter("user_id");
        if (maybeUserId != null) {
            Integer userId = Integer.valueOf(maybeUserId);
            UserDto userDto = userService.getById(userId).orElseThrow((() -> new RuntimeException("Пользователя не существует")));
            setJsonResponse(userDto, response);
        } else {
            List<UserDto> users = userService.getAll();
            setJsonResponse(users, response);
        }
    }
}
