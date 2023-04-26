package repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<K extends Serializable, E> {
    void delete(K id);

    Optional<E> getById(K id);

    List<E> getAll();

    E save(E entity);

    E update(E entity);

}
