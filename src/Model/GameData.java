package Model;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Stack;

public class GameData implements java.io.Serializable {
    public static boolean HUMAN_PLAYER_FIRST = true;
    public static boolean HUMAN_PLAYER_TURN = true;
    public static boolean FIRST_PIECE = true;
    public static int BOARD_SIZE = 5;
    public static byte FREE_TILES_LEFT;

    public static HashMap<Hexagon, UnionFindTile> HEX_MAP;
    public static HashMap<Byte, Hexagon> HEX_MAP_BY_ID;
    public static HashMap<Byte, UnionFindTile> UNION_FIND_MAP;
    public static UnionFindTile[] UNION_FIND_TILE_ARRAY;
    public static Stack<Hexagon> HEX_STACK = new Stack<>();
    // just store the ID so u don't have to update the objects constantly
    public static LinkedHashSet<Byte> CLUSTER_PARENT_ID_LIST;

//    public static long ZORBIST_WHITE_MOVE = 0;
//    public static HashMap<Long, TTEntry> TRANSPOSITION_TABLE;

    private boolean human_player_first;
    private boolean human_player_turn;
    private boolean first_piece;
    private int board_size;
    private byte free_tiles_left;

    private HashMap<Hexagon, UnionFindTile> hex_map;
    private HashMap<Byte, Hexagon> hex_map_by_id;
    private UnionFindTile[] union_find_tile_array;
    private HashMap<Byte, UnionFindTile> union_find_map;
    private LinkedHashSet<Byte> cluster_parent_id_list;
    private Stack<Hexagon> hex_stack;

//    private long zorbist_white_move;
//    private HashMap<Long, TTEntry> transposition_table;

    public GameData() {
        this.human_player_first = HUMAN_PLAYER_FIRST;
        this.human_player_turn = HUMAN_PLAYER_TURN;
        this.first_piece = FIRST_PIECE;
        this.board_size = BOARD_SIZE;
        this.hex_map = HEX_MAP;
        this.union_find_map = UNION_FIND_MAP;
        this.cluster_parent_id_list = CLUSTER_PARENT_ID_LIST;
        this.free_tiles_left = FREE_TILES_LEFT;
        this.union_find_tile_array = UNION_FIND_TILE_ARRAY;
        this.hex_stack = HEX_STACK;
        this.hex_map_by_id = HEX_MAP_BY_ID;
//        this.zorbist_white_move = ZORBIST_WHITE_MOVE;
//        this.transposition_table = TRANSPOSITION_TABLE;
    }

    public static void setGameData(GameData gd) {
        HUMAN_PLAYER_FIRST = gd.human_player_first;
        HUMAN_PLAYER_TURN = gd.human_player_turn;
        FIRST_PIECE = gd.first_piece;
        BOARD_SIZE = gd.board_size;
        HEX_MAP = gd.hex_map;
        UNION_FIND_MAP = gd.union_find_map;
        CLUSTER_PARENT_ID_LIST = gd.cluster_parent_id_list;
        FREE_TILES_LEFT = gd.free_tiles_left;
        UNION_FIND_TILE_ARRAY = gd.union_find_tile_array;
        HEX_STACK = gd.hex_stack;
        HEX_MAP_BY_ID = gd.hex_map_by_id;
//        ZORBIST_WHITE_MOVE = gd.zorbist_white_move;
//        TRANSPOSITION_TABLE = gd.transposition_table;
    }
}
