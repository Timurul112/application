package service;

import dto.EventDto;
import entity.Event;
import entity.File;
import entity.User;
import mapper.EventMapper;
import repository.EventRepository;
import repository.FileRepository;
import repository.UserRepository;

import java.util.List;
import java.util.Optional;

public class EventService {

    private EventService() {
    }

    public static EventService INSTANCE = new EventService();
    private final FileRepository fileRepository = FileRepository.getInstance();
    private final UserRepository userRepository = UserRepository.getInstance();
    private final EventRepository eventRepository = EventRepository.getInstance();
    EventMapper eventMapper = EventMapper.getInstance();



    public void save(EventDto event) {
        Optional<File> maybeFile = fileRepository.getById(event.getFileId());
        Optional<User> maybeUser = userRepository.getById(event.getUserId());
        if (!(maybeUser.isPresent() && maybeFile.isPresent())) {
            throw new RuntimeException("непредвиденная ошибка");
        }
        User user = maybeUser.get();
        File file = maybeFile.get();
        Event savedEvent = Event.builder()
                .user(user)
                .file(file)
                .build();
        eventRepository.save(savedEvent);
    }

    public List<EventDto> getAll() {
        return eventRepository.getAll().stream().map(event -> eventMapper.mapToDto(event)).toList();
    }

    public Optional<EventDto> getById(Integer id) {
        Optional<Event> maybeEvent = eventRepository.getById(id);
        return maybeEvent.map(event -> eventMapper.mapToDto(event));
    }


    public static EventService getInstance() {
        return INSTANCE;
    }
}
