package Model;

public class FractionalHex {

    private final double q;
    private final double r;
    private final double s;

    FractionalHex(double q, double r, double s) {
        this.q = q;
        this.r = r;
        this.s = s;
        if (Math.round(q + r + s) != 0) throw new IllegalArgumentException("q + r + s must be 0");
    }

    public Hexagon hexRound() {
        int qi = (int) (Math.round(q));
        int ri = (int) (Math.round(r));
        int si = (int) (Math.round(s));
        double q_diff = Math.abs(qi - q);
        double r_diff = Math.abs(ri - r);
        double s_diff = Math.abs(si - s);
        if (q_diff > r_diff && q_diff > s_diff) {
            qi = -ri - si;
        } else if (r_diff > s_diff) {
            ri = -qi - si;
        } else {
            si = -qi - ri;
        }
        return new Hexagon(qi, ri, si);
    }
}