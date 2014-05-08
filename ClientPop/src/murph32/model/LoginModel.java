package murph32.model;

import murph32.core.User;

import java.util.List;

/**
 * <code>LoginModel</code> translates the data from the server to the Login.
 */
public class LoginModel {
    private DataAccessObject<User> sao = new UserServerAO();

    /**
     * <code>getUsers</code> retrieves the requested user from the server access object.
     * <br>returns the requested user if it exists.</br>
     *
     * @param username: the attempted login username.
     * @throws DataAccessException
     */
    public User getUsers(String username) throws DataAccessException {
        return sao.read(username);
    }

    /**
     * TODO: remove this method..
     *
     * @throws DataAccessException
     */
    public List<User> listAllUsers() throws DataAccessException {
        return sao.listAll();
    }


}
