package me.chongwish.zhess.stuff.virtual;

public class PieceRelation {
    private int pieceBits = 0x0000_0000;

    public void appendPieceId(byte id) {
        int positionBit = 0b1 << id;
        pieceBits |= positionBit;
    }

    public void removePieceId(byte id) {
        int positionBit = 0b1 << id;
        pieceBits ^= positionBit;
    }

    public byte[] getPieceIds() {
        byte[] states = new byte[32];
        int positionBit = 0b1;
        int count = 32;

        for (byte i = 0; i < 32; ++i) {
            positionBit = 0b1 << i;
            if ((pieceBits & positionBit) == 0) {
                --count;
                states[i] = Byte.MAX_VALUE;
            }
        }

        byte[] pieceIds = new byte[count];
        for (byte i = 31; i >= 0; --i) {
            if (states[i] != Byte.MAX_VALUE) {
                pieceIds[--count] = i;
            }
        }

        return pieceIds;
    }
}
