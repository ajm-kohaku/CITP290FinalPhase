package murph32.model;

import murph32.core.Product;

import java.util.HashMap;

/**
 * <code>SalesModel</code> translates the data from the server to the Sales display.
 */
public class SalesModel {
    private DataAccessObject<Product> sao = new ProductServerAO();

    public HashMap<String, Product> getProducts() {
        return sao.hashList();

    }
}
