package Model;

import java.util.ArrayList;

public class HistoryEntry implements Comparable<HistoryEntry> {

    //    private Constants.FLAG flag;
    private byte depth;
    private short value;
    private byte[] bestMoves;
    private UnionFindTile[] board;
    private ArrayList moveHistory;
//    private byte bestMove;
//    private long key;

    public HistoryEntry(byte depth, short value, byte[] bestMoves, UnionFindTile[] board) {
        this.depth = depth;
        this.value = value;
        this.bestMoves = bestMoves;
        this.board = board;
    }

    public HistoryEntry(byte depth, short value, ArrayList moveHistory) {
        this.depth = depth;
        this.value = value;
        this.moveHistory = moveHistory;
    }

    public ArrayList getMoveHistory() {
        return moveHistory;
    }

    public HistoryEntry(byte[] bestMoves) {
        this.bestMoves = bestMoves;
    }

    @Override
    public int compareTo(HistoryEntry o) {
        int result = 0;
        for (byte i = 0; i < this.board.length; ++i) {
            result = this.board[i].compareTo(o.board[i]);
        }
        return result;
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

//    public UnionFindTile[] getBoard() {
//        return board;
//    }
}
