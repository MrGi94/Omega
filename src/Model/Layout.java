package Model;

import java.util.ArrayList;

public class Layout {

    private static Point hexToPixel(Hexagon h) {
        Orientation M = Constants.BOARD_ORIENTATION;
        double x = (M.f0 * h.q + M.f1 * h.r) * Constants.TILE_SIZE.x;
        double y = (M.f2 * h.q + M.f3 * h.r) * Constants.TILE_SIZE.y;
        return new Point(x + Constants.BOARD_ORIGIN.x, y + Constants.BOARD_ORIGIN.y);
    }

    public static FractionalHex pixelToHex(Point p) {
        Orientation M = Constants.BOARD_ORIENTATION;
        Point pt = new Point((p.x - Constants.BOARD_ORIGIN.x) / Constants.TILE_SIZE.x, (p.y - Constants.BOARD_ORIGIN.y) / Constants.TILE_SIZE.y);
        double q = M.b0 * pt.x + M.b1 * pt.y;
        double r = M.b2 * pt.x + M.b3 * pt.y;
        return new FractionalHex(q, r, -q - r);
    }

    private static Point hexCornerOffset(int corner) {
        Orientation M = Constants.BOARD_ORIENTATION;
        double angle = 2.0 * Math.PI * (M.start_angle - corner) / 6;
        return new Point(Constants.TILE_SIZE.x * Math.cos(angle), Constants.TILE_SIZE.y * Math.sin(angle));
    }

    public static ArrayList<Point> polygonCorners(Hexagon h) {
        ArrayList<Point> corners = new ArrayList<>();
        Point center = hexToPixel(h);
        for (int i = 0; i < 6; i++) {
            Point offset = hexCornerOffset(i);
            corners.add(new Point(center.x + offset.x, center.y + offset.y));
        }
        return corners;
    }
}
