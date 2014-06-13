package murph32.controller;

import murph32.core.User;
import murph32.model.UserMaintModel;
import murph32.view.InventoryView;
import murph32.view.UserMaintView;

import javax.swing.*;
import java.util.HashMap;

/**
 *
 */
public class UserMaintController {
    private UserMaintView view;
    private UserMaintModel model = new UserMaintModel();
    private DefaultListModel<User> userModel;
    private User user;

    public UserMaintController(UserMaintView view, User user) {
        this.view = view;
        this.user = user;
        this.userModel = view.getUserModel();
    }

    public HashMap<String, User> getUsers() {
        return model.getUsers();
    }

    public void salesDisplay() {
        UserMaintView.getFrame().dispose();
    }

    public void inventory() {
        new InventoryView(user);
        UserMaintView.getFrame().dispose();

    }

    public void removeUser(String username) {
        if (!username.equals("ADMIN") &&
                validateUsername(username) != null) {
            model.removeUser(username);
            userModel.remove(getIndex(username));
            view.errMsgRest();
        } else {
            view.errMsgRemoveUser();
        }
    }

    public void updateUser(String username, char[] password, String accessLevel) {
        if (model.getUsers().containsKey(username) &&
                validatePassword(password) != null) {
            User u = new User();
            u.setUsername(username);
            u.setPassword(password);
            u.setAccessLevel(accessLevel);
            model.updateUser(u);
            userModel.set(getIndex(username), u);
            view.errMsgRest();
        } else {
            view.errMsgUpdateUser();
        }
    }

    public void addUser(String username, char[] password, String accessLevel) {
        if (!model.getUsers().containsKey(username) &&
                validateUsername(username) != null &&
                validatePassword(password) != null) {
            User u = new User();
            u.setUsername(validateUsername(username));
            u.setPassword(validatePassword(password));
            u.setAccessLevel(accessLevel);
            model.addUser(u);
            userModel.addElement(u);
            view.errMsgRest();
        } else {
            view.errMsgAddUser();
        }
    }

    private String validateUsername(String username) {
        if (username.equalsIgnoreCase("ADMIN") ||
                username.length() < 6 || username.length() > 12 ||
                username.matches("[^A-Za-z0-9]+")) {
            view.errMsgInvalidUsername();
        }
        return username;
    }

    private char[] validatePassword(char[] password) {
        if (password.length < 8 || password.length > 20 ||
                new String(password).matches("\\s")) {
            view.errMsgInvalidPassword();
        }
        return password;
    }

    //got help from Dave Smith to find the index of invoiceModel.
    private int getIndex(String username) {
        int index = -1;

        for (int j = 0; j < userModel.size(); j++) {
            if (userModel.get(j).getUsername().equals(username)) {
                index = j;
                break;
            }
        }
        return index;
    }

}
