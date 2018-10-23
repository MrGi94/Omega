import Model.UnionFind;
import View.Menu;
import javax.swing.SwingUtilities;

public class RunDemo {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    UnionFind uf = new UnionFind(5);
                    uf.connected(0, 3);
                    uf.union(0, 3);
                    uf.count();
                    uf.union(1,2);
                    uf.count();
                    uf.union(1, 3);
                    uf.count();
                    boolean t = uf.connected(0, 2);


//                    int n = StdIn.readInt();
//                    UF uf = new UF(n);
//                    while (!StdIn.isEmpty()) {
//                        int p = StdIn.readInt();
//                        int q = StdIn.readInt();
//                        if (uf.connected(p, q)) continue;
//                        uf.union(p, q);
//                        StdOut.println(p + " " + q);
//                    }
//                    StdOut.println(uf.count() + " components");


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
