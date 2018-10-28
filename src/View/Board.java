package View;

import javax.swing.*;
import java.awt.*;

/*
 * Draws the tile on the board
 * */
public class Board extends JComponent {

    public void drawPolygon(Graphics g, int[] xPoly, int[] yPoly, Color c) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(238, 238, 238));
        g2.setStroke(new BasicStroke(3));
        Polygon p = new Polygon(xPoly, yPoly, yPoly.length);
        g2.drawPolygon(p);
        g2.setColor(c);
        g2.fillPolygon(p);
    }

    public void paint(Graphics g) {
        super.paint(g);
    }
}
