package me.chongwish.zhess.stuff.virtual;

public class Move {
    private byte pieceId = Byte.MAX_VALUE;
    private int position = Integer.MAX_VALUE;

    public byte getPieceId() {
        return pieceId;
    }

    public void setPieceId(byte pieceId) {
        this.pieceId = pieceId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
