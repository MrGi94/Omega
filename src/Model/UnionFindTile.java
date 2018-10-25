package Model;

public class UnionFindTile implements java.io.Serializable {

    private byte parent;
    private byte tile_id;
    private byte size;
    private byte color;

    /* transposition table entries */
//    private long white_hash_value;
//    private long black_hash_value;

    public UnionFindTile(byte tile_id) {
        this.size = 1;
        this.parent = tile_id;
        this.tile_id = tile_id;
        this.color = 0;
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

    public byte getSize() {
        return size;
    }

    public void setSize(byte size) {
        this.size = size;
    }

//    public long getHashValue() {
//        if (this.color == 1)
//            return white_hash_value;
//        else
//            return black_hash_value;
//    }
}