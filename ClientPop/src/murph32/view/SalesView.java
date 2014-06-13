package murph32.view;

import murph32.controller.SalesController;
import murph32.core.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

/**
 *
 */
public class SalesView {
    private User user = new User();
    private static JFrame f = new JFrame();
    private SalesController controller;
    private DefaultListModel<Invoice> invoiceModel = new DefaultListModel<>();
    private JList<Invoice> invoiceList = new JList<>(invoiceModel);
    private static HashMap<String, Invoice> invoice = new HashMap<>();

    private JLabel errorMessage = new JLabel("");

    private JTextField upcField;
    private JTextField quantityField;
    private JLabel subTotalCalc = new JLabel();
    private JLabel taxCalc = new JLabel();
    private JLabel totalCalc = new JLabel();

    public SalesView(User user) {
        this.user = user;
        controller = new SalesController(this, user);

        //create GroupLayout Shell
        JPanel panel = new JPanel();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JPanel listPanel = new JPanel(new GridLayout());
        listPanel.add(invoicePanel());
        JPanel controlPanel = new JPanel(new GridLayout());
        controlPanel.add(controlPanel());
        JPanel navPane = new JPanel(new GridLayout());
        navPane.add(rightPanel());

        //horizontal layout
        layout.setHorizontalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(listPanel)
                                .addComponent(controlPanel))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(navPane))
        );
        //vertical layout
        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(listPanel)
                                .addComponent(navPane))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(controlPanel))
        );

        f.add(panel);
        f.setTitle("Sales Display");
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
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
        invoiceList.setFont(new Font("Courier New", Font.PLAIN, 12));
        invoiceList.setVisibleRowCount(10);
        invoiceList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                Invoice i = invoiceList.getSelectedValue();
                if (i != null) {
                    upcField.setText(i.getUpc());
                    quantityField.setText(String.valueOf(i.getNumInStock()));
                } else {
                    upcField.setText("");
                    quantityField.setText("");
                }
            }
        });
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

    private JPanel controlPanel() {
        JPanel panel = new JPanel();
        GroupLayout controlPane = new GroupLayout(panel);
        panel.setLayout(controlPane);

        Font font = errorMessage.getFont();
        errorMessage.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()));
        errorMessage.setForeground(Color.RED);

        JPanel controlInput = new JPanel(new GridLayout());
        controlInput.add(controlInputPanel());
        JPanel controlButtons = new JPanel(new GridLayout());
        controlButtons.add(controlOpPanel());

        //horizontal plane
        controlPane.setHorizontalGroup(controlPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(errorMessage)
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(controlPane.createSequentialGroup()
                                        .addComponent(controlInput)
                                        .addComponent(controlButtons)))
        );
        //vertical plane
        controlPane.setVerticalGroup(controlPane.createSequentialGroup()
                        .addComponent(errorMessage, 15, 15, 15)
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(controlInput)
                                .addComponent(controlButtons))
        );
        return panel;
    }

    private JPanel controlOpPanel() {
        JPanel panel = new JPanel();
        GroupLayout controlButtonPane = new GroupLayout(panel);
        panel.setLayout(controlButtonPane);


        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.addProduct(upcField.getText(), quantityField.getText());
            }
        });

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.removeProduct(upcField.getText(), quantityField.getText());
            }
        });

        //horizontal plane
        controlButtonPane.setHorizontalGroup(controlButtonPane.createParallelGroup()
                        .addComponent(addButton)
                        .addComponent(removeButton)
        );
        controlButtonPane.linkSize(SwingConstants.HORIZONTAL, addButton, removeButton);

        //vertical plane
        controlButtonPane.setVerticalGroup(controlButtonPane.createSequentialGroup()
                        .addComponent(addButton)
                        .addComponent(removeButton)
        );
        return panel;
    }

    private JPanel controlInputPanel() {
        JPanel panel = new JPanel();
        GroupLayout inputPane = new GroupLayout(panel);
        panel.setLayout(inputPane);

        JLabel upcLabel = new JLabel("UPC:");
        JLabel quantityLabel = new JLabel("Quantity:");

        upcField = new JTextField();
        quantityField = new JTextField();

        //horizontal plane
        inputPane.setHorizontalGroup(inputPane.createSequentialGroup()
                        .addGroup(inputPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(upcLabel)
                                .addComponent(quantityLabel))
                        .addGroup(inputPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(upcField, 100, 100, 100)
                                .addComponent(quantityField))
        );
        inputPane.linkSize(SwingConstants.HORIZONTAL, upcField, quantityField);

        //vertical plane
        inputPane.setVerticalGroup(inputPane.createSequentialGroup()
                        .addGroup(inputPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(upcLabel)
                                .addComponent(upcField))
                        .addGroup(inputPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(quantityLabel)
                                .addComponent(quantityField))
        );
        inputPane.linkSize(SwingConstants.VERTICAL, upcField, quantityField);
        return panel;
    }

    private JPanel rightPanel() {
        JPanel panel = new JPanel();

        GroupLayout rightPane = new GroupLayout(panel);
        panel.setLayout(rightPane);

        JButton adminButton = new JButton("Admin");
        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.adminOperation();
            }
        });
        JButton finishButton = new JButton("Finish");
        finishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.finishOperation();
            }
        });
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.exitOperation();
            }
        });

        //horizontal plane (top down)
        rightPane.setHorizontalGroup(rightPane.createParallelGroup()
                        .addComponent(adminButton)
                        .addComponent(finishButton)
                        .addComponent(exitButton)
        );
        rightPane.linkSize(adminButton, finishButton, exitButton);

        //vertical plane (rows)
        rightPane.setVerticalGroup(rightPane.createSequentialGroup()
                .addComponent(adminButton)
                .addComponent(finishButton)
                .addComponent(exitButton));
        return panel;
    }

    public DefaultListModel<Invoice> getInvoiceModel() {
        return invoiceModel;
    }

    // messages
    public void invMsgSubTotalCalc(String msg) {
        subTotalCalc.setText(msg);
    }

    public void invMsgTaxCalc(String msg) {
        taxCalc.setText(msg);
    }

    public void invMsgTotalCalc(String msg) {
        totalCalc.setText(msg);
    }

    public void errMsgReset() {
        errorMessage.setText("");
    }

    public void errMsgValidInteger() {
        errorMessage.setText("Value must be a valid Integer");
    }

    public void errMsgInvalidQuantity() {
        errorMessage.setText("Quantity must be between 1 and 999");
    }

    public void errMsgInvalidUpc() {
        errorMessage.setText("UPC is not in the invoice.");

    }

    public void errMsgProductNotFound(String upc) {
        errorMessage.setText("Product \"" + upc + "\" not found");
    }

    public static JFrame getFrame() {
        return f;
    }

}
