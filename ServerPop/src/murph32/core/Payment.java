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


import java.text.NumberFormat;

/**
 * <code>Payment</code> superclass class defines the basic information of a payment.
 * This class was created during class.
 * The constant "TAX_RATE" is defined here as well as a variable for amount.
 * All methods in this class are auto-generated.
 *
 * @author hoffmanz
 */
public class Payment {

    public static final double TAX_RATE = .06;

    private int paymentID;
    private double amount;
    private String type;

    public Payment() {

    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(int paymentID) {
        this.paymentID = paymentID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Payment(double amount) {
        this.amount = amount;
    }

    public double getAmount() {

        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        NumberFormat nf = NumberFormat.getCurrencyInstance();

        sb.append(StringUtils.padWithSpaces(String.valueOf(getPaymentID()), 10));
        sb.append(StringUtils.padWithSpaces(getType(), 15));
        sb.append(StringUtils.padWithSpaces(String.valueOf(nf.format(getAmount())), 11));
        return sb.toString();
    }
}
