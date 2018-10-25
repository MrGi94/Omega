package Model;

public class Hexagon implements java.io.Serializable {
    public final int q;
    public final int r;
    public final int s;

    public Hexagon(int q, int r, int s) {
        this.q = q;
        this.r = r;
        this.s = s;
        if (q + r + s != 0) throw new IllegalArgumentException("q + r + s must be 0");
    }

    public Hexagon add(Hexagon b) {
        return new Hexagon(q + b.q, r + b.r, s + b.s);
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
