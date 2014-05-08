/*
* Programmer:   Amber Murphy
* TUID:         MURPH32
* Course:       CITP 290: Spring 2014
* Main Program: PopApp.java
* Assignment:   Phase 3
* Summary:      Create GUI for phase 2 content.
*
*/
package murph32.core;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * <code>Product</code> super-class class.
 * This class was created during class.
 * This class creates an object to define a product.
 * Most methods in this class are auto-generated.
 * <br><code>toDataString</code> and <code>printProduct</code> methods are based on class examples.
 * <br>String <code>upc</code> stores the UPC for the product.
 * <br>String <code>description</code> stores the description.
 * <br>double <code>price</code> stores the price.
 * <br>int <code>description</code> stores the quantity.
 * <br>int <code>numInStock</code> stores the Number of items in stock (this isn't currently being used).
 *
 * @author hoffmanz
 * @author Amber Murphy
 */
public class Product implements Serializable {
    private static final long serialVersionUID = 8023480059884662475L;
    private String upc;
    private String description;
    private BigDecimal price;
    private int quantity;
    private int numInStock;

    public Product() {

    }

    public Product(String upc, String description, BigDecimal price, int quantity, int numInStock) {
        this.upc = upc;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.numInStock = numInStock;
    }

    public Product(String s, String s1, Object o, Object o1, Object o2) {
    }

    public Product(Product p) {
    }

    public Product(String personData) {
        String[] data = personData.split("\t", -1);
        upc = data[0];
        description = data[1];
        price = new BigDecimal(data[2]);
        quantity = Integer.parseInt(data[3]);
        numInStock = Integer.parseInt(data[4]);
    }

    /**
     * This method creates a screen friendly display of the Product.
     *
     * @param p
     * @return print
     */
    public static String printProduct(Product p) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();

        String print = p.getUpc() + "| "
                + p.getDescription() + "| "
                + nf.format(p.getPrice()) + "| "
                + p.getNumInStock();
        return print;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getNumInStock() {
        return numInStock;
    }

    public void setNumInStock(int numInStock) {
        this.numInStock = numInStock;
    }

    /**
     * creates a string to output to a file.
     * this was taken directly from class example.
     *
     * @return print
     */
    public String toDataString() {
        return upc + "\t" +
                description + "\t" +
                price + "\t" +
                quantity + "\t" +
                numInStock;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        NumberFormat nf = NumberFormat.getCurrencyInstance();

        sb.append(StringUtils.padWithSpaces(upc, 10));
        sb.append(StringUtils.padWithSpaces(description, 32));
        sb.append(StringUtils.padWithSpaces(String.valueOf(nf.format(price)), 11));
        sb.append(StringUtils.padWithSpaces(String.valueOf(numInStock), 5));
        return sb.toString();
    }
}
