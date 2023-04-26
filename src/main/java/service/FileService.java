package service;


import dto.EventDto;
import dto.FileDto;
import entity.File;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import mapper.FileMapper;
import repository.FileRepository;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class FileService {

    private static final String INCOMPLETE_PATH = "file:///C:/Users/timga/IdeaProjects/timur_project/my_computer/";
    private static final String INCOMPLETE_DIRECTORY_PATH = "my_computer/";


    private final FileRepository fileRepository = FileRepository.getInstance();

    private static final FileService INSTANCE = new FileService();

    private final FileMapper fileMapper = FileMapper.getInstance();


    private final EventService eventService = EventService.getInstance();

    private FileService() {
    }

//    public void deleteById(Integer fileId, Integer userId) {
//        File file = fileRepository.getById(fileId).get();
//        File updatedFile = File.builder()
//                .status("DELETED")
//                .id(file.getId())
//                .name(file.getName())
//                .filePath(file.getFilePath())
//                .build();
//        fileRepository.update(updatedFile);
//        EventDto eventDto = EventDto.builder()
//                .fileId(fileId)
//                .userId(userId)
//                .build();
//        eventService.save(eventDto);
//    }


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

    //    Path downloadPath = Path.of(URI.create("file:///C:/Users/timga/IdeaProjects/timur_project/my_computer/" + "sound"));
    public String downloadFile(String fileName, Integer userId) throws IOException {
        Path downloadPath = Path.of(URI.create(INCOMPLETE_PATH + fileName));
        String result = new String(Files.readAllBytes(downloadPath));
        Optional<Integer> fileId = getAll().stream().filter(file -> file.getFileName().equals(fileName)).findFirst().map(FileDto::getId);
        EventDto eventDto = EventDto.builder()
                .userId(userId)
                .fileId(fileId.get())
                .build();
        eventService.save(eventDto);
        return result;
    }


    public void deleteByName(String fileName, Integer userId) throws IOException {
        Files.delete(Path.of(URI.create(INCOMPLETE_PATH + fileName)));
        Integer fileId = fileRepository.getAll().stream().filter(file -> file.getName().equals(fileName)).findFirst().get().getId();
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

    public static FileService getInstance() {
        return INSTANCE;
    }
}
