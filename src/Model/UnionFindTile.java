package Model;

public class UnionFindTile implements java.io.Serializable {

    private byte parent;
    private byte tile_id;
    private byte size;
    private byte color;

    /* transposition table entries */
    private long white_hash_value;
    private long black_hash_value;

    public UnionFindTile(byte tile_id) {
        this.size = 1;
        this.parent = tile_id;
        this.tile_id = tile_id;
        this.color = 0;
    }

    public UnionFindTile(long white_hash_value, long black_hash_value) {
        this.size = 1;
        this.parent = 0;
        this.tile_id = 0;
        this.color = 0;
        this.white_hash_value = white_hash_value;
        this.black_hash_value = black_hash_value;
    }

    public UnionFindTile(UnionFindTile uft) {
        this.size = uft.getSize();
        this.parent = uft.getParent();
        this.tile_id = uft.getTileId();
        this.color = uft.getColor();
    }

    public byte getColor() {
        return color;
    }

    public void setColor(byte color) {
        this.color = color;
    }

    public byte getParent() {
        return parent;
    }

    public void setParent(byte parent) {
        this.parent = parent;
    }

    public byte getTileId() {
        return tile_id;
    }

    public void setTileId(byte tile_id) {
        this.tile_id = tile_id;
    }

    public byte getSize() {
        return size;
    }

    public void setSize(byte size) {
        this.size = size;
    }

    public long getHashValue() {
        if (this.color == 1)
            return white_hash_value;
        else
            return black_hash_value;
    }
}