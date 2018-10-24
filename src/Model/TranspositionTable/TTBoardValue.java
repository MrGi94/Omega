package Model.TranspositionTable;

public class TTBoardValue {
    private long white_value;
    private long black_value;

    public TTBoardValue(long white_value, long black_value) {
        this.white_value = white_value;
        this.black_value = black_value;
    }

    public long getValue(byte color) {
        if (color == 1)
            return white_value;
        else
            return black_value;
    }
}
