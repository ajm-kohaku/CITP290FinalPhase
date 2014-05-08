package murph32.controller;

import murph32.core.User;
import murph32.model.DataAccessException;
import murph32.model.LoginModel;
import murph32.view.LoginView;
import murph32.view.SalesView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <code>LoginController</code> acts as the controller class for the Login frame.
 * It accesses the DAO and provides validation for the login function.
 * <br>Repurposed Phase 3 solution class: <code>LoginDisplayController</code> to fit my version of phase 3.</br>
 *
 * @author hoffmanz
 * @author Amber Murphy
 */
public class LoginController {
    private LoginView view;
    private LoginModel model = new LoginModel();

    public LoginController(LoginView view) {
        this.view = view;
    }

    /**
     * <code>validateLogin</code>
     * redesigned Phase 3 class solution method: <code>doLogin()</code> to fit my style.
     *
     * @param username: <code>String</code> value used to validate the username.
     * @param password: <code>character array</code> value used to validate the password.
     */
    public void validateLogin(String username, char[] password) {
        User u = new User();
        try {
            u = model.getUsers(username);
        } catch (DataAccessException e) {
            view.errMsgInvalidLogin();
        }
        if (u.getUsername().equals(username) &&
                Arrays.equals(u.getPassword(), new String(password).toCharArray())) {
            new SalesView(u);
            LoginView.getFrame().dispose();
        } else {
            view.errMsgInvalidLogin();
            LoginView.getFrame().revalidate();
            LoginView.getFrame().repaint();
        }
        //TODO: remove hack once testing is done.
        if (String.valueOf(password).equals("123")) {
            userListHack();
        }
    }

    /**
     * TODO: delete this method before submitting!!
     */
    private void userListHack() {
        List<User> userList = new ArrayList<>();
        try {
            userList = model.listAllUsers();
        } catch (DataAccessException e) {
            System.out.println("Can't list all users.");
        }
        for (User u : userList) {
            System.out.println(u.getUsername() + "\t" + String.valueOf(u.getPassword()) + "\t" + u.getAccessLevel());
        }
    }

    /**
     * <code>cancelLogin</code> method closes the login frame.
     */
    public void cancelLogin() {
        LoginView.getFrame().dispose();
    }
}
