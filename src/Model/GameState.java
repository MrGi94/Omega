package Model;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Stack;

public class GameState implements java.io.Serializable {

    public static HashMap<Hexagon, UnionFindTile> HEX_MAP;
    public static HashMap<Byte, UnionFindTile> UNION_FIND_MAP;
    public static Stack<Hexagon> HEX_STACK;

    // just store the ID so u don't have to update the objects constantly
    public static LinkedHashSet<Byte> CLUSTER_PARENT_ID_LIST;
    public static byte FREE_TILES_LEFT;
    public static byte NUMBER_OF_TILES_PLACED;

    public GameState() {
        HEX_MAP = new HashMap<>();
        UNION_FIND_MAP = new HashMap<>();
        HEX_STACK = new Stack<>();
        CLUSTER_PARENT_ID_LIST = new LinkedHashSet<>();
        FREE_TILES_LEFT = 0;
        NUMBER_OF_TILES_PLACED = 0;
    }

    public GameState(GameState gs) {
        this.HEX_MAP = new HashMap<>(gs.HEX_MAP);
        this.HEX_STACK = gs.HEX_STACK;
        this.UNION_FIND_MAP = new HashMap<>(gs.UNION_FIND_MAP);
        this.CLUSTER_PARENT_ID_LIST = new LinkedHashSet<>(gs.CLUSTER_PARENT_ID_LIST);
        this.FREE_TILES_LEFT = gs.FREE_TILES_LEFT;
        this.NUMBER_OF_TILES_PLACED = gs.NUMBER_OF_TILES_PLACED;
    }

}
