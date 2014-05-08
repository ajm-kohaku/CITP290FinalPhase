package murph32.model;

/**
 * Handles Exceptions when handling the data store.
 * created based on class example.
 */
public class DataAccessException extends Exception {

    private static final long serialVersionUID = 1843093481564120502L;

    public DataAccessException(String message) {
        super(message);
    }
}
