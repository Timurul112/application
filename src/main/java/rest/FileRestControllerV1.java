package rest;

import dto.FileDto;
import dto.UserDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.FileService;
import service.UserService;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static util.utillity.ServletUtils.setJsonResponse;

@WebServlet("/api/v1/files")
public class FileRestControllerV1 extends HttpServlet {

    private final UserService userService = UserService.getInstance();
    private final FileService fileService = FileService.getInstance();
    private static final String INCOMPLETE_PATH = "file:///C:/Users/timga/IdeaProjects/timur_project/my_computer/";


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maybeUserId = request.getParameter("user_id");
        String fileName = request.getParameter("file_name");
        if (maybeUserId == null) {
            throw new RuntimeException("Вы не ввели id пользователя");
        }
        if (fileName == null) {
            throw new RuntimeException("Вы не ввели имя файла");
        }
        Integer userId = Integer.valueOf(maybeUserId);
        if (userService.getById(userId).isEmpty()) {
            throw new RuntimeException("Пользователя не сущетсвует");
        }
        fileService.uploadFile(request, fileName, userId);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maybeUserId = request.getParameter("user_id");
        String fileName = request.getParameter("file_name");
        if (maybeUserId == null) {
            throw new RuntimeException("Введите имя пользователя");
        }
        Integer userId = Integer.valueOf(maybeUserId);
        if (fileName == null) {
            throw new RuntimeException("Введите имя файла");
        }
        UserDto userDto = userService.getById(userId).orElseThrow(() -> new RuntimeException("Пользователя не существует"));
        if (!Files.exists(Path.of(URI.create(INCOMPLETE_PATH + fileName)))) {
            throw new RuntimeException("Файла не существует");
        }
        fileService.deleteByName(fileName, userId);
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maybeId = request.getParameter("user_id");
        String fileName = request.getParameter("file_name");
        String maybeFileId = request.getParameter("file_id");
        if (maybeId == null && fileName == null && maybeFileId == null) {
            List<FileDto> files = fileService.getAll();
            setJsonResponse(files, response);
        } else if (maybeId != null && fileName != null && maybeFileId == null) {
            if (!Files.exists(Path.of(URI.create(INCOMPLETE_PATH + fileName)))) {
                throw new RuntimeException("Файла не существует");
            }
            Integer id = Integer.valueOf(maybeId);
            userService.getById(id).orElseThrow((() -> new RuntimeException("Пользователя не существует")));
            response.setHeader("Content-Disposition", "attachment; filename=%s".formatted(fileName));
            String downloadFile = fileService.downloadFile(fileName, id);
            setJsonResponse(downloadFile, response);
        } else {
            Integer fileId = Integer.valueOf(maybeFileId);
            FileDto fileDto = fileService.getById(fileId).orElseThrow(() -> new RuntimeException("Файла не существует"));
            setJsonResponse(fileDto, response);
        }
    }
}
