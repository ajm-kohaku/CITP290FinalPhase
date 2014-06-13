package murph32.model;

import murph32.core.Product;
import murph32.core.Request;
import murph32.core.Response;
import murph32.core.User;

import java.io.*;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class RequestHandler implements Runnable {
    private static DBUserDAO userDAO = new DBUserDAO();
    private static DBProductDAO productDAO = new DBProductDAO();
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
                case "LIST PRODUCTS":
                    ArrayList<Product> lp = (ArrayList<Product>) getProducts();
                    resp = new Response("SUCCESS", lp);
                    break;
                case "ADD PRODUCT":
                    addProduct((Product) rqst.getPayload());
                    resp = new Response("SUCCESS", null);
                    break;
                case "READ PRODUCT":
                    Product rp = findProduct((String) rqst.getPayload());
                    resp = new Response("SUCCESS", rp);
                    break;
                case "UPDATE PRODUCT":
                    Product up = (Product) rqst.getPayload();
                    updateProduct(up.getUpc(), up.getDescription(), up.getPrice(), up.getNumInStock());
                    resp = new Response("SUCCESS", null);
                    break;
                case "REMOVE PRODUCT":
                    deleteProduct((String) rqst.getPayload());
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


    private void addProduct(Product p) {
        try {
            productDAO.create(p);
        } catch (DataAccessException e) {
            System.out.println("Unable to create product: " + p.getDescription());
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

    private void updateProduct(String upc, String description, BigDecimal price, int numInStock) {
        try {
            Product p = productDAO.read(upc);
            p.setDescription(description);
            p.setPrice(price);
            p.setNumInStock(numInStock);
            productDAO.update(p);
        } catch (DataAccessException e) {
            System.out.println("Unable to update user: " + upc);
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

    private void deleteProduct(String upc) {
        try {
            productDAO.delete(upc);
        } catch (DataAccessException e) {
            System.out.println("Unable to delete product: " + upc);
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

    private Product findProduct(String upc) {
        Product p = null;
        try {
            p = productDAO.read(upc);
        } catch (DataAccessException e) {
            System.out.println("Unable to find product: " + upc);
            e.printStackTrace();
        }
        return p;
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

    public List<Product> getProducts() {
        List<Product> ps = new ArrayList<>();
        try {
            ps.addAll(productDAO.listAll());
        } catch (DataAccessException e) {
            System.out.println("Unable to find products");
            e.printStackTrace();
        }
        return ps;
    }
}
