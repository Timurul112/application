package service;

import dto.EventDto;
import entity.Event;
import entity.File;
import entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.EventRepository;
import repository.FileRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;


    @Test
    void save() {

        EventDto eventDto = EventDto.builder()
                .userId(1)
                .fileId(1)
                .build();

        File file = File.builder()
                .id(eventDto.getFileId())
                .build();

        User user = User.builder()
                .id(eventDto.getUserId())
                .build();
        Event saveEvent = Event.builder()
                .user(user)
                .file(file)
                .build();
        doReturn(Optional.of(user)).when(userRepository).getById(user.getId());
        doReturn(Optional.of(file)).when(fileRepository).getById(file.getId());
        doReturn(saveEvent).when(eventRepository).save(saveEvent);

        eventService.save(eventDto);


        verify(userRepository).getById(eventDto.getUserId());
        verify(fileRepository).getById(eventDto.getFileId());
        verify(eventRepository).save(saveEvent);
    }

    @Test
    void getAll() {
        User timur = User.builder().name("Timur").build();
        User artem = User.builder().name("Artem").build();
        File test = File.builder().name("test").build();
        File test1 = File.builder().name("test1").build();
        Event event = Event.builder().file(test).user(timur).build();
        Event event1 = Event.builder().file(test1).user(artem).build();
        ArrayList<Event> eventsList = new ArrayList<>();
        eventsList.add(event);
        eventsList.add(event1);
        doReturn(eventsList).when(eventRepository).getAll();

        List<EventDto> actualList = eventService.getAll();

        assertThat(actualList).isNotEmpty();
        assertThat(actualList).hasSize(eventsList.size());
    }


    @Test
    void getById() {
        User timur = User.builder().name("Timur").build();
        File test = File.builder().name("test").build();

        Event event = Event.builder()
                .id(1)
                .user(timur)
                .file(test)
                .build();
        EventDto eventDto = EventDto.builder().userId(timur.getId()).fileId(test.getId()).build();
        doReturn(Optional.of(event)).when(eventRepository).getById(1);

        Optional<EventDto> actual = eventService.getById(1);
        assertThat(actual).isPresent();
        assertThat(actual).isEqualTo(Optional.of(eventDto));

    }
}