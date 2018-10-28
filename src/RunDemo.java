import View.Menu;

import javax.swing.*;

public class RunDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                createAndShowGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private static void createAndShowGUI() {
        new Menu();
    }
}
