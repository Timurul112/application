package repository;

import entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class UserRepository implements CrudRepository<Integer, User> {


    private static final UserRepository INSTANCE = new UserRepository();


    private UserRepository() {
    }

    @Override
    public User save(User entity) {
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
    public void delete(Integer id) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                User user = session.get(User.class, id);
                session.remove(user);
                session.getTransaction().commit();
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }

    @Override
    public User update(User entity) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                User mergeUser = session.merge(entity);
                session.getTransaction().commit();
                return mergeUser;
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }

    @Override
    public Optional<User> getById(Integer id) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                session.getTransaction().commit();
                return Optional.ofNullable(session.find(User.class, id));
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }


    @Override
    public List<User> getAll() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                List<User> resultList = session.createQuery("FROM User u", User.class).getResultList();
                session.getTransaction().commit();
                return resultList;
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }

    public static UserRepository getInstance() {
        return INSTANCE;
    }
}
