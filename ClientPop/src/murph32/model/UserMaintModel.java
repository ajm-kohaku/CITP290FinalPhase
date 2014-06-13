package murph32.model;

import murph32.core.User;

import java.util.HashMap;

/**
 *
 */
public class UserMaintModel {
    private DataAccessObject<User> sao = new UserServerAO();

    public HashMap<String, User> getUsers() {
        return sao.hashList();

    }

    public void addUser(User u) {
        try {
            sao.create(u);
        } catch (DataAccessException e) {
            System.out.println("(User Model) Unable to create user");
            e.printStackTrace();
        }
    }

    public void updateUser(User user) {
        try {
            User u = sao.read(user.getUsername());
            u.setPassword(user.getPassword());
            u.setAccessLevel(user.getAccessLevel());
            sao.update(user);
        } catch (DataAccessException e) {
            System.out.println("(User Model) Unable to update product ");
            e.printStackTrace();
        }
    }

    public void removeUser(String username) {
        try {
            sao.delete(username);
        } catch (DataAccessException e) {
            System.out.println("(User Model) Unable to delete product");
        }
    }
}
