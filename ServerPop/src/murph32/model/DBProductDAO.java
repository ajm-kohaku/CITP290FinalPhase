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
            "SELECT upc, description, price, num_stock FROM inventory";
    private static final String SELECT_PRODUCT =
            "SELECT upc, description, price, num_stock FROM inventory WHERE upc = ?";
    private static final String INSERT_PRODUCT =
            "INSERT INTO inventory (upc, description, price, num_stock) VALUES(?,?,?,?)";

    private static final String UPDATE_PRODUCT =
            "UPDATE inventory SET description = ?, price = ?, num_stock = ?" +
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
                        p.setNumInStock(rs.getInt("num_stock"));
                        up.add(p);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Can't create database connection.");
            System.out.println("Unable to execute query.");
            e.printStackTrace();
        }
        return up;
    }

    private HashMap<String, Product> readAllProductsHash() {
        HashMap<String, Product> up = new HashMap<>();
        List<Product> productList = readAllProducts();
        for (Product p : productList) {
            up.put(p.getUpc(), p);
        }
        return up;
    }

    @Override
    public void create(Product data) throws DataAccessException {
        Product product = read(data.getUpc());
        if (product.getUpc() == null) {
            try (Connection derbyCon = DriverManager
                    .getConnection(DERBY_CREATE_CONNECTION)) {
                try (PreparedStatement insertProduct = derbyCon
                        .prepareStatement(INSERT_PRODUCT)) {
                    insertProduct.setString(1, data.getUpc());
                    insertProduct.setString(2, data.getDescription());
                    insertProduct.setBigDecimal(3, data.getPrice());
                    insertProduct.setInt(4, data.getNumInStock());

                    insertProduct.execute();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("add Product called..");
        }

    }

    @Override
    public Product read(String key) throws DataAccessException {
        Product product = new Product();
        try (Connection derbyCon = DriverManager
                .getConnection(DERBY_CREATE_CONNECTION)) {
            try (PreparedStatement readProduct = derbyCon
                    .prepareStatement(SELECT_PRODUCT)) {
                readProduct.setString(1, key);
                try (ResultSet rs = readProduct.executeQuery()) {
                    if (rs.next()) {
                        product.setUpc(rs.getString("upc"));
                        product.setDescription(rs.getString("description"));
                        product.setPrice(rs.getBigDecimal("price"));
                        product.setNumInStock(rs.getInt("num_stock"));

                        System.out.println("\nStart List:\n");
                        System.out.println(product.getUpc() + "\t" + product.getDescription());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("read Product called..");
        return product;
    }

    @Override
    public void update(Product data) throws DataAccessException {
        Product product = read(data.getUpc());
        if (product != null) {
            try (Connection derbyCon = DriverManager
                    .getConnection(DERBY_CREATE_CONNECTION)) {
                try (PreparedStatement updateProduct = derbyCon
                        .prepareStatement(UPDATE_PRODUCT)) {
                    updateProduct.setString(1, data.getDescription());
                    updateProduct.setBigDecimal(2, data.getPrice());
                    updateProduct.setString(3, String.valueOf(data.getNumInStock()));
                    updateProduct.setString(4, product.getUpc());
                    updateProduct.execute();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(String key) throws DataAccessException {
        Product product = read(key);
        if (product != null) {
            try (Connection derbyCon = DriverManager
                    .getConnection(DERBY_CREATE_CONNECTION)) {
                try (PreparedStatement updateProduct = derbyCon
                        .prepareStatement(DELETE_PRODUCT)) {
                    updateProduct.setString(1, product.getUpc());

                    updateProduct.execute();

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<Product> listAll() throws DataAccessException {
        HashMap<String, Product> up = readAllProductsHash();
        return new ArrayList<>(up.values());
    }

    @Override
    public HashMap<String, Product> hashList() {
        return readAllProductsHash();
    }

}
