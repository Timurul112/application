package mapper;

import dto.EventDto;
import entity.Event;

public class EventMapper {

    private EventMapper() {
    }

    public static EventMapper INSTANCE = new EventMapper();


    public EventDto mapToDto(Event entity) {
        return EventDto.builder()
                .fileId(entity.getFile().getId())
                .userId(entity.getUser().getId())
                .build();
    }



    public static EventMapper getInstance() {
        return INSTANCE;
    }
}
