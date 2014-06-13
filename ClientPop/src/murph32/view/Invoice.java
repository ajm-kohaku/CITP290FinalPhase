package murph32.view;

import murph32.core.Product;
import murph32.core.StringUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by Dukat on 5/7/2014.
 */
public class Invoice extends Product {
    public Invoice() {
    }

    public Invoice(String upc, String description, BigDecimal price, int quantity, int numInStock) {
        super(upc, description, price, quantity, numInStock);
    }

    public Invoice(String s, String s1, Object o, Object o1, Object o2) {
        super(s, s1, o, o1, o2);
    }

    public Invoice(Product p) {
        super(p);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        NumberFormat nf = NumberFormat.getCurrencyInstance();

        sb.append(StringUtils.padWithSpaces(getUpc(), 10));
        sb.append(StringUtils.padWithSpaces(getDescription(), 32));
        sb.append(StringUtils.padWithSpaces(String.valueOf(nf.format(getPrice())), 11));
        sb.append(StringUtils.padWithSpaces(String.valueOf(getQuantity()), 5));
        sb.append(StringUtils.padWithSpaces(String.valueOf(nf.format(getPrice().multiply(new BigDecimal(getQuantity())))), 15));
        return sb.toString();
    }
}

