package murph32.model;

import murph32.core.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dukat on 5/3/2014.
 */
public class DBProductDAO implements DataAccessObject<Product> {
    private static final String DERBY_DRIVER_CLASS = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String DERBY_CREATE_CONNECTION = "jdbc:derby:POPDB";

    //prepared statements
    private static final String SELECT_ALL_PRODUCTS =
            "SELECT upc, description, price, stock FROM inventory";
    private static final String SELECT_PRODUCT =
            "SELECT upc, description, price, stock FROM inventory WHERE upc = ?";
    private static final String INSERT_PRODUCT =
            "INSERT INTO inventory (upc, description, price, stock) VALUES(?,?,?,?)";

    private static final String UPDATE_PRODUCT =
            "UPDATE inventory SET description = ?, price = ?, stock = ?" +
                    "WHERE upc = ?";
    private static final String DELETE_PRODUCT =
            "DELETE FROM inventory WHERE upc = ?";

    public DBProductDAO() {
        try {
            Class.forName(DERBY_DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
            System.out.println("Unable to locate Derby database class.");
        }
    }

    //todo: finish converting this class to product..
    private List<Product> readAllProducts() {
        List<Product> up = new ArrayList<>();

        try (Connection derbyCon = DriverManager.getConnection(DERBY_CREATE_CONNECTION)) {
            try (PreparedStatement selectProducts = derbyCon
                    .prepareStatement(SELECT_ALL_PRODUCTS)) {
                try (ResultSet rs = selectProducts.executeQuery()) {
                    while (rs.next()) {
                        Product p = new Product();
                        p.setUpc(rs.getString("upc"));
                        p.setDescription(rs.getString("description"));
                        p.setPrice(rs.getBigDecimal("price"));
                        p.getNumInStock(rs.getInt("num_stock"));
                        up.add(p);
                    }
                    if (us.size() == 0) {
                        us.add(bootStrap());
                        try (PreparedStatement insertProduct = derbyCon
                                .prepareStatement(INSERT_PRODUCT)) {
                            insertProduct.setString(1, us.get(0).getProductname());
                            insertProduct.setString(2, new String(us.get(0).getPassword()));
                            insertProduct.setString(3, us.get(0).getAccessLevel());

                            insertProduct.execute();
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

    private HashMap<String, Product> readAllProductsHash() {
        HashMap<String, Product> us = new HashMap<>();
        List<Product> userList = readAllProducts();
        for (Product u : userList) {
            us.put(u.getProductname(), u);
        }
        return us;
    }

    @Override
    public void create(Product data) throws DataAccessException {
        Product user = readAllProductsHash().get(data.getProductname());
        if (user == null) {
            try (Connection derbyCon = DriverManager
                    .getConnection(DERBY_CREATE_CONNECTION)) {
                try (PreparedStatement insertProduct = derbyCon
                        .prepareStatement(INSERT_PRODUCT)) {
                    insertProduct.setString(1, data.getProductname());
                    insertProduct.setString(2, new String(data.getPassword()));
                    insertProduct.setString(3, data.getProductname());

                    insertProduct.execute();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public Product read(String key) throws DataAccessException {
        Product user = new Product();
        try (Connection derbyCon = DriverManager
                .getConnection(DERBY_CREATE_CONNECTION)) {
            try (PreparedStatement readProduct = derbyCon
                    .prepareStatement(SELECT_PRODUCT)) {
                readProduct.setString(1, key);
                try (ResultSet rs = readProduct.executeQuery()) {
                    while (rs.next()) {
                        user.setProductname(rs.getString("username"));
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
    public void update(Product data) throws DataAccessException {
        Product user = readAllProductsHash().get(data.getProductname());
        if (user != null) {
            try (Connection derbyCon = DriverManager
                    .getConnection(DERBY_CREATE_CONNECTION)) {
                try (PreparedStatement updateProduct = derbyCon
                        .prepareStatement(UPDATE_PRODUCT)) {
                    updateProduct.setString(1, user.getProductname());
                    updateProduct.setString(2, new String(data.getPassword()));
                    updateProduct.setString(3, data.getAccessLevel());
                    updateProduct.setString(4, user.getProductname());
                    updateProduct.execute();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(String key) throws DataAccessException {
        Product user = readAllProductsHash().get(key);
        if (user != null) {
            try (Connection derbyCon = DriverManager
                    .getConnection(DERBY_CREATE_CONNECTION)) {
                try (PreparedStatement updateProduct = derbyCon
                        .prepareStatement(DELETE_PRODUCT)) {
                    updateProduct.setString(1, user.getProductname());

                    updateProduct.execute();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Product> listAll() throws DataAccessException {
        HashMap<String, Product> us = readAllProductsHash();
        return new ArrayList<>(us.values());
    }

    @Override
    public HashMap<String, Product> hashList() {
        return readAllProductsHash();
    }

}
