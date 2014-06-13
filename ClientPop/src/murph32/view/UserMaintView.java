package murph32.view;

import murph32.controller.UserMaintController;
import murph32.core.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Dukat on 5/7/2014.
 */
public class UserMaintView {
    private static JFrame f = new JFrame();
    final DefaultComboBoxModel accessLevels = new DefaultComboBoxModel();
    final JComboBox accessLevelBox = new JComboBox(accessLevels);
    private User user = new User();
    private DefaultListModel<User> userModel = new DefaultListModel<>();
    private JList<User> userList = new JList<>(userModel);
    private UserMaintController controller;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorMessage = new JLabel("");

    public UserMaintView(User user) {
        controller = new UserMaintController(this, user);
        controller.getUsers();
        this.initializeList();

        //used example from tutorialspoint.com for JComboBox
        accessLevels.addElement("Employee");
        accessLevels.addElement("Manager");
        accessLevelBox.setSelectedIndex(0);

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel accessLevelLabel = new JLabel("Access Level:");
        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.addUser(usernameField.getText(), passwordField.getPassword(),
                        String.valueOf(accessLevelBox.getItemAt(accessLevelBox.getSelectedIndex())));
            }
        });
        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.updateUser(usernameField.getText(), passwordField.getPassword(),
                        String.valueOf(accessLevelBox.getItemAt(accessLevelBox.getSelectedIndex())));
            }
        });
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.removeUser(usernameField.getText());
            }
        });

        Font font = errorMessage.getFont();
        errorMessage.setFont(new Font(font.getFontName(), Font.BOLD, font.getSize()));
        errorMessage.setForeground(Color.RED);
        //create GroupLayout Shell
        JPanel panel = new JPanel();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        //frame guts
        JScrollPane listPanel = new JScrollPane(userList);
        listPanel.add(userFrame());
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        rightPanel.add(rightFrame());
        //horizontal layout
        layout.setHorizontalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(listPanel)
                                .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(usernameLabel)
                                                .addComponent(passwordLabel)
                                                .addComponent(accessLevelLabel))
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(usernameField, 150, 150, 150)
                                                .addComponent(passwordField)
                                                .addComponent(accessLevelBox))
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                .addComponent(addButton)
                                                .addComponent(updateButton)
                                                .addComponent(removeButton)))
                                .addComponent(errorMessage))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(rightPanel))
        );
        layout.linkSize(SwingConstants.HORIZONTAL, addButton, updateButton, removeButton);
        layout.linkSize(SwingConstants.HORIZONTAL, usernameField, passwordField, accessLevelBox);

        //vertical layout
        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(listPanel)
                                .addComponent(rightPanel))
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(usernameLabel)
                                        .addComponent(usernameField)
                                        .addComponent(addButton))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(passwordLabel)
                                        .addComponent(passwordField)
                                        .addComponent(updateButton))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(accessLevelLabel)
                                        .addComponent(accessLevelBox)
                                        .addComponent(removeButton)))
                        .addComponent(errorMessage, 15, 15, 15)
        );

        f.add(panel);
        f.setTitle("User Maintenance");
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
    }

    private JScrollPane userFrame() {
        JScrollPane panel = new JScrollPane();
        userList.setFont(new Font("Courier New", Font.PLAIN, 12));
        userList.setVisibleRowCount(10);
        userList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                User u = userList.getSelectedValue();
                if (u != null) {
                    usernameField.setText(u.getUsername());
                    passwordField.setText(String.valueOf(u.getPassword()));
                    accessLevelBox.getItemAt(accessLevelBox.getSelectedIndex());
                } else {
                    usernameField.setText("");
                    passwordField.setText("");
                    accessLevelBox.getItemAt(1);
                }
            }
        });
        return panel;
    }

    private JPanel rightFrame() {
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
        JButton inventoryButton = new JButton("Inventory");
        inventoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                controller.inventory();
            }
        });
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                f.dispose();
            }
        });

        //horizontal plane (top down)
        rightPane.setHorizontalGroup(rightPane.createParallelGroup()
                        .addComponent(salesButton)
                        .addComponent(inventoryButton)
                        .addComponent(exitButton)
        );
        rightPane.linkSize(salesButton, inventoryButton, exitButton);

        //vertical plane (rows)
        rightPane.setVerticalGroup(rightPane.createSequentialGroup()
                .addComponent(salesButton)
                .addComponent(inventoryButton)
                .addComponent(exitButton));
        return panel;
    }

    public DefaultListModel<User> getUserModel() {
        return userModel;
    }

    /**
     * <code>getFrame</code> method getter for the JFrame
     *
     * @return f: allows the JFrame to be accessed from other classes.
     */
    public static JFrame getFrame() {
        return f;
    }

    private void initializeList() {
        int listSize = userList.getMaxSelectionIndex();
        if (listSize < 0) {
            if (controller.getUsers().size() > -1) {
                for (User u : controller.getUsers().values()) {
                    userModel.addElement(controller.getUsers().get(u.getUsername()));
                }
            }
        }
    }


    //error messages.
    public void errMsgInvalidUsername() {
        errorMessage.setText("Invalid Username.");
    }

    public void errMsgInvalidPassword() {
        errorMessage.setText("Invalid Password.");
    }

    public void errMsgAddUser() {
        errorMessage.setText("Can't Add User.");
    }

    public void errMsgUpdateUser() {
        errorMessage.setText("Can't update user.");
    }

    public void errMsgRemoveUser() {
        errorMessage.setText("Can't remove user.");
    }

    public void errMsgRest() {
        errorMessage.setText("");
    }

}
