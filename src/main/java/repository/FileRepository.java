package repository;

import entity.File;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class FileRepository implements CrudRepository<Integer, File> {

    private static final String GET_FILE_ID_BY_NAME_SQL = """
            SELECT file.id FROM file
            WHERE name = :name
            """;


    private static final FileRepository INSTANCE = new FileRepository();


    private FileRepository() {
    }

    @Override
    public void delete(Integer id) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            try {
                session.beginTransaction();
                File file = session.get(File.class, id);
                session.remove(file);
                session.flush();
                session.getTransaction().commit();
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }

    @Override
    public Optional<File> getById(Integer id) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                session.getTransaction().commit();
                return Optional.ofNullable(session.find(File.class, id));
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }

    @Override
    public List<File> getAll() {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                List<File> resultList = session.createQuery("FROM File f", File.class).list();
                session.getTransaction().commit();
                return resultList;
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }


    @Override
    public File save(File entity) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
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
    public File update(File entity) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                File mergeFile = session.merge(entity);
                session.getTransaction().commit();
                return mergeFile;
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }

    public Integer getFileIdByName(String fileName) {
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            try {
                session.beginTransaction();
                NativeQuery<Integer> nativeQuery = session.createNativeQuery(GET_FILE_ID_BY_NAME_SQL, Integer.class);
                nativeQuery.setParameter("name", fileName);
                Integer integer = nativeQuery.getResultList().stream().findFirst().get();
                session.getTransaction().commit();
                return integer;
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                throw e;
            }
        }
    }


    public static FileRepository getInstance() {
        return INSTANCE;
    }
}
