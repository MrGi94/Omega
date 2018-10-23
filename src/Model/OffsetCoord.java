package Model;

public class OffsetCoord {
    public final int col;
    public final int row;
    static public int EVEN = 1;
    static public int ODD = -1;

    public OffsetCoord(int col, int row) {
        this.col = col;
        this.row = row;
    }

    static public OffsetCoord qoffsetFromCube(int offset, Hexagon h) {
        int col = h.q;
        int row = h.r + (int) ((h.q + offset * (h.q & 1)) / 2);
        return new OffsetCoord(col, row);
    }

    static public Hexagon qoffsetToCube(int offset, OffsetCoord h) {
        int q = h.col;
        int r = h.row - (int) ((h.col + offset * (h.col & 1)) / 2);
        int s = -q - r;
        return new Hexagon(q, r, s);
    }

    static public OffsetCoord roffsetFromCube(int offset, Hexagon h) {
        int col = h.q + (int) ((h.r + offset * (h.r & 1)) / 2);
        int row = h.r;
        return new OffsetCoord(col, row);
    }

    static public Hexagon roffsetToCube(int offset, OffsetCoord h) {
        int q = h.col - (int) ((h.row + offset * (h.row & 1)) / 2);
        int r = h.row;
        int s = -q - r;
        return new Hexagon(q, r, s);
    }
}
