package Model;

public class UnionFindTile implements java.io.Serializable {

    private byte parent;
    private byte tile_id;
    private byte size;
    private byte color;

    public UnionFindTile(byte color) {
        this.size = 1;
        this.parent = 0;
        this.tile_id = 0;
        this.color = color;
    }

    public UnionFindTile(byte parent_id, byte color) {
        this.size = 1;
        this.parent = parent_id;
        this.tile_id = parent_id;
        this.color = color;
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
}