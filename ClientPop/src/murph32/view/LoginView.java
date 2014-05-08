package murph32.view;

import murph32.controller.LoginController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Dukat on 4/25/2014.
 */
public class LoginView {
    private LoginController control = new LoginController(this);
    private static JFrame f = new JFrame();

    public LoginView() {
        JPanel panel = new JPanel();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GroupLayout layout = new GroupLayout(panel);
        panel.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        //create components
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        final JTextField usernameField = new JTextField();
        final JPasswordField passwordField = new JPasswordField();
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                control.validateLogin(usernameField.getText(), passwordField.getPassword());
            }
        });

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                control.validateLogin(usernameField.getText(), passwordField.getPassword());
            }
        });

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                control.cancelLogin();
            }
        });

        //horizontal layout
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(usernameLabel)
                                        .addComponent(passwordLabel))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(usernameField, 100, 100, 100)
                                        .addComponent(passwordField, 100, 100, 100))
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(loginButton)
                                        .addComponent(cancelButton)))
        );
        layout.linkSize(SwingConstants.HORIZONTAL, loginButton, cancelButton);

        //vertical layout
        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(usernameLabel)
                                .addComponent(usernameField)
                                .addComponent(loginButton))
                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(passwordLabel)
                                .addComponent(passwordField)
                                .addComponent(cancelButton))
        );
        layout.linkSize(SwingConstants.VERTICAL, usernameLabel, passwordLabel, usernameField, passwordField);

        f.add(panel);
        f.setTitle("Login Frame");
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
    }

    public static JFrame getFrame() {
        return f;
    }

    public void errMsgInvalidLogin() {
        //found how to change font @http://stackoverflow.com/questions/19831538/how-can-i-change-color-of-string-in-message-box
        JOptionPane.showMessageDialog(f, new JLabel("<html><font color='red'><strong>Username and/or Password is Invalid." +
                        "<br>Please try again. Or click Cancel to exit</strong></font></html>"),
                "Login Error", JOptionPane.ERROR_MESSAGE
        );
    }
}
