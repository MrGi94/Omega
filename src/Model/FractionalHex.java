package Model;

import java.util.ArrayList;

public class FractionalHex {

    public final double q;
    public final double r;
    public final double s;

    public FractionalHex(double q, double r, double s) {
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

    public FractionalHex hexLerp(FractionalHex b, double t) {
        return new FractionalHex(q * (1 - t) + b.q * t, r * (1 - t) + b.r * t, s * (1 - t) + b.s * t);
    }

    static public ArrayList<Hexagon> hexLinedraw(Hexagon a, Hexagon b) {
        int N = a.distance(b);
        FractionalHex a_nudge = new FractionalHex(a.q + 0.000001, a.r + 0.000001, a.s - 0.000002);
        FractionalHex b_nudge = new FractionalHex(b.q + 0.000001, b.r + 0.000001, b.s - 0.000002);
        ArrayList<Hexagon> results = new ArrayList<Hexagon>() {{
        }};
        double step = 1.0 / Math.max(N, 1);
        for (int i = 0; i <= N; i++) {
            results.add(a_nudge.hexLerp(b_nudge, step * i).hexRound());
        }
        return results;
    }
}