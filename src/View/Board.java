package View;

import javax.swing.*;
import java.awt.*;

public class Board extends JComponent {

    public void drawPolygon(Graphics g, int[] xPoly, int[] yPoly, Color c) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(238, 238, 238));
        g2.setStroke(new BasicStroke(2));
        Polygon p = new Polygon(xPoly, yPoly, yPoly.length);
        g2.drawPolygon(p);
        g2.setColor(c);
        g2.fillPolygon(p);
    }
}
