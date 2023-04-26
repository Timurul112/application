package rest;

import dto.EventDto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.EventService;

import java.io.IOException;
import java.util.List;

import static util.utillity.ServletUtils.setJsonResponse;

/**
 * Так как ивенты - это история загрузок/добвления -> удалять и изменять его нельзя, а создается ивент при создании файла.
 */


@WebServlet("/api/v1/events")
public class EventRestControllerV1 extends HttpServlet {

    private final EventService eventService = EventService.getInstance();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String maybeEventId = request.getParameter("event_id");
        if (maybeEventId == null) {
            List<EventDto> events = eventService.getAll();
            setJsonResponse(events, response);
        } else {
            Integer eventId = Integer.valueOf(maybeEventId);
            EventDto eventDto = eventService.getById(eventId).orElseThrow(() -> new RuntimeException("Ивента с таким id не существует"));
            setJsonResponse(eventDto, response);
        }
    }
}
