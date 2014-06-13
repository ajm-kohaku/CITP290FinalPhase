package murph32.controller;

import murph32.core.Product;
import murph32.core.User;
import murph32.model.InventoryModel;
import murph32.view.InventoryView;
import murph32.view.UserMaintView;

import javax.swing.*;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 * <code>InventoryController</code> class is the workhorse of the Inventory management display.
 * reviewed phase 3 solution on way to split controller type operations from the view.
 * Also added data constraints.
 * @author Amber Murphy
 * @author hoffmanz
 * @author Dave Smith (Vertafore)
 */
public class InventoryController {
    private InventoryView view;
    private InventoryModel model = new InventoryModel();
    private DefaultListModel<Product> productModel;
    private User user;

    public InventoryController(InventoryView view, User user) {
        this.view = view;
        this.user = user;
        this.productModel = view.getProductModel();
    }

    /**
     * <code>getInventory</code> method retrieves a HashMap of the Inventory from the model class.
     *
     * @return HashMap of inventory.
     */
    public HashMap<String, Product> getInventory() {
        return model.getProducts();
    }


    public void salesDisplay() {
        InventoryView.getFrame().dispose();
    }

    public void userMaintenance() {
        new UserMaintView(user);
        InventoryView.getFrame().dispose();
    }

    /**
     * <code>removeProduct</code> method accesses the Model layer and removes the upc.
     *
     * @param upc: upc to be removed.
     */
    public void removeProduct(String upc) {
        if (validateUpc(upc) != null) {
            model.removeProduct(upc);
            productModel.remove(getIndex(upc));
            view.errMsgRest();
        } else {
            view.errMsgRemoveProduct();
        }
    }

    /**
     * <code>updateProduct</code> method finds the existing UPC
     * and updates the description, price, and number in stock.
     *
     * @param upc:         upc to be updated. cannot be changed.
     * @param description: description of the product.
     * @param price:       price of the product (in BigDecimal)
     * @param numInStock:  number of items in stock.
     */
    public void updateProduct(String upc, String description, String price, String numInStock) {
        if (model.getProducts().containsKey(upc) &&
                validateUpc(upc) != null &&
                validateDescription(description) != null &&
                validatePrice(price) != null &&
                validateStock(numInStock) != -1) {
            Product p = new Product();
            p.setUpc(upc);
            p.setDescription(description);
            p.setPrice(validatePrice(price));
            p.setNumInStock(validateStock(numInStock));
            model.updateProduct(p);
            productModel.set(getIndex(upc), p);
            view.errMsgRest();
        } else {
            view.errMsgUpdateProduct();
        }
    }

    /**
     * <code>addProduct</code> method adds new product if no results from the model layer.
     *
     * @param upc:         upc to be added.
     * @param description: description of the product.
     * @param price:       price of the product (in BigDecimal)
     * @param numInStock:  number of items in stock.
     */
    public void addProduct(String upc, String description, String price, String numInStock) {
        if (!model.getProducts().containsKey(upc) &&
                validateUpc(upc) != null &&
                validateDescription(description) != null &&
                validatePrice(price) != null &&
                validateStock(numInStock) != -1) {
            Product p = new Product();
            p.setUpc(upc);
            p.setDescription(description);
            p.setPrice(validatePrice(price));
            p.setNumInStock(validateStock(numInStock));
            model.addProduct(p);
            productModel.addElement(p);
            view.errMsgRest();
        } else {
            view.errMsgAddProduct();
        }
    }

    /**
     * helper method to verify upc follows data requirements
     *
     * @param upc: upc to be verified.
     * @return upc
     */
    private String validateUpc(String upc) {
        if (upc.length() != 8 ||
                upc.matches("[^0-9]+")) {
            view.errMsgInvalidUpc();
            upc = null;
        }
        return upc;
    }

    /**
     * helper method to verify description follows data requirements
     *
     * @param description: description of the product.
     * @return description
     */
    private String validateDescription(String description) {
        if (description.length() > 30) {
            view.errMsgInvalidDescription();
            description = null;
        }
        return description;
    }

    /**
     * helper method to verify price follows data requirements
     *
     * @param price: price of the product (as a String)
     * @return price as BigDecimal
     */
    private BigDecimal validatePrice(String price) {
        BigDecimal bdPrice;

        try {
            bdPrice = new BigDecimal(price);
        } catch (NumberFormatException e) {
            view.errMsgInvalidPrice();
            bdPrice = null;
        }
        if (bdPrice != null &&
                (bdPrice.compareTo(new BigDecimal(25000)) > 0 ||
                        bdPrice.compareTo(new BigDecimal(-25000)) < 0)) {
            view.errMsgInvalidPrice();
            bdPrice = null;
        }
        return bdPrice;
    }

    /**
     * helper method to verify number in stock follows data requirements
     *
     * @param stock: number of items in stock
     * @return int value of number in stock.
     */
    private int validateStock(String stock) {
        int numbStock = -1;

        try {
            numbStock = Integer.parseInt(stock);
        } catch (NumberFormatException e) {
            view.errMsgInvalidStock();
        }
        if (numbStock < 0 || numbStock > 999) {
            view.errMsgInvalidStock();
            numbStock = -1;
        }
        return numbStock;
    }

    /**
     * helper method to get the index of the JList. this is more accurate than JList.getSelectedIndex
     *
     * @param upc: upc of the indexed item.
     * @return index of passed upc.
     */
    //got help from Dave Smith to find the index of invoiceModel.
    private int getIndex(String upc) {
        int index = -1;

        for (int j = 0; j < productModel.size(); j++) {
            if (productModel.get(j).getUpc().equals(upc)) {
                index = j;
                break;
            }
        }
        return index;
    }
}
