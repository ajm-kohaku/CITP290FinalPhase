package murph32.view;

import murph32.controller.InventoryController;
import murph32.core.Product;
import murph32.core.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * <code>InventoryView</code> class that manages and displays the current Inventory in the database.
 * <br>Reviewed phase 3 solution from class and combined Zach's solution with my own.
 * concepts were borrowed and then split out into my style of coding. </br>
 *
 * @author Amber Murphy
 * @author hoffmanz
 */
public class InventoryView {
    private static JFrame f = new JFrame();
    private static User user;
    private DefaultListModel<Product> productModel = new DefaultListModel<>();
    private JList<Product> productList = new JList<>(productModel);
    private JLabel errorMessage = new JLabel("");
    private InventoryController controller;

    private JTextField upcField;
    private JTextField descriptionField;
    private JTextField priceField;
    private JTextField numberInStockField;

    /**
     * <code>InventoryView</code> constructor for the class. Contains the Swing layout that displays the GUI.
     *
     * @param user : the User object passed from the login.
     */
    public InventoryView(User user) {
        controller = new InventoryController(this, user);
        controller.getInventory();
        InventoryView.user = user;
        this.initializeList();
        //create GroupLayout Shell
        JPanel panel = new JPanel();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        //frame guts
        JPanel listPanel = new JPanel(new GridLayout());
        listPanel.add(productPanel());
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
                                        .addComponent(navPane)
                        )
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(controlPanel))
        );
        f.add(panel);
        f.setTitle("Product Inventory");
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
    }

    /**
     * <code>rightPanel</code> method creates the GUI for the right side of the frame.
     *
     * @return panel : returns the right panel of the GUI as a JPanel.
     */
    private JPanel rightPanel() {
        JPanel panel = new JPanel();

        GroupLayout rightPane = new GroupLayout(panel);
        panel.setLayout(rightPane);

        JButton salesButton = new JButton("Sales");
        salesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.salesDisplay();
            }
        });
        JButton userButton = new JButton("User Maint.");
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.userMaintenance();
            }
        });
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                getFrame().dispose();
            }
        });

        //horizontal plane (top down)
        rightPane.setHorizontalGroup(rightPane.createParallelGroup()
                        .addComponent(salesButton)
                        .addComponent(userButton)
                        .addComponent(exitButton)
        );
        rightPane.linkSize(salesButton, userButton, exitButton);

        //vertical plane (rows)
        rightPane.setVerticalGroup(rightPane.createSequentialGroup()
                .addComponent(salesButton)
                .addComponent(userButton)
                .addComponent(exitButton));
        return panel;
    }

    /**
     * <code>productScroll</code> method displays the JList for the top portion of the frame.
     *
     * @return panel : returns the JList as a JScrollPane for use in <code>productPanel</code> method.
     */
    private JScrollPane productScroll() {
        JScrollPane panel = new JScrollPane();
        ListSelectionListener listListener = new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                Product p = productList.getSelectedValue();
                if (p != null) {
                    upcField.setText(p.getUpc());
                    descriptionField.setText(p.getDescription());
                    priceField.setText(String.valueOf(p.getPrice()));
                    numberInStockField.setText(String.valueOf(p.getNumInStock()));
                } else {
                    upcField.setText("");
                    descriptionField.setText("");
                    priceField.setText("");
                    numberInStockField.setText("");
                }
            }
        };
        productList.setFont(new Font("Courier New", Font.PLAIN, 12));
        productList.setVisibleRowCount(10);
        productList.addListSelectionListener(listListener);
        return panel;
    }

    /**
     * <code>productPanel</code> method displays the JList and its header.
     *
     * @return panel : returns the JList and header as one JPanel object.
     */
    private JPanel productPanel() {
        JPanel panel = new JPanel();
        GroupLayout inventoryPane = new GroupLayout(panel);
        panel.setLayout(inventoryPane);

        JScrollPane scrollPane = new JScrollPane(productList);
        scrollPane.add(productScroll());

        JLabel upcLabel = new JLabel(" UPC");
        JLabel descriptionLabel = new JLabel("Description");
        JLabel priceLabel = new JLabel("Price");
        JLabel numberInStockLabel = new JLabel("# In Stock");

        Font font = new Font("Courier New", Font.BOLD, 12);
        upcLabel.setFont(font);
        descriptionLabel.setFont(font);
        priceLabel.setFont(font);
        numberInStockLabel.setFont(font);

        //horizontal plane
        inventoryPane.setHorizontalGroup(inventoryPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(inventoryPane.createSequentialGroup()
                        .addComponent(upcLabel, 75, 75, 75)
                        .addComponent(descriptionLabel, 225, 225, 225)
                        .addComponent(priceLabel, 65, 65, 65)
                        .addComponent(numberInStockLabel, 70, 70, 70))
                .addGroup(inventoryPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(scrollPane)));
        //vertical plane
        inventoryPane.setVerticalGroup(inventoryPane.createSequentialGroup()
                        .addGroup(inventoryPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(upcLabel)
                                .addComponent(descriptionLabel)
                                .addComponent(priceLabel)
                                .addComponent(numberInStockLabel))
                        .addGroup(inventoryPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(scrollPane))
        );
        return panel;
    }

    /**
     * <code>controlPanel</code> method displays the bottom portion of the Inventory frame.
     * It combines <code>controlOpPanel(), controlInputPanel()</code> methods into one section.
     *
     * @return panel : returns the JPanel section designed for user input.
     */
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
        controlPane.setHorizontalGroup(controlPane.createSequentialGroup()
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(errorMessage)
                                        .addComponent(controlInput)
                        )
                        .addGroup(controlPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(controlButtons))
        );
        //vertical plane
        controlPane.setVerticalGroup(controlPane.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(errorMessage, 15, 15, 15)
                        .addComponent(controlInput)
                        .addComponent(controlButtons)
        );
        return panel;
    }

    /**
     * <code>controlOpPanel</code> method contains the buttons for the "control" panel.
     *
     * @return panel : returns the JPanel sections containing the add, update, and remove buttons
     */
    private JPanel controlOpPanel() {
        JPanel panel = new JPanel();
        final GroupLayout controlButtonPane = new GroupLayout(panel);
        panel.setLayout(controlButtonPane);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.addProduct(upcField.getText(), descriptionField.getText(),
                        priceField.getText(), numberInStockField.getText());
            }
        });
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.updateProduct(upcField.getText(), descriptionField.getText(),
                        priceField.getText(), numberInStockField.getText());
            }
        });
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.removeProduct(upcField.getText());
            }
        });

        //horizontal plane
        controlButtonPane.setHorizontalGroup(controlButtonPane.createParallelGroup()
                        .addComponent(addButton)
                        .addComponent(updateButton)
                        .addComponent(removeButton)
        );
        controlButtonPane.linkSize(SwingConstants.HORIZONTAL, addButton, updateButton, removeButton);

        //vertical plane
        controlButtonPane.setVerticalGroup(controlButtonPane.createSequentialGroup()
                        .addComponent(addButton)
                        .addComponent(updateButton)
                        .addComponent(removeButton)
        );
        return panel;
    }

    /**
     * <code>controlInputPanel</code> method contains the JTextFields for user input.
     *
     * @return panel : returns the user input section of the "control" panel.
     */
    private JPanel controlInputPanel() {
        JPanel panel = new JPanel();
        GroupLayout inputPane = new GroupLayout(panel);
        panel.setLayout(inputPane);

        JLabel upcLabel = new JLabel("UPC:");
        JLabel descriptionLabel = new JLabel("Description:");
        JLabel priceLabel = new JLabel("Price:");
        JLabel numberInStockLabel = new JLabel("In Stock:");

        upcField = new JTextField();
        descriptionField = new JTextField();
        priceField = new JTextField();
        numberInStockField = new JTextField();

        //horizontal plane
        inputPane.setHorizontalGroup(inputPane.createSequentialGroup()
                        .addGroup(inputPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(upcLabel)
                                .addComponent(descriptionLabel))
                        .addGroup(inputPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(upcField, 100, 100, 100)
                                .addComponent(descriptionField, 240, 240, 240))
                        .addGroup(inputPane.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(priceLabel)
                                .addComponent(numberInStockLabel))
                        .addGroup(inputPane.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(priceField)
                                .addComponent(numberInStockField))
        );
        inputPane.linkSize(SwingConstants.HORIZONTAL, upcField, priceField, numberInStockField);

        //vertical plane
        inputPane.setVerticalGroup(inputPane.createSequentialGroup()
                        .addGroup(inputPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(upcLabel)
                                .addComponent(upcField, 20, 20, 20)
                                .addComponent(priceLabel)
                                .addComponent(priceField))
                        .addGroup(inputPane.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addComponent(descriptionLabel)
                                .addComponent(descriptionField)
                                .addComponent(numberInStockLabel)
                                .addComponent(numberInStockField))
        );
        inputPane.linkSize(SwingConstants.VERTICAL, upcField, priceField, numberInStockField, descriptionField);
        return panel;
    }

    /**
     * <code>getProductModel</code> method getter for DefaultListModel<Product>
     *
     * @return productModel : allows InventoryController to access the DefaultListModel
     */
    public DefaultListModel<Product> getProductModel() {
        return productModel;
    }

    /**
     * <code>getFrame</code> method getter for the JFrame
     *
     * @return f: allows the JFrame to be accessed from other classes.
     */
    public static JFrame getFrame() {
        return f;
    }

    /**
     * <code>initializeList</code> method generates the JList for the inventory from the database
     * if the current list is not populated.
     */
    private void initializeList() {
        int listSize = productList.getMaxSelectionIndex();
        if (listSize < 0) {
            if (controller.getInventory().size() > -1) {
                for (Product p : controller.getInventory().values()) {
                    productModel.addElement(controller.getInventory().get(p.getUpc()));
                }
            }
        }
    }

    //error messages
    public void errMsgInvalidUpc() {
        errorMessage.setText("Invalid UPC. UPC must be exactly 8 digits");
    }

    public void errMsgInvalidDescription() {
        errorMessage.setText("Descriptions must be 30 characters or less.");
    }

    public void errMsgInvalidPrice() {
        errorMessage.setText("Prices must be decimal numbers between -$25,000.00 and $25,000.00.");
    }

    public void errMsgInvalidStock() {
        errorMessage.setText("Stock quantity must be integers between 0 and 999.");
    }

    public void errMsgUpdateProduct() {
        errorMessage.setText("Unable to update product.");
    }

    public void errMsgAddProduct() {
        errorMessage.setText("Unable to add product");
    }

    public void errMsgRemoveProduct() {
        errorMessage.setText("Unable to remove product");
    }

    public void errMsgRest() {
        errorMessage.setText("");
    }
}
