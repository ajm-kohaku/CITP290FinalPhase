package murph32.view;

import murph32.core.User;

import javax.swing.*;

/**
 * Created by Dukat on 5/7/2014.
 */
public class SalesView {
    private User user = new User();
    private static JFrame f = new JFrame();

    public SalesView(User user) {
        this.user = user;

        String accessLevel = user.getAccessLevel();

        //create GroupLayout Shell
        JPanel panel = new JPanel();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        f.add(panel);
        f.setTitle("Sales Display");
        f.setResizable(false);
        f.setLocationRelativeTo(null);
        f.pack();
        f.setVisible(true);
    }
}
