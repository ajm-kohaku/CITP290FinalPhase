package murph32;

import murph32.view.LoginView;

import javax.swing.*;

/**
 * Created by Dukat on 5/6/2014.
 */
public class ClientMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new LoginView();
            }
        });
    }
}
