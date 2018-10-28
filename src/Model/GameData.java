package Model;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Stack;

/*
 * Contains all game relevant information and is used for de-/serialization
 * */
public class GameData implements java.io.Serializable {
    public static boolean HUMAN_PLAYER_FIRST = true;
    public static boolean HUMAN_PLAYER_TURN = true;
    public static boolean FIRST_PIECE = true;
    public static int BOARD_SIZE = 5;
    public static short FREE_TILES_LEFT;

    // contains the total spending time
    public static long PROCESSING_TIME = 100000;

    public static HashMap<Hexagon, UnionFindTile> HEX_MAP;

    // connects the Hex Map with the UnionFind tile ID
    public static HashMap<Byte, Hexagon> HEX_MAP_BY_ID;
    // stores the union find tiles by placement
    public static HashMap<Byte, UnionFindTile> UNION_FIND_MAP_BY_PLACEMENT;
    public static LinkedHashSet<Byte> OPEN_BOOK_CORNERS;
    public static LinkedHashSet<Byte> OPEN_BOOK_CENTER;
    public static UnionFindTile[] UNION_FIND_TILE_ARRAY;
    // stores the move history
    public static Stack<Hexagon> HEX_STACK = new Stack<>();

    // just store the ID so u don't have to update the objects constantly
    public static LinkedHashSet<Byte> CLUSTER_PARENT_ID_LIST;
    // public static HashMap<Long, TTEntry> TRANSPOSITION_TABLE;

    // private variables used for de-/serialization
    private boolean human_player_first;
    private boolean human_player_turn;
    private boolean first_piece;
    private int board_size;
    private short free_tiles_left;
    private long processing_time;

    private HashMap<Hexagon, UnionFindTile> hex_map;
    private HashMap<Byte, Hexagon> hex_map_by_id;
    private UnionFindTile[] union_find_tile_array;
    private HashMap<Byte, UnionFindTile> union_find_map_by_placement;
    private LinkedHashSet<Byte> cluster_parent_id_list;
    private Stack<Hexagon> hex_stack;
    private LinkedHashSet<Byte> open_book_center;
    private LinkedHashSet<Byte> open_book_corners;

    // private HashMap<Long, TTEntry> transposition_table;

    // generates a copy of all relevant data
    public GameData() {
        this.human_player_first = HUMAN_PLAYER_FIRST;
        this.human_player_turn = HUMAN_PLAYER_TURN;
        this.first_piece = FIRST_PIECE;
        this.board_size = BOARD_SIZE;
        this.hex_map = HEX_MAP;
        this.cluster_parent_id_list = CLUSTER_PARENT_ID_LIST;
        this.free_tiles_left = FREE_TILES_LEFT;
        this.union_find_tile_array = UNION_FIND_TILE_ARRAY;
        this.hex_stack = HEX_STACK;
        this.hex_map_by_id = HEX_MAP_BY_ID;
        this.processing_time = PROCESSING_TIME;
        this.union_find_map_by_placement = UNION_FIND_MAP_BY_PLACEMENT;
        this.open_book_center = OPEN_BOOK_CENTER;
        this.open_book_corners = OPEN_BOOK_CORNERS;
        // this.transposition_table = TRANSPOSITION_TABLE;
    }

    // retrieves all relevant data
    public static void setGameData(GameData gd) {
        HUMAN_PLAYER_FIRST = gd.human_player_first;
        HUMAN_PLAYER_TURN = gd.human_player_turn;
        FIRST_PIECE = gd.first_piece;
        BOARD_SIZE = gd.board_size;
        HEX_MAP = gd.hex_map;
        CLUSTER_PARENT_ID_LIST = gd.cluster_parent_id_list;
        FREE_TILES_LEFT = gd.free_tiles_left;
        UNION_FIND_TILE_ARRAY = gd.union_find_tile_array;
        HEX_STACK = gd.hex_stack;
        HEX_MAP_BY_ID = gd.hex_map_by_id;
        PROCESSING_TIME = gd.processing_time;
        UNION_FIND_MAP_BY_PLACEMENT = gd.union_find_map_by_placement;
        OPEN_BOOK_CORNERS = gd.open_book_corners;
        OPEN_BOOK_CENTER = gd.open_book_center;
        // TRANSPOSITION_TABLE = gd.transposition_table;
    }
}
