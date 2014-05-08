package murph32.model;

import murph32.core.Request;
import murph32.core.Response;
import murph32.core.User;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dukat on 5/3/2014.
 */
public class RequestHandler implements Runnable {
    private static DBUserDAO userDAO = new DBUserDAO();
    private Socket sock;

    public RequestHandler(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run() {
        Request rqst = null;

        try {
            ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(sock.getInputStream()));
            rqst = (Request) ois.readObject();
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("Unable to read or access Request.");
        }

        Response resp = null;
        if (rqst != null) {
            switch (rqst.getHeader()) {
                case "LIST USERS":
                    ArrayList<User> ul = (ArrayList<User>) getUsers();
                    resp = new Response("SUCCESS", ul);
                    break;
                case "ADD USER":
                    addUser((User) rqst.getPayload());
                    resp = new Response("SUCCESS", null);
                    break;
                case "READ USER":
                    User ru = findUser((String) rqst.getPayload());
                    resp = new Response("SUCCESS", ru);
                    break;
                case "UPDATE USER":
                    User uu = (User) rqst.getPayload();
                    updateUser(uu.getUsername(), uu.getPassword(), uu.getAccessLevel());
                    resp = new Response("SUCCESS", null);
                    break;
                case "REMOVE USER":
                    deleteUser((String) rqst.getPayload());
                    resp = new Response("SUCCESS", null);
                    break;
                default:
                    resp = new Response("FAILURE", null);
                    break;
            }
        }
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(sock.getOutputStream()));
            oos.writeObject(resp);
            oos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User u) {
        try {
            userDAO.create(u);
        } catch (DataAccessException e) {
            System.out.println("Unable to create user: " + u.getUsername());
            e.printStackTrace();
        }
    }

    public void updateUser(String username, char[] password, String accessLevel) {
        try {
            User u = userDAO.read(username);
            u.setPassword(password);
            u.setAccessLevel(accessLevel);
            userDAO.update(u);
        } catch (DataAccessException e) {
            System.out.println("Unable to update user: " + username);
            e.printStackTrace();
        }

    }

    public void deleteUser(String username) {
        try {
            userDAO.delete(username);
        } catch (DataAccessException e) {
            System.out.println("Unable to delete user: " + username);
            e.printStackTrace();
        }
    }

    public User findUser(String username) {
        User u = null;
        try {
            u = userDAO.read(username);
        } catch (DataAccessException e) {
            System.out.println("Unable to find user: " + username);
            e.printStackTrace();
        }
        return u;
    }

    public List<User> getUsers() {
        List<User> us = new ArrayList<>();
        try {
            us.addAll(userDAO.listAll());
        } catch (DataAccessException e) {
            System.out.println("Unable to find users. ");
            e.printStackTrace();
        }
        return us;
    }

}
