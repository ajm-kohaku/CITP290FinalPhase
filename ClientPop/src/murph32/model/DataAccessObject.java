package murph32.model;

import java.util.HashMap;
import java.util.List;

/**
 * <code>DataAccessObject</code> class defines DAO structure.
 *
 * @param <E> generic object for class
 */
public interface DataAccessObject<E> {

    void create(E data) throws DataAccessException;

    E read(String key) throws DataAccessException;

    void update(E data) throws DataAccessException;

    void delete(String key) throws DataAccessException;

    List<E> listAll() throws DataAccessException;

    HashMap<String, E> hashList();
}
