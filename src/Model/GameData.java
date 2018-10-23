package Model;

import java.util.HashMap;
import java.util.Stack;

public class GameData implements java.io.Serializable {
    public static boolean HUMAN_PLAYER_FIRST = true;
    public static boolean HUMAN_PLAYER_TURN = true;
    public static boolean FIRST_PIECE = true;
    public static int FREE_TILES_LEFT = 0;
    public static int BOARD_SIZE = 7;
    public static Stack<Hexagon> HEX_STACK = new Stack<>();
    public static HashMap<Hexagon, Byte> HEX_MAP;

    private boolean human_player_first;
    private boolean human_player_turn;
    private boolean first_piece;
    private int free_tiles_left;
    private int board_size;
    private Stack<Hexagon> hex_stack;
    private HashMap<Hexagon, Byte> hex_map;

    public GameData() {
        this.human_player_first = HUMAN_PLAYER_FIRST;
        this.human_player_turn = HUMAN_PLAYER_TURN;
        this.first_piece = FIRST_PIECE;
        this.free_tiles_left = FREE_TILES_LEFT;
        this.board_size = BOARD_SIZE;
        this.hex_stack = HEX_STACK;
        this.hex_map = HEX_MAP;
    }

    public static void setGameData(GameData gd) {
        HUMAN_PLAYER_FIRST = gd.human_player_first;
        HUMAN_PLAYER_TURN = gd.human_player_turn;
        FIRST_PIECE = gd.first_piece;
        FREE_TILES_LEFT = gd.free_tiles_left;
        BOARD_SIZE = gd.board_size;
        HEX_STACK = gd.hex_stack;
        HEX_MAP = gd.hex_map;
    }
}
