package murph32.view;

import murph32.core.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 */
public class AdminFrame extends JFrame {
    private static User user;

    public AdminFrame(User user) {
        this.user = user;
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateContainerGaps(true);
        layout.setAutoCreateGaps(true);


        JButton userButton = new JButton("User Maint.");
        userButton.setName("User");
        userButton.addActionListener(new ButtonListener());
        JButton inventoryButton = new JButton("Inventory");
        inventoryButton.setName("Inventory");
        inventoryButton.addActionListener(new ButtonListener());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setName("Cancel");
        cancelButton.addActionListener(new ButtonListener());

        //horizontal
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addComponent(userButton)
                        .addComponent(inventoryButton)
                        .addComponent(cancelButton)
        );
        layout.linkSize(SwingConstants.HORIZONTAL, userButton, inventoryButton, cancelButton);
        //vertical
        layout.setVerticalGroup(layout.createSequentialGroup()
                        .addComponent(userButton)
                        .addComponent(inventoryButton)
                        .addComponent(cancelButton)
        );
        setTitle("Admin Control");
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }


    private void inventoryOperation() {
        //todo: update input for InventoryView
        new InventoryView(user);
        dispose();
    }

    private void userOperation() {
        //todo: update input for UserMaintView
        new UserMaintView(user);
        dispose();
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            JComponent source = (JButton) actionEvent.getSource();
            switch (source.getName()) {
                case "User":
                    userOperation();
                    break;
                case "Inventory":
                    inventoryOperation();
                    break;
                case "Cancel":
                    dispose();
                    break;
            }
        }
    }
}
