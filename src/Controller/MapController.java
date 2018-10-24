package Controller;

import Controller.GUI.BoardController;
import Model.*;

import java.util.*;

public class MapController {

    public static void generateHexMap(int map_radius) {
        Random rand = new Random();
        GameData.GAME_STATE = new GameState();
        GameData.TRANSPOSITION_TABLE = new HashMap<>();
        GameData.ZORBIST_WHITE_MOVE = rand.nextLong();
        map_radius--;
        for (int q = -map_radius; q <= map_radius; q++) {
            int r1 = Math.max(-map_radius, -q - map_radius);
            int r2 = Math.min(map_radius, -q + map_radius);
            for (int r = r1; r <= r2; r++) {
                Hexagon h = new Hexagon(q, r, -q - r);
                UnionFindTile uft = new UnionFindTile(rand.nextLong(), rand.nextLong());
                GameData.GAME_STATE.HEX_MAP.put(h, uft);
            }
        }
        GameData.GAME_STATE.FREE_TILES_LEFT = (byte) GameData.GAME_STATE.HEX_MAP.size();
    }

    public static void putHexMapValue(Hexagon h, Byte color, GameState gs) {
        if (gs.HEX_MAP.containsKey(h)) {
            UnionFindTile uft = gs.HEX_MAP.get(h);
            byte tile_id = gs.NUMBER_OF_TILES_PLACED;
            uft.setTileId(tile_id);
            uft.setParent(tile_id);
            uft.setColor(color);
            gs.HEX_MAP.put(h, uft);
            gs.CLUSTER_PARENT_ID_LIST.add(uft.getParent());
            setUnionFindTile(uft, gs);
            connectNeighbors(h, color, tile_id, gs);
        }
    }

    public static ArrayList<Hexagon> getNeighborsByColor(Hexagon hex, Byte color, GameState gs) {
        ArrayList<Hexagon> valid_neighbors = getValidNeighbors(hex);
        ArrayList<Hexagon> valid_color_neighbors = new ArrayList<>();
        for (Hexagon entry : valid_neighbors) {
            if (getHexMapColor(entry, gs).equals(color)) {
                valid_color_neighbors.add(entry);
            }
        }
        return valid_color_neighbors;
    }

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

    private static boolean withinBoundary(Hexagon h) {
        return Math.abs(h.q) < GameData.BOARD_SIZE &&
                Math.abs(h.r) < GameData.BOARD_SIZE &&
                Math.abs(h.s) < GameData.BOARD_SIZE;
    }

    public static Byte getHexMapColor(Hexagon h, GameState gs) {
        if (h == null)
            return null;
        return gs.HEX_MAP.get(h).getColor();
    }

    public static Set<Map.Entry<Hexagon, UnionFindTile>> getHexMapEntrySet() {
        return GameData.GAME_STATE.HEX_MAP.entrySet();
    }

    public static UnionFindTile getHexMapValue(Hexagon h) {
        if (h == null)
            return null;
        return GameData.GAME_STATE.HEX_MAP.get(h);
    }

    /* Union Find Map Control */

    public static void connectNeighbors(Hexagon h, byte color, byte tile_id, GameState gs) {
        ArrayList<Hexagon> neighbor_list = getNeighborsByColor(h, color, gs);
        for (Hexagon neighbor : neighbor_list) {
            union(getHexMapValue(neighbor).getTileId(), tile_id, gs);
        }
    }

    public static boolean connected(byte tile_id1, byte tile_id2, GameState gs) {
        return find(tile_id1, gs).getParent() == find(tile_id2, gs).getParent();
    }

    public static UnionFindTile getUnionFindTile(byte tile_id, GameState gs) {
        return gs.UNION_FIND_MAP.get(tile_id);
    }

    public static void setUnionFindTile(UnionFindTile uft, GameState gs) {
        gs.UNION_FIND_MAP.put(uft.getTileId(), uft);
    }

    public static UnionFindTile find(byte tile_id, GameState gs) {
        UnionFindTile uft = getUnionFindTile(tile_id, gs);
        do {
            if (uft == null)
                return new UnionFindTile();
            uft = getUnionFindTile(uft.getParent(), gs);
        } while (uft.getTileId() != uft.getParent());
        return uft;
    }

    public static void union(byte tile_id1, byte tile_id2, GameState gs) {
        UnionFindTile root1 = find(tile_id1, gs);
        UnionFindTile root2 = find(tile_id2, gs);
        if (root1 == root2) return;
        // make root of smaller parent point to root of larger parent
        if (root1.getParent() < root2.getParent())
            linkUnionFindTile(root2, root1, gs);
        else
            linkUnionFindTile(root1, root2, gs);
    }

    private static void linkUnionFindTile(UnionFindTile largeParent, UnionFindTile smallParent, GameState gs) {
        smallParent.setParent(largeParent.getParent());
        largeParent.setSize((byte) (smallParent.getSize() + largeParent.getSize()));
        setUnionFindTile(smallParent, gs);
        setUnionFindTile(largeParent, gs);
        gs.CLUSTER_PARENT_ID_LIST.remove(smallParent.getTileId());
    }


    /* Transposition Table Control */

    public static long getZobristBoardHash() {
        long zobristKey = 0;
        Byte b = BoardController.determineNextMoveColor();
        for (Map.Entry<Hexagon, UnionFindTile> entry : getHexMapEntrySet()) {
            if (entry.getValue().getColor() != 0) {
                zobristKey ^= entry.getValue().getHashValue();
            }
        }
        if (!GameData.HUMAN_PLAYER_FIRST)
            zobristKey ^= GameData.ZORBIST_WHITE_MOVE;
        return zobristKey;
    }
}
