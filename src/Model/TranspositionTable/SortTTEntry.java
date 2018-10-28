package Model.TranspositionTable;

import java.util.Comparator;

public class SortTTEntry implements Comparator<TTEntry> {

    @Override
    public int compare(TTEntry o1, TTEntry o2) {
        return o2.getValue() - o1.getValue();
    }
}
