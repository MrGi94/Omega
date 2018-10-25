package Model;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class GameState implements java.io.Serializable {

    public HashMap<Hexagon, UnionFindTile> HEX_MAP;
    public HashMap<Byte, Hexagon> HEX_MAP_BY_ID;
    public HashMap<Byte, UnionFindTile> UNION_FIND_MAP;
    public byte[] POSITION_ARRAY;

    // just store the ID so u don't have to update the objects constantly
    public LinkedHashSet<Byte> CLUSTER_PARENT_ID_LIST;
    public byte FREE_TILES_LEFT;
    public byte NUMBER_OF_TILES_PLACED;
    public boolean FIRST_PIECE;

    public GameState() {
        HEX_MAP = new HashMap<>();
        HEX_MAP_BY_ID = new HashMap<>();
        UNION_FIND_MAP = new HashMap<>();
        CLUSTER_PARENT_ID_LIST = new LinkedHashSet<>();
        FREE_TILES_LEFT = 0;
        NUMBER_OF_TILES_PLACED = 0;
        FIRST_PIECE = true;
        POSITION_ARRAY = new byte[1-3*GameData.BOARD_SIZE+3*(GameData.BOARD_SIZE*GameData.BOARD_SIZE)];
    }

    public GameState(GameState gamestate) {
        HEX_MAP = new HashMap<>(gamestate.HEX_MAP);
        HEX_MAP_BY_ID = new HashMap<>(gamestate.HEX_MAP_BY_ID);
        UNION_FIND_MAP = new HashMap<>(gamestate.UNION_FIND_MAP);
        CLUSTER_PARENT_ID_LIST = new LinkedHashSet<>(gamestate.CLUSTER_PARENT_ID_LIST);
        FREE_TILES_LEFT = gamestate.FREE_TILES_LEFT;
        NUMBER_OF_TILES_PLACED = gamestate.NUMBER_OF_TILES_PLACED;
        FIRST_PIECE = gamestate.FIRST_PIECE;
        POSITION_ARRAY = gamestate.POSITION_ARRAY;
    }
}
