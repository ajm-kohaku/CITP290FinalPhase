package murph32.model;

import murph32.core.Product;

import java.util.HashMap;

/**
 * <code>InventoryModel</code> class is the model layer for the Inventory management display.
 * <br>used phase 3 solution layout to break out model layer from view</br>
 *
 * @author Amber Murphy
 * @author hoffmanz
 */
public class InventoryModel {
    private DataAccessObject<Product> sao = new ProductServerAO();

    public HashMap<String, Product> getProducts() {
        return sao.hashList();

    }

    public Product getProduct(String upc) {
        Product p = new Product();
        try {
            sao.read(upc);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return p;
    }

    /**
     * <code>addProduct</code> method calls the ServerAccessObject to create new product.
     *
     * @param p: The product object to add.
     */
    public void addProduct(Product p) {
        try {
            sao.create(p);
        } catch (DataAccessException e) {
            System.out.println("(Inventory Model) Unable to create product");
            e.printStackTrace();
        }
    }

    /**
     * <code>updateProduct</code> method calls the ServerAccessObject to update an existing product.
     *
     * @param product: the product to be updated.
     */
    public void updateProduct(Product product) {
        try {
            Product p = sao.read(product.getUpc());
            p.setDescription(product.getDescription());
            p.setPrice(product.getPrice());
            p.setNumInStock(product.getNumInStock());
            sao.update(p);
        } catch (DataAccessException e) {
            System.out.println("(Inventory Model) Unable to update product");
            e.printStackTrace();
        }
    }

    /**
     * <code>removeProduct</code> method calls the ServerAccessObject to remove an existing product.
     *
     * @param upc; the upc of the product to be deleted.
     */
    public void removeProduct(String upc) {
        try {
            sao.delete(upc);
        } catch (DataAccessException e) {
            System.out.println("Unable to delete product");
            e.printStackTrace();
        }
    }
}
