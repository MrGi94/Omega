package Model;

import java.util.ArrayList;

/*
* stores all the necessary information computed by the negamax algorithm
* */
public class HistoryEntry {

    private byte depth;
    private short value;
    private ArrayList moveHistory;
    // private Constants.FLAG flag;
    // private byte bestMove;
    // private long key;

    public HistoryEntry(byte depth, short value, ArrayList moveHistory) {
        this.depth = depth;
        this.value = value;
        this.moveHistory = moveHistory;
    }

    public ArrayList getMoveHistory() {
        return moveHistory;
    }

    public HistoryEntry(ArrayList moveHistory) {
        this.moveHistory = moveHistory;
    }

//    public HistoryEntry(Constants.FLAG flag, byte depth, short value, byte bestMove, UnionFindTile[] board) {
//        this.flag = flag;
//        this.depth = depth;
//        this.value = value;
//        this.bestMove = bestMove;
//        this.board = board;
//    }
//
//    public Constants.FLAG getFlag() {
//        return flag;
//    }

    public byte getDepth() {
        return depth;
    }

    public short getValue() {
        return value;
    }

    public byte[] getBestMoves() {
        return new byte[]{(byte) this.moveHistory.get(1), (byte) this.moveHistory.get(2)};
    }
}
