package service;

import dto.EventDto;
import dto.FileDto;
import entity.Event;
import entity.File;
import entity.User;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.FileRepository;
import repository.UserRepository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private EventService eventService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FileService fileService;


    @Test
    void getById() {

        File file = File.builder()
                .id(1)
                .name("test")
                .filePath("my_computer/test")
                .status("SAVED")
                .build();

        FileDto fileDto = FileDto.builder()
                .id(1)
                .fileName("test")
                .status("SAVED")
                .filePath("my_computer/test")
                .build();

        doReturn(Optional.of(file)).when(fileRepository).getById(file.getId());

        Optional<FileDto> actualResult = fileService.getById(file.getId());

        assertThat(actualResult).isPresent();
        assertThat(actualResult.get()).isEqualTo(fileDto);

    }

    @Test
    void getAll() {
        File test1 = File.builder()
                .id(1)
                .name("test1")
                .build();
        File test2 = File.builder()
                .id(1)
                .name("test2")
                .build();
        File test3 = File.builder()
                .id(1)
                .name("test3")
                .build();

        List<File> fileList = Arrays.asList(test1, test2, test3);


        doReturn(fileList).when(fileRepository).getAll();

        List<FileDto> actualList = fileService.getAll();

        assertThat(actualList).isNotEmpty();
        assertThat(actualList).hasSize(fileList.size());
    }

    @Test
    void uploadFile() throws IOException {


        String fileName = "test.txt";
        Integer userId = 1;
        byte[] fileContent = "Hello World".getBytes();
        ServletInputStream mockInputStream = mock(ServletInputStream.class);

        doReturn(mockInputStream).when(request).getInputStream();
        doReturn(fileContent).when(mockInputStream).readAllBytes();
        File file = File.builder()
                .filePath("my_computer/" + fileName)
                .name(fileName)
                .status("SAVED")
                .build();
        doReturn(file).when(fileRepository).save(file);

        fileService.uploadFile(request, fileName, userId);

        verify(fileRepository).save(file);

        EventDto eventDto = EventDto.builder()
                .fileId(file.getId())
                .userId(userId)
                .build();

        verify(eventService).save(eventDto);


    }

    @Test
    void downloadFile() throws IOException {

        String fileName = "test.txt";
        String fileContent = "Hello World";
        Integer userId = 1;
        Integer fileId = 123;

        doReturn(fileId).when(fileRepository).getFileIdByName(fileName);

        String result = fileService.downloadFile(fileName, userId);

        assertThat(fileContent).isEqualTo(result);

        EventDto eventDto = EventDto.builder()
                .userId(userId)
                .fileId(fileId)
                .build();
        verify(eventService).save(eventDto);
    }


    @Test
    void deleteByName() throws IOException {

        String fileName = "test.txt";
        Integer userId = 1;
        Integer fileId = 123;

        doReturn(fileId).when(fileRepository).getFileIdByName(fileName);
        File file = File.builder().id(fileId).build();
        Event event = Event.builder().file(file).build();
        User user = User.builder().events(List.of(event)).build();
        doReturn(Optional.of(user)).when(userRepository).getById(userId);

        fileService.deleteByName(fileName, userId);

        verify(fileRepository).update(any(File.class));
        EventDto expectedEventDto = EventDto.builder()
                .userId(userId)
                .fileId(fileId)
                .build();
        verify(eventService).save(expectedEventDto);
    }


    /**
     *не смог реализовать этот тест никак. не знаю как захардкодить в мок ServletInputStream. То что получилось написать
     * оставляю. Бился с ним очень много времени - ничего не смог сделать.
     */

    @Test
    void update() throws IOException {
        Integer userId = 1;
        String fileName = "test.txt";
        Integer fileId = 1;

        ServletInputStream inputStream = request.getInputStream(); // он пустой, не знаю как с ним работать и как в него данные запихать.
        //если его настроить, то все должно работать в этом тесте.

        doReturn(inputStream).when(request).getInputStream();

        File file = File.builder()
                .id(fileId)
                .name(fileName)
                .build();
        Event event = Event.builder()
                .file(file)
                .build();
        User user = User.builder()
                .id(userId)
                .events(List.of(event))
                .build();

        doReturn(Optional.of(user)).when(userRepository).getById(userId);
        doReturn(fileId).when(fileRepository).getFileIdByName(fileName);

        File updatedFile = File.builder()
                .id(fileId)
                .status("UPDATED")
                .filePath("my_computer/" + fileName)
                .name(fileName)
                .build();


        doReturn(updatedFile).when(fileRepository).update(updatedFile);

        fileService.update(request, userId, fileName);


        verify(fileRepository, times(1)).update(updatedFile);
        EventDto eventDto = EventDto.builder()
                .fileId(fileId)
                .userId(userId)
                .build();
        verify(eventService, times(1)).save(eventDto);
    }

}



