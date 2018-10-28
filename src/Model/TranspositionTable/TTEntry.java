package Model.TranspositionTable;

import Model.Constants;
import Model.UnionFindTile;

public class TTEntry implements Comparable<TTEntry> {

    private Constants.FLAG flag;
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
    public int compareTo(TTEntry o) {
        int result = 0;
        for (byte i = 0; i < this.board.length; ++i) {
            result = this.board[i].compareTo(o.board[i]);
        }
        return result;
    }

    public TTEntry(Constants.FLAG flag, byte depth, short value, byte[] bestMove, UnionFindTile[] board) {
        this.flag = flag;
        this.depth = depth;
        this.value = value;
        this.bestMove = bestMove;
        this.board = board;
    }

public Constants.FLAG getFlag() {
    return flag;
}

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
}
