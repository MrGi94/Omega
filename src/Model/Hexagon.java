package Model;

import java.util.ArrayList;

public class Hexagon implements java.io.Serializable {
    public final int q;
    public final int r;
    public final int s;

    public Hexagon() {
        this.q = 0;
        this.r = 0;
        this.s = 0;
    }

    public Hexagon(int q, int r, int s) {
        this.q = q;
        this.r = r;
        this.s = s;
        if (q + r + s != 0) throw new IllegalArgumentException("q + r + s must be 0");
    }

    public Hexagon add(Hexagon b) {
        return new Hexagon(q + b.q, r + b.r, s + b.s);
    }

    public Hexagon subtract(Hexagon b) {
        return new Hexagon(q - b.q, r - b.r, s - b.s);
    }

    public Hexagon scale(int k) {
        return new Hexagon(q * k, r * k, s * k);
    }

    public Hexagon rotateLeft() {
        return new Hexagon(-s, -q, -r);
    }

    public Hexagon rotateRight() {
        return new Hexagon(-r, -s, -q);
    }

    static public ArrayList<Hexagon> diagonals = new ArrayList<Hexagon>() {{
        add(new Hexagon(2, -1, -1));
        add(new Hexagon(1, -2, 1));
        add(new Hexagon(-1, -1, 2));
        add(new Hexagon(-2, 1, 1));
        add(new Hexagon(-1, 2, -1));
        add(new Hexagon(1, 1, -2));
    }};

    public Hexagon diagonalNeighbor(int direction) {
        return add(Hexagon.diagonals.get(direction));
    }

    public int length() {
        return (int) ((Math.abs(q) + Math.abs(r) + Math.abs(s)) / 2);
    }

    public int distance(Hexagon b) {
        return subtract(b).length();
    }

    @Override
    public int hashCode() {
        final int PRIMEQ = 31;
        final int PRIMER = 17;
        final int PRIMES = 23;
        return PRIMEQ * this.q + PRIMER * this.r + PRIMES * this.s;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        if (o == this)
            return true;

        if (o.getClass() != getClass())
            return false;

        Hexagon b = (Hexagon) o;
        return this.q == b.q && this.r == b.r && this.s == b.s;
    }
}
