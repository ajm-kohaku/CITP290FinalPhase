package murph32.model;

import murph32.core.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dukat on 5/3/2014.
 */
public class DBUserDAO implements DataAccessObject<User> {
    private static final String DERBY_DRIVER_CLASS = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String DERBY_CREATE_CONNECTION = "jdbc:derby:POPDB";

    //prepared statements
    private static final String SELECT_ALL_USERS =
            "SELECT username, password, accesslevel FROM users";
    private static final String SELECT_USER =
            "SELECT username, password, accesslevel FROM users WHERE username = ?";
    private static final String INSERT_USER =
            "INSERT INTO users (username, password, accesslevel) VALUES(?,?,?)";

    private static final String UPDATE_USER =
            "UPDATE users SET username = ?, password = ?, accesslevel = ?" +
                    "WHERE username = ?" +
                    "AND username <> 'ADMIN'";
    private static final String DELETE_USER =
            "DELETE FROM users WHERE username = ? AND username <> 'ADMIN'";

    public DBUserDAO() {
        try {
            Class.forName(DERBY_DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            System.out.println("Unable to locate Derby database class.");
        }
    }

    private User bootStrap() {
        //create default user
        User u = new User();
        u.setUsername("ADMIN");
        char[] pswd = {'a', 'n', 's', 'h', '0', 'u'};
        u.setPassword(pswd);
        u.setAccessLevel("MANAGER");
        return u;
    }

    private List<User> readAllUsers() {
        List<User> us = new ArrayList<>();

        try (Connection derbyCon = DriverManager.getConnection(DERBY_CREATE_CONNECTION)) {
            try (PreparedStatement selectUsers = derbyCon
                    .prepareStatement(SELECT_ALL_USERS)) {
                try (ResultSet rs = selectUsers.executeQuery()) {
                    while (rs.next()) {
                        User u = new User();
                        u.setUsername(rs.getString("username"));
                        u.setPassword(rs.getString("password").toCharArray());
                        u.setAccessLevel(rs.getString("accesslevel"));
                        us.add(u);
                    }
                    if (us.size() == 0) {
                        us.add(bootStrap());
                        try (PreparedStatement insertUser = derbyCon
                                .prepareStatement(INSERT_USER)) {
                            insertUser.setString(1, us.get(0).getUsername());
                            insertUser.setString(2, new String(us.get(0).getPassword()));
                            insertUser.setString(3, us.get(0).getAccessLevel());

                            insertUser.execute();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Can't create database connection.");
            System.out.println("Unable to execute query.");
            e.printStackTrace();
        }
        return us;
    }

    private HashMap<String, User> readAllUsersHash() {
        HashMap<String, User> us = new HashMap<>();
        List<User> userList = readAllUsers();
        for (User u : userList) {
            us.put(u.getUsername(), u);
        }
        return us;
    }

    @Override
    public void create(User data) throws DataAccessException {
        User user = readAllUsersHash().get(data.getUsername());
        if (user == null) {
            try (Connection derbyCon = DriverManager
                    .getConnection(DERBY_CREATE_CONNECTION)) {
                try (PreparedStatement insertUser = derbyCon
                        .prepareStatement(INSERT_USER)) {
                    insertUser.setString(1, data.getUsername());
                    insertUser.setString(2, new String(data.getPassword()));
                    insertUser.setString(3, data.getUsername());

                    insertUser.execute();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public User read(String key) throws DataAccessException {
        User user = new User();
        try (Connection derbyCon = DriverManager
                .getConnection(DERBY_CREATE_CONNECTION)) {
            try (PreparedStatement readUser = derbyCon
                    .prepareStatement(SELECT_USER)) {
                readUser.setString(1, key);
                try (ResultSet rs = readUser.executeQuery()) {
                    while (rs.next()) {
                        user.setUsername(rs.getString("username"));
                        user.setPassword(rs.getString("password").toCharArray());
                        user.setAccessLevel(rs.getString("accesslevel"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    @Override
    public void update(User data) throws DataAccessException {
        User user = readAllUsersHash().get(data.getUsername());
        if (user != null) {
            try (Connection derbyCon = DriverManager
                    .getConnection(DERBY_CREATE_CONNECTION)) {
                try (PreparedStatement updateUser = derbyCon
                        .prepareStatement(UPDATE_USER)) {
                    updateUser.setString(1, user.getUsername());
                    updateUser.setString(2, new String(data.getPassword()));
                    updateUser.setString(3, data.getAccessLevel());
                    updateUser.setString(4, user.getUsername());
                    updateUser.execute();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(String key) throws DataAccessException {
        User user = readAllUsersHash().get(key);
        if (user != null) {
            try (Connection derbyCon = DriverManager
                    .getConnection(DERBY_CREATE_CONNECTION)) {
                try (PreparedStatement updateUser = derbyCon
                        .prepareStatement(DELETE_USER)) {
                    updateUser.setString(1, user.getUsername());

                    updateUser.execute();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<User> listAll() throws DataAccessException {
        HashMap<String, User> us = readAllUsersHash();
        return new ArrayList<>(us.values());
    }

    @Override
    public HashMap<String, User> hashList() {
        return readAllUsersHash();
    }

}
