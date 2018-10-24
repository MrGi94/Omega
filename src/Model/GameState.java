package Model;

import java.util.HashMap;
import java.util.LinkedHashSet;

public class GameState implements java.io.Serializable {

    public HashMap<Hexagon, UnionFindTile> HEX_MAP;
    public HashMap<Byte, UnionFindTile> UNION_FIND_MAP;

    // just store the ID so u don't have to update the objects constantly
    public LinkedHashSet<Byte> CLUSTER_PARENT_ID_LIST;
    public byte FREE_TILES_LEFT;
    public byte NUMBER_OF_TILES_PLACED;

    public GameState() {
        HEX_MAP = new HashMap<>();
        UNION_FIND_MAP = new HashMap<>();
        CLUSTER_PARENT_ID_LIST = new LinkedHashSet<>();
        FREE_TILES_LEFT = 0;
        NUMBER_OF_TILES_PLACED = 0;
    }
}
