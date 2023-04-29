package repository;

import entity.Event;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class EventRepository implements CrudRepository<Integer, Event> {

    private static final EventRepository INSTANCE = new EventRepository();

    private EventRepository() {
    }


    @Override
    public void delete(Integer id) {//не будет использоваться
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                Event event = session.get(Event.class, id);
                session.remove(event);
                session.getTransaction().commit();
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }


    @Override
    public Optional<Event> getById(Integer id) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                Event event = session.get(Event.class, id);
                session.getTransaction().commit();
                return Optional.ofNullable(event);
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }


    @Override
    public List<Event> getAll() {//нужен точно
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                List<Event> listEntity = session.createQuery("FROM Event e", Event.class).list();
                session.getTransaction().commit();
                return listEntity;
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }


    @Override
    public Event save(Event entity) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                session.persist(entity);
                session.getTransaction().commit();
                return entity;
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }


    @Override
    public Event update(Event entity) { //не будет использоваться
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                Event merge = session.merge(entity);
                session.getTransaction().commit();
                return merge;
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }


    public static EventRepository getInstance() {
        return INSTANCE;
    }
}
