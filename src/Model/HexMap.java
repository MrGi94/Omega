package Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HexMap implements java.io.Serializable {

    public static void generateHexMap(int map_radius) {
        GameData.HEX_MAP = new HashMap<>();
        map_radius--;
        for (int q = -map_radius; q <= map_radius; q++) {
            int r1 = Math.max(-map_radius, -q - map_radius);
            int r2 = Math.min(map_radius, -q + map_radius);
            for (int r = r1; r <= r2; r++) {
                Hexagon h = new Hexagon(q, r, -q - r);
                Byte b = 0;
                GameData.HEX_MAP.put(h, b);
            }
        }
        GameData.FREE_TILES_LEFT = GameData.HEX_MAP.size();
    }

    public static void putHexMapValue(Hexagon h, Byte b) {
        if (GameData.HEX_MAP.containsKey(h)) {
            GameData.HEX_MAP.put(h, b);
        }
    }

    public static Byte getHexMapValue(Hexagon h) {
        if (h == null)
            return null;
        return GameData.HEX_MAP.get(h);
    }

    public static Set<Map.Entry<Hexagon, Byte>> getHexMapEntrySet() {
        return GameData.HEX_MAP.entrySet();
    }

}
