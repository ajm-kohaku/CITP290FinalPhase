package murph32.model;

import murph32.core.Product;
import murph32.core.Request;
import murph32.core.Response;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * sends CRUD(L) data to the server
 */
public class ProductServerAO implements DataAccessObject<Product> {
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

    @SuppressWarnings("unchecked")
    private List<Product> readAllProducts() {
        List<Product> us = new ArrayList<>();
        Response resp = accessServer(new Request("LIST PRODUCTS", null));
        if (resp.getHeader().equals("SUCCESS")) {


            us = (List<Product>) resp.getPayload();

        } else {
            //TODO: display (readAllUsers) error message
            System.out.println("Unable to read products");
        }
        return us;
    }

    private HashMap<String, Product> readAllProductsHash() {
        HashMap<String, Product> us = new HashMap<>();
        List<Product> productList = readAllProducts();
        for (Product u : productList) {
            us.put(u.getUpc(), u);
        }
        return us;
    }


    @Override
    public void create(Product data) throws DataAccessException {
        Product user = read(data.getUpc());
        if (user.getUpc() == null) {
            Response resp = accessServer(new Request("ADD PRODUCT", data));
            if (!resp.getHeader().equals("SUCCESS")) {
                //TODO: display (create) error message
                System.out.println("Product not created");
            } else {
                System.out.println("Product created");
            }
        }
    }

    @Override
    public Product read(String key) throws DataAccessException {
        Product user = new Product();
        Request rqst = new Request("READ PRODUCT", key);
        Response resp = accessServer(rqst);
        if (resp.getHeader().equals("SUCCESS")) {

            user = (Product) resp.getPayload();
        } else {
            //TODO: display (read) error message
            System.out.println("Unable to find username: " + key);
        }
        return user;
    }

    @Override
    public void update(Product data) throws DataAccessException {
        Product user = read(data.getUpc());
        if (user.getUpc() != null) {
            Response resp = accessServer(new Request("UPDATE PRODUCT", data));
            if (!resp.getHeader().equals("SUCCESS")) {
                //TODO: display (update) error message
            }
        }

    }

    @Override
    public void delete(String key) throws DataAccessException {
        Product user = read(key);
        if (user != null) {
            Response resp = accessServer(new Request("REMOVE PRODUCT", key));
            if (!resp.getHeader().equals("SUCCESS")) {
                //TODO: display (delete) error message
            }
        }
    }

    @Override
    public List<Product> listAll() throws DataAccessException {
        return readAllProducts();
    }

    @Override
    public HashMap<String, Product> hashList() {
        return readAllProductsHash();
    }
}
