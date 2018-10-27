package Model.TranspositionTable;

import Model.UnionFindTile;

import java.util.Comparator;

public class TTEntry implements Comparator<TTEntry>, Comparable<TTEntry> {

    //    private Constants.FLAG flag;
    private byte depth;
    private short value;
    private byte[] bestMove;
    private UnionFindTile[] board;

    public TTEntry(byte depth, short value, byte[] bestMove, UnionFindTile[] board) {
        this.depth = depth;
        this.value = value;
        this.bestMove = bestMove;
        this.board = board;
    }

    public TTEntry(byte[] bestMove) {
        this.bestMove = bestMove;
    }

    @Override
    public int compare(TTEntry o1, TTEntry o2) {
        return o1.value - o2.value;
    }

//
//    public TTEntry(Constants.FLAG flag, byte depth, short value, short bestMove){
//        this.flag = flag;
//        this.depth = depth;
//        this.value = value;
//        this.bestMove = bestMove;
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

    public byte[] getBestMove() {
        return bestMove;
    }

    public UnionFindTile[] getBoard() {
        return board;
    }

    @Override
    public int compareTo(TTEntry o) {
        int result = 0;
        for (byte i = 0; i < this.board.length; ++i) {
            result = this.board[i].compareTo(o.board[i]);
        }
        return result;
    }
}
