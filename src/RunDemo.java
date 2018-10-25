import View.Menu;

import javax.swing.*;

public class RunDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void createAndShowGUI() throws Exception {
        new Menu();
    }
}
