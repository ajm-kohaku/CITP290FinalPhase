package murph32.controller;

import murph32.core.Payment;
import murph32.core.Product;
import murph32.core.User;
import murph32.model.SalesModel;
import murph32.view.*;

import javax.swing.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;

/**
 *
 */
public class SalesController {
    private static HashMap<String, Invoice> invoice = new HashMap<>();
    private SalesView view;
    private SalesModel model = new SalesModel();
    private DefaultListModel<Invoice> invoiceModel;
    private static User user;

    public SalesController(SalesView view, User user) {
        this.view = view;
        SalesController.user = user;
        invoiceModel = view.getInvoiceModel();
    }

    public void finishOperation() {
        new PaymentsView(invoice, user);
        SalesView.getFrame().dispose();
    }

    public void adminOperation() {
        if (user.getAccessLevel().equalsIgnoreCase("manager")) {
            new AdminFrame(user).setVisible(true);
        } else {
            new LoginView();
        }
    }

    public void exitOperation() {
        SalesView.getFrame().dispose();
    }

    //got help from Dave Smith to find the index of invoiceModel.
    private int getIndex(String upc) {
        int index = -1;

        for (int j = 0; j < invoiceModel.size(); j++) {
            if (invoiceModel.get(j).getUpc().equals(upc)) {
                index = j;
                break;
            }
        }
        return index;
    }

    private int getQty(String quantityField) {
        int quantity;
        if (quantityField.isEmpty()) {
            quantity = 1;
        } else {
            quantity = castInteger(quantityField);
        }

        return quantity;
    }

    private int castInteger(String integer) {
        int ci = 0;
        try {
            ci = Integer.valueOf(integer);
        } catch (NumberFormatException e) {
            view.errMsgValidInteger();
        }
        return ci;
    }

    private String getFormattedSubTotal() {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(getSubTotal());
    }

    private static BigDecimal getSubTotal() {
        BigDecimal itemTotal;
        BigDecimal subTotal = new BigDecimal(0);
        for (Invoice i : invoice.values()) {
            itemTotal = i.getPrice().multiply(new BigDecimal(i.getQuantity()));
            subTotal = itemTotal.add(subTotal);
        }
        return subTotal;
    }

    private String getFormattedFinalTotal() {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(getFinalTotal());

    }

    protected static BigDecimal getFinalTotal() {
        return getSubTotal().add(getTaxAmount());
    }

    private String getFormattedTaxAmount() {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(getTaxAmount());
    }

    private static BigDecimal getTaxAmount() {
        return getSubTotal().multiply(new BigDecimal(Payment.TAX_RATE));
    }

    public void addProduct(String upc, String quantity) {
        //TODO: relate quantity to stock if there's time.
        Invoice i = new Invoice();
        int index = getIndex(upc);

        if (model.getProducts().containsKey(upc)) {
            Product p = model.getProducts().get(upc);
            i.setUpc(p.getUpc());
            i.setDescription(p.getDescription());
            i.setQuantity(getQty(quantity));
            i.setPrice(p.getPrice());

            if (index == -1) {
                int newQty = i.getQuantity();
                if (newQty >= 1 && newQty <= 999) {
                    invoice.put(i.getUpc(), i);
                    invoiceModel.addElement(i);
                    view.invMsgSubTotalCalc(getFormattedSubTotal());
                    view.invMsgTaxCalc(getFormattedTaxAmount());
                    view.invMsgTotalCalc(getFormattedFinalTotal());
                    view.errMsgReset();
                } else {
                    view.errMsgInvalidQuantity();
                }

            } else {
                int newQty = i.getQuantity() + invoice.get(i.getUpc()).getQuantity();
                if (newQty >= 1 && newQty <= 999) {
                    i.setQuantity(newQty);
                    invoice.put(i.getUpc(), i);
                    invoiceModel.set(index, i);
                    view.invMsgSubTotalCalc(getFormattedSubTotal());
                    view.invMsgTaxCalc(getFormattedTaxAmount());
                    view.invMsgTotalCalc(getFormattedFinalTotal());
                    view.errMsgReset();
                } else {
                    view.errMsgInvalidQuantity();
                }

            }
        }
    }

    public void removeProduct(String upc, String quantity) {
        if (model.getProducts().containsKey(upc)) {
            int index = getIndex(upc);
            if (index == -1) {
                view.errMsgInvalidUpc();
            } else if (index > -1) {
                Invoice i = invoiceModel.get(index);
                int newQty = i.getQuantity() - getQty(quantity);
                if (newQty > 0) {
                    i.setQuantity(newQty);

                    invoice.put(i.getUpc(), i);
                    invoiceModel.set(index, i);

                } else {
                    invoice.remove(i.getUpc());
                    invoiceModel.remove(index);
                }
                view.invMsgSubTotalCalc(getFormattedSubTotal());
                view.invMsgTaxCalc(getFormattedTaxAmount());
                view.invMsgTotalCalc(getFormattedFinalTotal());
                view.errMsgReset();
            } else {
                view.errMsgInvalidUpc();
            }
        } else {
            view.errMsgProductNotFound(upc);
        }
    }
}
