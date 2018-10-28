package Controller;

import Model.*;

import java.util.*;

/*
 * controls the behaviour of the map representation
 * */
public class MapController {

    public static void generateHexMap(int map_radius) {
        // GameData.TRANSPOSITION_TABLE = new HashMap<>();
        GameData.HEX_MAP = new HashMap<>();
        GameData.HEX_MAP_BY_ID = new HashMap<>();
        GameData.UNION_FIND_MAP_BY_PLACEMENT = new HashMap<>();
        GameData.CLUSTER_PARENT_ID_LIST = new LinkedHashSet<>();
        GameData.OPEN_BOOK_CORNERS = new LinkedHashSet<>();
        GameData.OPEN_BOOK_CENTER = new LinkedHashSet<>();
        GameData.UNION_FIND_TILE_ARRAY = new UnionFindTile[1 - 3 * GameData.BOARD_SIZE + 3 * (GameData.BOARD_SIZE * GameData.BOARD_SIZE)];
        byte tile_id = 0;
        map_radius--;
        for (int q = -map_radius; q <= map_radius; q++) {
            int r1 = Math.max(-map_radius, -q - map_radius);
            int r2 = Math.min(map_radius, -q + map_radius);
            for (int r = r1; r <= r2; r++) {
                Hexagon h = new Hexagon(q, r, -q - r);
                UnionFindTile uft = new UnionFindTile(tile_id);
                GameData.HEX_MAP.put(h, uft);
                setUnionFindTile(uft);
                GameData.UNION_FIND_TILE_ARRAY[tile_id] = new UnionFindTile(uft);
                GameData.HEX_MAP_BY_ID.put(tile_id, h);
                int sum = Math.abs(q) + Math.abs(r) + Math.abs(-q - r);
                if ((q == 0 || r == 0 || (-q - r) == 0) && sum == map_radius * 2)
                    GameData.OPEN_BOOK_CORNERS.add(tile_id);
                if (GameData.BOARD_SIZE > 3 && (sum == 0 || sum == 2))
                    GameData.OPEN_BOOK_CENTER.add(tile_id);
                tile_id++;
            }
        }
        GameData.FREE_TILES_LEFT = (byte) GameData.HEX_MAP.size();
    }

    // assigns a color to the particular hexagon
    public static void putHexMapValue(Hexagon h, Byte color) {
        if (GameData.HEX_MAP.containsKey(h)) {
            UnionFindTile uft = GameData.HEX_MAP.get(h);
            byte placement_id = (byte) (GameData.HEX_MAP.size() - GameData.FREE_TILES_LEFT + 1);
            uft.setColor(color);
            uft.setPlacement_id(placement_id);
            uft.setParent(placement_id);
            GameData.HEX_MAP.put(h, uft);
            GameData.CLUSTER_PARENT_ID_LIST.add(uft.getPlacement_id());
            setUnionFindTile(uft);
            connectNeighbors(h, color);
        }
    }

    // retrieves the neighbours of the particular hexagon with matching color
    private static ArrayList<Hexagon> getNeighborsByColor(Hexagon hex, Byte color) {
        ArrayList<Hexagon> valid_neighbors = getValidNeighbors(hex);
        ArrayList<Hexagon> valid_color_neighbors = new ArrayList<>();
        for (Hexagon entry : valid_neighbors) {
            if (getHexMapColor(entry).equals(color)) {
                valid_color_neighbors.add(entry);
            }
        }
        return valid_color_neighbors;
    }

    // retrieves all neighbours of the particular hexagon which are withing the map borders
    public static ArrayList<Hexagon> getValidNeighbors(Hexagon hex) {
        ArrayList<Hexagon> valid_neighbors = new ArrayList<>();
        for (Hexagon hex_direction : Constants.HEXAGON_DIRECTIONS) {
            hex_direction = hex_direction.add(hex);
            if (withinBoundary(hex_direction)) {
                valid_neighbors.add(hex_direction);
            }
        }
        return valid_neighbors;
    }

    // check if a certain hexagon is within the map boundaries
    private static boolean withinBoundary(Hexagon h) {
        return Math.abs(h.q) < GameData.BOARD_SIZE &&
                Math.abs(h.r) < GameData.BOARD_SIZE &&
                Math.abs(h.s) < GameData.BOARD_SIZE;
    }

    public static Byte getHexMapColor(Hexagon h) {
        if (h == null || GameData.HEX_MAP == null)
            return null;
        return GameData.HEX_MAP.get(h).getColor();
    }

    public static Set<Map.Entry<Hexagon, UnionFindTile>> getHexMapEntrySet() {
        return GameData.HEX_MAP.entrySet();
    }

    private static UnionFindTile getHexMapValue(Hexagon h) {
        if (h == null)
            return null;
        return GameData.HEX_MAP.get(h);
    }

    /* Union Find Map Control */

    // connects same colored, valid, neighboring tiles
    private static void connectNeighbors(Hexagon h, byte color) {
        ArrayList<Hexagon> neighbor_list = getNeighborsByColor(h, color);
        for (Hexagon neighbor : neighbor_list) {
            union(getHexMapValue(neighbor).getPlacement_id(), GameData.HEX_MAP.get(h).getPlacement_id());
        }
    }

    private static UnionFindTile getUnionFindTileByPlacement(byte placement_id) {
        return GameData.UNION_FIND_MAP_BY_PLACEMENT.get(placement_id);
    }

    private static void setUnionFindTile(UnionFindTile uft) {
        GameData.UNION_FIND_MAP_BY_PLACEMENT.put(uft.getPlacement_id(), uft);
        GameData.UNION_FIND_TILE_ARRAY[uft.getTileId()] = uft;
    }

    private static UnionFindTile find(byte placement_id) {
        UnionFindTile uft = getUnionFindTileByPlacement(placement_id);
        do {
            if (uft == null)
                return new UnionFindTile((byte) -1);
            uft = getUnionFindTileByPlacement(uft.getParent());
        } while (uft.getPlacement_id() != uft.getParent());
        return uft;
    }

    private static void union(byte placement_id1, byte placement_id2) {
        UnionFindTile root1 = find(placement_id1);
        UnionFindTile root2 = find(placement_id2);
        if (root1 == root2) return;
        // make root of smaller parent point to root of larger parent
        if (root1.getParent() < root2.getParent())
            linkUnionFindTile(root2, root1);
        else
            linkUnionFindTile(root1, root2);
    }

    private static void linkUnionFindTile(UnionFindTile largeParent, UnionFindTile smallParent) {
        smallParent.setParent(largeParent.getParent());
        largeParent.setSize((byte) (smallParent.getSize() + largeParent.getSize()));
        setUnionFindTile(smallParent);
        setUnionFindTile(largeParent);
        GameData.CLUSTER_PARENT_ID_LIST.remove(smallParent.getPlacement_id());
    }

    // reverts a union by removing the particular tile and setting the same colored neighbours parent id to their
    // original placement number
    public static void disconnectTile(Hexagon h) {
        UnionFindTile uft = GameData.HEX_MAP.get(h);
        GameData.CLUSTER_PARENT_ID_LIST.remove(uft.getPlacement_id());

        for (Map.Entry<Byte, UnionFindTile> entry : GameData.UNION_FIND_MAP_BY_PLACEMENT.entrySet()) {
            if (entry.getValue().getParent() == uft.getPlacement_id()) {
                // disconnect this tile
                entry.getValue().setParent(entry.getValue().getPlacement_id());
                setUnionFindTile(entry.getValue());
                GameData.CLUSTER_PARENT_ID_LIST.add(entry.getValue().getPlacement_id());
            }
        }
        uft = new UnionFindTile(uft.getTileId());
        setUnionFindTile(uft);
        GameData.HEX_MAP.put(h, uft);
    }


    /* Transposition Table Control */

//    public static long getZobristBoardHash() {
//        long zobristKey = 0;
//        Byte b = BoardController.determineNextMoveColor();
//        for (Map.Entry<Hexagon, UnionFindTile> entry : getHexMapEntrySet()) {
//            if (entry.getValue().getColor() != 0) {
//                zobristKey ^= entry.getValue().getHashValue();
//            }
//        }
//        if (!GameData.HUMAN_PLAYER_FIRST)
//            zobristKey ^= GameData.ZORBIST_WHITE_MOVE;
//        return zobristKey;
//    }
}
