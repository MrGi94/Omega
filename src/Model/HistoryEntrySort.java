package Model;

import java.util.Comparator;

/*
* sorts history elements by depth and value
* */
public class HistoryEntrySort implements Comparator<HistoryEntry> {

    @Override
    public int compare(HistoryEntry o1, HistoryEntry o2) {
        if (o1.getDepth() != o2.getDepth()) {
            return o2.getDepth() - o1.getDepth();
        }
        return o2.getValue() - o1.getValue();
    }
}
