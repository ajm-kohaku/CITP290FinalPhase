package murph32.model;

import murph32.core.Request;
import murph32.core.Response;
import murph32.core.User;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * sends CRUD(L) data to the server
 */
public class UserServerAO implements DataAccessObject<User> {
    Response response = null;

    private Response accessServer(Request rqst) {
        try {
            Socket s = new Socket("localhost", 12345);
            ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(s.getOutputStream()));

            os.writeObject(rqst);
            os.flush();

            ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
            response = (Response) is.readObject();

            s.close();

        } catch (UnknownHostException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private List<User> readAllUsers() {
        List<User> us = new ArrayList<>();
        Response resp = accessServer(new Request("LIST USERS", null));
        if (resp.getHeader().equals("SUCCESS")) {

            us = (List<User>) resp.getPayload();
        } else {
            //TODO: display (readAllUsers) error message
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
        User user = read(data.getUsername());
        if (user == null) {
            Response resp = accessServer(new Request("ADD USER", data));
            if (!resp.getHeader().equals("SUCCESS")) {
                //TODO: display (create) error message
            }
        }
    }

    @Override
    public User read(String key) throws DataAccessException {
        User user = new User();
        Request rqst = new Request("READ USER", key);
        Response resp = accessServer(rqst);
        if (resp.getHeader().equals("SUCCESS")) {

            user = (User) resp.getPayload();
        } else {
            //TODO: display (read) error message
            System.out.println("Unable to find username: " + key);
        }
        return user;
    }

    @Override
    public void update(User data) throws DataAccessException {
        User user = read(data.getUsername());
        if (user != null) {
            Response resp = accessServer(new Request("UPDATE USER", data));
            if (!resp.getHeader().equals("SUCCESS")) {
                //TODO: display (update) error message
            }
        }

    }

    @Override
    public void delete(String key) throws DataAccessException {
        User user = read(key);
        if (user != null) {
            Response resp = accessServer(new Request("REMOVE USER", key));
            if (!resp.getHeader().equals("SUCCESS")) {
                //TODO: display (delete) error message
            }
        }
    }

    @Override
    public List<User> listAll() throws DataAccessException {
        return readAllUsers();
    }

    @Override
    public HashMap<String, User> hashList() {
        return readAllUsersHash();
    }
}
