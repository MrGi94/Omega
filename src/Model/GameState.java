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

    public GameState(GameState gs) {
        this.HEX_MAP = new HashMap<>(gs.HEX_MAP);
        this.UNION_FIND_MAP = new HashMap<>(gs.UNION_FIND_MAP);
        this.CLUSTER_PARENT_ID_LIST = new LinkedHashSet<>(gs.CLUSTER_PARENT_ID_LIST);
        this.FREE_TILES_LEFT = gs.FREE_TILES_LEFT;
        this.NUMBER_OF_TILES_PLACED = gs.NUMBER_OF_TILES_PLACED;
    }

}
