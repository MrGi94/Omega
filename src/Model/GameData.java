package Model;

import Model.TranspositionTable.TTEntry;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;

public class GameData implements java.io.Serializable {
    public static boolean HUMAN_PLAYER_FIRST = true;
    public static boolean HUMAN_PLAYER_TURN = true;
    public static boolean FIRST_PIECE = true;
    public static int BOARD_SIZE = 5;
    public static long ZORBIST_WHITE_MOVE = 0;
    public static Stack<GameState> GAME_STATES;
    public static HashMap<Long, TTEntry> TRANSPOSITION_TABLE;

    private boolean human_player_first;
    private boolean human_player_turn;
    private boolean first_piece;
    private int free_tiles_left;
    private int number_of_tiles_placed;
    private int board_size;
    private long zorbist_white_move;
    private List<Hexagon> hex_list;
    private HashMap<Hexagon, UnionFindTile> hex_map;
    private HashMap<Byte, UnionFindTile> union_find_map;
    private LinkedHashSet<Byte> cluster_parent_id_list;
    private HashMap<Long, TTEntry> transposition_table;

    public GameData() {
        this.human_player_first = HUMAN_PLAYER_FIRST;
        this.human_player_turn = HUMAN_PLAYER_TURN;
        this.first_piece = FIRST_PIECE;
        this.board_size = BOARD_SIZE;
        this.zorbist_white_move = ZORBIST_WHITE_MOVE;
        this.transposition_table = TRANSPOSITION_TABLE;
    }

    public static void setGameData(GameData gd) {
        HUMAN_PLAYER_FIRST = gd.human_player_first;
        HUMAN_PLAYER_TURN = gd.human_player_turn;
        FIRST_PIECE = gd.first_piece;
        BOARD_SIZE = gd.board_size;
        ZORBIST_WHITE_MOVE = gd.zorbist_white_move;
        TRANSPOSITION_TABLE = gd.transposition_table;
    }
}
