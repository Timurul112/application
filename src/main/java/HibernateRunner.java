import entity.Event;
import entity.File;
import entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.archive.scan.spi.PackageInfoArchiveEntryHandler;
import repository.EventRepository;
import repository.FileRepository;
import repository.UserRepository;
import service.FileService;
import service.UserService;
import util.HibernateUtil;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

public class HibernateRunner {

    private static final String INCOMPLETE_PATH = "file://C:/Users/timga/IdeaProjects/timur-project/my_computer/";


    public static void main(String[] args) throws IOException {

        UserRepository userRepository = UserRepository.getInstance();
        Optional<User> maybeUser = userRepository.getById(13);
        List<Integer> eventId = maybeUser.get().getEvents().stream().map(Event::getId).toList();
        System.out.println(eventId);
    }

    private static void getAllFileTest() {
        FileRepository fileRepository = FileRepository.getInstance();
        List<File> all = fileRepository.getAll();
        List<String> strings = all.stream().map(File::getName).toList();
        System.out.println(strings);
    }


    private static void eventSavingTest() {
        UserRepository userRepo = UserRepository.getInstance();
        FileRepository fileRepo = FileRepository.getInstance();
        EventRepository eventRepo = EventRepository.getInstance();
        User user = User.builder()
                .name("Аркадий")
                .build();
        userRepo.save(user);


        File file = File.builder()
                .filePath("my_computer")
                .name("xml")
                .build();
        fileRepo.save(file);

        Event event = Event.builder().user(user).file(file).build();
        eventRepo.save(event);
    }

    private static void checkUserServiceWithoutSession() {
        UserService userService = UserService.getInstance();
        System.out.println(userService.getById(1).get());
    }

    private static void withoutSession() {
        UserRepository userRepository = UserRepository.getInstance();
        Optional<User> byId = userRepository.getById(1);
        byId.ifPresent(System.out::println);
    }

    private static void withSession() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            UserRepository userRepository = UserRepository.getInstance();
            Optional<User> byId = userRepository.getById(1);
            User user = byId.get();
            System.out.println(user);
            session.getTransaction().commit();
        }
    }

}


