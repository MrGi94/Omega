package Model;

import Model.TranspositionTable.TTEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class GameData implements java.io.Serializable {
    public static boolean HUMAN_PLAYER_FIRST = true;
    public static boolean HUMAN_PLAYER_TURN = true;
    public static boolean FIRST_PIECE = true;
    public static int FREE_TILES_LEFT = 0;
    public static int NUMBER_OF_TILES_PLACED = 0;
    public static int BOARD_SIZE = 5;
    public static long ZORBIST_WHITE_MOVE = 0;
    public static List<Hexagon> HEX_LIST = new ArrayList<>();
    public static HashMap<Hexagon, UnionFindTile> HEX_MAP;
    public static HashMap<Byte, UnionFindTile> UNION_FIND_MAP;
    public static HashMap<Long, TTEntry> TRANSPOSITION_TABLE;
    // just store the ID so u don't have to update the objects constantly
    public static LinkedHashSet<Byte> CLUSTER_PARENT_ID_LIST = new LinkedHashSet<>();

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
        this.free_tiles_left = FREE_TILES_LEFT;
        this.number_of_tiles_placed = NUMBER_OF_TILES_PLACED;
        this.board_size = BOARD_SIZE;
        this.hex_list = HEX_LIST;
        this.hex_map = HEX_MAP;
        this.union_find_map = UNION_FIND_MAP;
        this.cluster_parent_id_list = CLUSTER_PARENT_ID_LIST;
        this.zorbist_white_move = ZORBIST_WHITE_MOVE;
        this.transposition_table = TRANSPOSITION_TABLE;
    }

    public static void setGameData(GameData gd) {
        HUMAN_PLAYER_FIRST = gd.human_player_first;
        HUMAN_PLAYER_TURN = gd.human_player_turn;
        FIRST_PIECE = gd.first_piece;
        FREE_TILES_LEFT = gd.free_tiles_left;
        NUMBER_OF_TILES_PLACED = gd.number_of_tiles_placed;
        BOARD_SIZE = gd.board_size;
        HEX_LIST = gd.hex_list;
        HEX_MAP = gd.hex_map;
        UNION_FIND_MAP = gd.union_find_map;
        CLUSTER_PARENT_ID_LIST = gd.cluster_parent_id_list;
        ZORBIST_WHITE_MOVE = gd.zorbist_white_move;
        TRANSPOSITION_TABLE = gd.transposition_table;
    }
}
