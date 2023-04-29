package service;


import dto.EventDto;
import dto.FileDto;
import entity.File;
import entity.User;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import mapper.FileMapper;
import repository.FileRepository;
import repository.UserRepository;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class FileService {
    private FileService() {
    }
    private static FileService INSTANCE = new FileService();

    private static final String INCOMPLETE_PATH = "file:///C:/Users/timga/IdeaProjects/timur_project/my_computer/";
    private static final String INCOMPLETE_DIRECTORY_PATH = "my_computer/";


    private FileRepository fileRepository = FileRepository.getInstance();


    private FileMapper fileMapper = FileMapper.getInstance();


    private EventService eventService = EventService.getInstance();

    private UserRepository userRepository = UserRepository.getInstance();


    public Optional<FileDto> getById(Integer id) {
        Optional<File> maybeFile = fileRepository.getById(id);
        return maybeFile.map(fileMapper::mapToDto);
    }


    public List<FileDto> getAll() {
        List<File> files = fileRepository.getAll();
        return files.stream().map(fileMapper::mapToDto).toList();
    }

    public void uploadFile(HttpServletRequest request, String fileName, Integer userId) throws IOException {
        try (ServletInputStream inputStream = request.getInputStream()) {
            byte[] bytes = inputStream.readAllBytes();
            Path savePath = Path.of(URI.create(INCOMPLETE_PATH + fileName));
            Files.write(savePath, bytes);
            File file = File.builder()
                    .name(fileName)
                    .filePath(INCOMPLETE_DIRECTORY_PATH + fileName)
                    .status("SAVED")
                    .build();
            File savedFile = fileRepository.save(file);
            EventDto eventDto = EventDto.builder()
                    .fileId(savedFile.getId())
                    .userId(userId)
                    .build();
            eventService.save(eventDto);
        }
    }

    public String downloadFile(String fileName, Integer userId) throws IOException {
        Path downloadPath = Path.of(URI.create(INCOMPLETE_PATH + fileName));
        String result = new String(Files.readAllBytes(downloadPath));
        Integer fileId = fileRepository.getFileIdByName(fileName);
        EventDto eventDto = EventDto.builder()
                .userId(userId)
                .fileId(fileId)
                .build();
        eventService.save(eventDto);
        return result;
    }


    public void deleteByName(String fileName, Integer userId) throws IOException {
        Integer fileId = fileRepository.getFileIdByName(fileName);
        User user = userRepository.getById(userId).get();
        List<Integer> fileIds = user.getEvents().stream().map(event -> event.getFile().getId()).toList();
        if (!fileIds.contains(fileId)) {
            throw new RuntimeException("Нет доступа к файлу");
        }
        Files.deleteIfExists(Path.of(URI.create(INCOMPLETE_PATH + fileName)));
        File file = File.builder()
                .id(fileId)
                .name(fileName)
                .filePath(INCOMPLETE_DIRECTORY_PATH + fileName)
                .status("DELETED")
                .build();
        fileRepository.update(file);
        EventDto eventDto = EventDto.builder()
                .userId(userId)
                .fileId(fileId)
                .build();
        eventService.save(eventDto);
    }

    public void update(HttpServletRequest request, Integer userId, String fileName) throws IOException {
        Integer fileId = fileRepository.getFileIdByName(fileName);
        List<Integer> fileIds = userRepository.getById(userId).get().getEvents().stream().map(event -> event.getFile().getId()).toList();
        if (!fileIds.contains(fileId)) {
            throw new RuntimeException("Нет доступа к файлу");
        }
        Path path = Path.of(URI.create(INCOMPLETE_PATH + fileName));
        Files.delete(path);
        Files.write(path, request.getInputStream().readAllBytes());
        File updatedFile = File.builder()
                .id(fileId)
                .status("UPDATED")
                .filePath(INCOMPLETE_DIRECTORY_PATH + fileName)
                .name(fileName)
                .build();
        EventDto eventDto = EventDto.builder()
                .fileId(fileId)
                .userId(userId)
                .build();
        fileMapper.mapToDto(fileRepository.update(updatedFile));
        eventService.save(eventDto);
    }

    public static FileService getInstance() {
        return INSTANCE;
    }
}
