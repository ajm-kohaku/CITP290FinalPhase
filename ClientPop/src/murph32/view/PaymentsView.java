package murph32.view;

import murph32.core.Payment;
import murph32.core.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

/**
 *
 */
public class PaymentsView {
    private JPanel cards;

    private String accessLevel;

    private DefaultListModel<Invoice> invoiceModel = new DefaultListModel<>();
    private JList<Invoice> invoiceList = new JList<>(invoiceModel);

    private static HashMap<Integer, Payment> payments = new HashMap<>();
    private DefaultListModel<Payment> paymentModel = new DefaultListModel<>();
    private JList<Payment> paymentList = new JList<>(paymentModel);

    private Font scrollFont = new Font("Courier New", Font.PLAIN, 12);

    private JLabel amountDueCalc;
    private JLabel amountReceivedCalc;
    private JLabel errorMessage = new JLabel("");

    private JTextField amountField;
    private final DefaultComboBoxModel paymentTypes = new DefaultComboBoxModel();
    private final JComboBox paymentTypeBox = new JComboBox(paymentTypes);

    private JTextField routingNbrField;
    private JTextField accountNbrField;
    private JTextField checkNbrField;

    private JTextField cardNbrField;
    private JTextField exprDateField;

    private JButton addButton = new JButton("Add");
    private JButton removeButton = new JButton("Remove");

    private JButton adminButton = new JButton("Admin");
    private JButton salesButton = new JButton("Sales");
    private JButton exitButton = new JButton("Exit");

    private JLabel subTotalCalc = new JLabel();
    private JLabel taxCalc = new JLabel();
    private JLabel totalCalc = new JLabel();
    private static JFrame f = new JFrame();
    private HashMap<String, Invoice> invoice;
    private User user;

    public PaymentsView(HashMap<String, Invoice> invoice, User user) {
        this.invoice = invoice;
        this.user = user;

        //create GroupLayout Shell
        JPanel panel = new JPanel();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        this.user = user;
        accessLevel = user.getAccessLevel();

        this.invoice = invoice;
        this.initializeInvoiceList();


//        JPanel invoiceListPanel = new JPanel(new GridLayout());
//        invoiceListPanel.add(invoicePanel());
        JPanel paymentListPanel = new JPanel(new GridLayout());
        paymentListPanel.add(paymentPanel());
        JPanel controlPanel = new JPanel(new GridLayout());
        controlPanel.add(controlPanel());
        JPanel navPanel = new JPanel(new GridLayout());
        navPanel.add(rightPanel());

        /*
        paymentList rightNav
        controlPanel


         */
        //horizontal layout
        layout.setHorizontalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(paymentListPanel)
                                .addComponent(controlPanel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(navPanel))
        );
        //vertical layout
        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(paymentListPanel)
                                        .addComponent(navPanel)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(controlPanel))
        );
        f.add(panel);
        f.setTitle("Payments");
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
    }

    public static JFrame getFrame() {
        return f;
    }

    private void initializeInvoiceList() {
        for (Invoice i : invoice.values()) {
            invoiceModel.addElement(i);
        }
    }

    private JPanel rightPanel() {
        JPanel panel = new JPanel();

        GroupLayout rightPane = new GroupLayout(panel);
        panel.setLayout(rightPane);

        adminButton.setName("Admin");
        adminButton.addActionListener(new ButtonListener());
        salesButton.setName("Sales");
        salesButton.addActionListener(new ButtonListener());
        exitButton.setName("Exit");
        exitButton.addActionListener(new ButtonListener());

        //horizontal plane (top down)
        rightPane.setHorizontalGroup(rightPane.createParallelGroup()
                        .addComponent(adminButton)
                        .addComponent(salesButton)
                        .addComponent(exitButton)
        );
        rightPane.linkSize(adminButton, salesButton, exitButton);

        //vertical plane (rows)
        rightPane.setVerticalGroup(rightPane.createSequentialGroup()
                .addComponent(adminButton)
                .addComponent(salesButton)
                .addComponent(exitButton));
        return panel;
    }

    private JPanel invoicePanel() {
        JPanel panel = new JPanel();
        GroupLayout invoicePane = new GroupLayout(panel);
        panel.setLayout(invoicePane);

        JScrollPane scrollPane = new JScrollPane(invoiceList);
        scrollPane.add(invoiceScroll());

        JPanel subTotalPane = new JPanel(new GridLayout());
        subTotalPane.add(invoiceSubTotal());

        JLabel upcLabel = new JLabel(" UPC");
        JLabel descriptionLabel = new JLabel("Description");
        JLabel priceLabel = new JLabel("Price");
        JLabel quantityLabel = new JLabel("  Qty");
        JLabel itemTotLabel = new JLabel("Item Total");

        Font font = new Font("Courier New", Font.BOLD, 12);
        upcLabel.setFont(font);
        descriptionLabel.setFont(font);
        priceLabel.setFont(font);
        quantityLabel.setFont(font);
        itemTotLabel.setFont(font);

        //horizontal plane
        invoicePane.setHorizontalGroup(invoicePane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(invoicePane.createSequentialGroup()
                                .addComponent(upcLabel, 75, 75, 75)
                                .addComponent(descriptionLabel, 228, 228, 228)
                                .addComponent(priceLabel, 60, 60, 60)
                                .addComponent(quantityLabel, 50, 50, 50)
                                .addComponent(itemTotLabel, 115, 115, 115))
                        .addGroup(invoicePane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(scrollPane))
                        .addComponent(subTotalPane)
        );
        //vertical plane
        invoicePane.setVerticalGroup(invoicePane.createSequentialGroup()
                        .addGroup(invoicePane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(upcLabel)
                                .addComponent(descriptionLabel)
                                .addComponent(priceLabel)
                                .addComponent(quantityLabel)
                                .addComponent(itemTotLabel))
                        .addGroup(invoicePane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(scrollPane))
                        .addGroup(invoicePane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(subTotalPane))
        );

        return panel;
    }

    private JScrollPane invoiceScroll() {
        JScrollPane panel = new JScrollPane();
        invoiceList.setFont(scrollFont);
        invoiceList.setVisibleRowCount(10);
        return panel;
    }

    private JPanel invoiceSubTotal() {
        JPanel panel = new JPanel();
        GroupLayout subPane = new GroupLayout(panel);
        panel.setLayout(subPane);

        JLabel subTotalLabel = new JLabel("SubTotal: ");
        JLabel taxLabel = new JLabel("6% Tax: ");
        JLabel totalLabel = new JLabel("Total: ");


        //horizontal
        subPane.setHorizontalGroup(subPane.createSequentialGroup()
                        .addGroup(subPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(subTotalLabel)
                                .addComponent(taxLabel)
                                .addComponent(totalLabel))
                        .addGroup(subPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(subTotalCalc)
                                .addComponent(taxCalc)
                                .addComponent(totalCalc))
        );
        //vertical
        subPane.setVerticalGroup(subPane.createSequentialGroup()
                        .addGroup(subPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(subTotalLabel)
                                .addComponent(subTotalCalc))
                        .addGroup(subPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(taxLabel)
                                .addComponent(taxCalc))
                        .addGroup(subPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(totalLabel)
                                .addComponent(totalCalc))
        );

        return panel;
    }

    private JPanel paymentPanel() {
        JPanel panel = new JPanel();
        GroupLayout paymentPane = new GroupLayout(panel);
        panel.setLayout(paymentPane);

        JScrollPane scrollPane = new JScrollPane(paymentList);
        scrollPane.add(paymentScroll());

        JPanel paymentSummaryPane = new JPanel(new GridLayout());
        paymentSummaryPane.add(paymentSummary());

        JLabel paymentIDLabel = new JLabel(" Payment ID");
        JLabel paymentTypeLabel = new JLabel("Type");
        JLabel paymentAmountLabel = new JLabel("Amount");

        Font font = new Font("Courier New", Font.BOLD, 12);
        paymentIDLabel.setFont(font);
        paymentTypeLabel.setFont(font);
        paymentAmountLabel.setFont(font);

        //horizontal plane
        paymentPane.setHorizontalGroup(paymentPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(paymentPane.createSequentialGroup()
                                .addComponent(paymentIDLabel, 100, 100, 100)
                                .addComponent(paymentTypeLabel, 80, 80, 80)
                                .addComponent(paymentAmountLabel, 60, 60, 60))
                        .addGroup(paymentPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(scrollPane))
                        .addComponent(paymentSummaryPane)
        );
        //vertical plane
        paymentPane.setVerticalGroup(paymentPane.createSequentialGroup()
                        .addGroup(paymentPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(paymentIDLabel)
                                .addComponent(paymentTypeLabel)
                                .addComponent(paymentAmountLabel))
                        .addGroup(paymentPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(scrollPane))
                        .addGroup(paymentPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(paymentSummaryPane))
        );

        return panel;
    }

    private JScrollPane paymentScroll() {
        JScrollPane panel = new JScrollPane();
        ListSelectionListener listListener = new ListLisenter();
        paymentList.setFont(scrollFont);
        paymentList.setVisibleRowCount(10);
        paymentList.addListSelectionListener(listListener);
        return panel;
    }

    private JPanel paymentSummary() {
        JPanel panel = new JPanel();
        GroupLayout subPane = new GroupLayout(panel);
        panel.setLayout(subPane);

        JLabel dueLabel = new JLabel("Amount Due: ");
        JLabel receivedLabel = new JLabel("Amount Received: ");
        amountDueCalc = new JLabel("");
        amountReceivedCalc = new JLabel("");

        //horizontal
        subPane.setHorizontalGroup(subPane.createSequentialGroup()
                        .addGroup(subPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(dueLabel)
                                .addComponent(receivedLabel))
                        .addGroup(subPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(amountDueCalc)
                                .addComponent(amountReceivedCalc))
        );
        //vertical
        subPane.setVerticalGroup(subPane.createSequentialGroup()
                        .addGroup(subPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(dueLabel)
                                .addComponent(amountDueCalc))
                        .addGroup(subPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(receivedLabel)
                                .addComponent(amountReceivedCalc))
        );

        return panel;
    }

    private JPanel controlButtonPanel() {
        JPanel panel = new JPanel();
        GroupLayout controlButtonPane = new GroupLayout(panel);
        panel.setLayout(controlButtonPane);

        //put the combobox in a jpanel
        JPanel comboBoxPane = new JPanel();

        paymentTypes.addElement("Payment Method");
        paymentTypes.addElement("Cash");
        paymentTypes.addElement("Check");
        paymentTypes.addElement("Credit");

        paymentTypeBox.setEditable(false);
        paymentTypeBox.addItemListener(new BoxListener());
        comboBoxPane.add(paymentTypeBox);

        addButton.setName("Add");
        addButton.addActionListener(new ButtonListener());
        removeButton.setName("Remove");
        removeButton.addActionListener(new ButtonListener());

        //horizontal plane
        controlButtonPane.setHorizontalGroup(controlButtonPane.createParallelGroup()
                        .addComponent(paymentTypeBox)
                        .addComponent(addButton)
                        .addComponent(removeButton)
        );
        controlButtonPane.linkSize(SwingConstants.HORIZONTAL, addButton, removeButton);

        //vertical plane
        controlButtonPane.setVerticalGroup(controlButtonPane.createSequentialGroup()
                        .addComponent(paymentTypeBox)
                        .addComponent(addButton)
                        .addComponent(removeButton)
        );
        controlButtonPane.linkSize(SwingConstants.VERTICAL, addButton, removeButton, paymentTypeBox);

        return panel;
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JComponent source = (JButton) actionEvent.getSource();

            switch (source.getName()) {
                case "Add":
                    addPayment();
                    break;
                case "Remove":
                    removePayment();
                    break;
                case "Admin":
                    adminOperation();
                    break;
                case "Sales":
                    salesOperation();
                    break;
                case "Exit":
                    f.dispose();

                    break;
            }
        }
    }

    private void salesOperation() {
        SalesView.getFrame();
        f.dispose();
    }

    private void adminOperation() {
        if (accessLevel.equalsIgnoreCase("manager")) {
            new AdminFrame(user).setVisible(true);
        } else {
            LoginView.getFrame();
        }
        f.dispose();
    }

    private void removePayment() {

    }

    private void addPayment() {

    }

    private class ListLisenter implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            Payment p = paymentList.getSelectedValue();
            if (p != null) {
                amountField.setText(String.valueOf(p.getAmount()));
                paymentTypeBox.getItemAt(paymentTypeBox.getSelectedIndex());
            }
        }
    }

    private class BoxListener implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent itemEvent) {
            CardLayout cl = (CardLayout) (cards.getLayout());
            cl.show(cards, (String) itemEvent.getItem());
        }
    }

    private JPanel controlInputPanel() {
        JPanel panel = new JPanel();

        //create the "cards"
        JPanel defaultCard = new JPanel();
        defaultCard.add(controlDefaultInputPanel());

        JPanel cashCard = new JPanel();
        cashCard.add(controlCashInputPanel());

        JPanel checkCard = new JPanel();
        checkCard.add(controlCheckInputPanel());

        JPanel creditCard = new JPanel();
        creditCard.add(controlCreditInputPanel());

        //create the panel that contains the "cards".
        cards = new JPanel(new CardLayout());
        cards.add(defaultCard, "Payment Method");
        cards.add(cashCard, "Cash");
        cards.add(checkCard, "Check");
        cards.add(creditCard, "Credit");

        panel.add(cards, BorderLayout.CENTER);

        return panel;
    }

    private JPanel controlPanel() {
        JPanel panel = new JPanel();
        GroupLayout controlPane = new GroupLayout(panel);
        panel.setLayout(controlPane);

        Font font = errorMessage.getFont();
        errorMessage.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()));
        errorMessage.setForeground(Color.RED);

        JPanel controlButton = new JPanel(new GridLayout());
        controlButton.add(controlButtonPanel());
        JPanel inputPanel = new JPanel(new GridLayout());
        inputPanel.add(controlInputPanel());

        //horizontal plane
        controlPane.setHorizontalGroup(controlPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(errorMessage)
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(controlPane.createSequentialGroup()
                                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                                .addComponent(inputPanel))
                                        .addComponent(controlButton)))
        );
        //vertical plane
        controlPane.setVerticalGroup(controlPane.createSequentialGroup()
                        .addComponent(errorMessage, 15, 15, 15)
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(inputPanel))
                                .addComponent(controlButton))
        );

        return panel;
    }

    private JPanel controlDefaultInputPanel() {
        JPanel panel = new JPanel();

        JLabel defaultText = new JLabel("Please select a payment option.");
        panel.add(defaultText);

        return panel;
    }

    private JPanel controlCashInputPanel() {
        JPanel panel = new JPanel();
        GroupLayout controlPane = new GroupLayout(panel);
        panel.setLayout(controlPane);

        JLabel amountLabel = new JLabel("Amount:");
        amountField = new JTextField();

        //horizontal plane
        controlPane.setHorizontalGroup(controlPane.createSequentialGroup()
                        .addComponent(amountLabel)
                        .addComponent(amountField, 150, 150, 150)
        );
        //vertical plane
        controlPane.setVerticalGroup(controlPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(amountLabel)
                        .addComponent(amountField)
        );
        return panel;
    }

    private JPanel controlCheckInputPanel() {
        JPanel panel = new JPanel();
        GroupLayout controlPane = new GroupLayout(panel);
        panel.setLayout(controlPane);

        JLabel amountLabel = new JLabel("Amount:");
        JLabel routingNbrLabel = new JLabel("Routing #:");
        JLabel accountNbrLabel = new JLabel("Account #:");
        JLabel checkNbrLabel = new JLabel("Check #:");

        amountField = new JTextField();
        routingNbrField = new JTextField();
        accountNbrField = new JTextField();
        checkNbrField = new JTextField();

        //horizontal plane
        controlPane.setHorizontalGroup(controlPane.createSequentialGroup()
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(amountLabel)
                                .addComponent(routingNbrLabel))
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(amountField, 150, 150, 150)
                                .addComponent(routingNbrField))
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(accountNbrLabel)
                                .addComponent(checkNbrLabel))
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(accountNbrField)
                                .addComponent(checkNbrField))
        );
        controlPane.linkSize(amountField, routingNbrField, accountNbrField, checkNbrField);
        //vertical plane
        controlPane.setVerticalGroup(controlPane.createSequentialGroup()
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(amountLabel)
                                .addComponent(amountField)
                                .addComponent(accountNbrLabel)
                                .addComponent(accountNbrField))
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(routingNbrLabel)
                                .addComponent(routingNbrField)
                                .addComponent(checkNbrLabel)
                                .addComponent(checkNbrField))
        );
        return panel;
    }

    private JPanel controlCreditInputPanel() {
        JPanel panel = new JPanel();
        GroupLayout controlPane = new GroupLayout(panel);
        panel.setLayout(controlPane);

        JLabel amountLabel = new JLabel("Amount:");
        JLabel cardNbrLabel = new JLabel("Card #:");
        JLabel expireDateLabel = new JLabel("Expiration Date:");

        amountField = new JTextField();
        cardNbrField = new JTextField();
        exprDateField = new JTextField();

        controlPane.setHorizontalGroup(controlPane.createSequentialGroup()
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(amountLabel)
                                .addComponent(cardNbrLabel)
                                .addComponent(expireDateLabel))
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(amountField, 150, 150, 150)
                                .addComponent(cardNbrField)
                                .addComponent(exprDateField))
        );
        controlPane.linkSize(amountField, cardNbrField, exprDateField);

        controlPane.setVerticalGroup(controlPane.createSequentialGroup()
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(amountLabel)
                                .addComponent(amountField))
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(cardNbrLabel)
                                .addComponent(cardNbrField))
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(expireDateLabel)
                                .addComponent(exprDateField))
        );
        return panel;
    }
}
