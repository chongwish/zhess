package me.chongwish.zhess.stuff.virtual;

import me.chongwish.zhess.stuff.Board;
import me.chongwish.zhess.stuff.Piece;

public abstract class Factory {
    public static Piece[] gainChineseChessPieces() {
        Piece[] pieces = new Piece[32];

        makePiece(pieces, (byte)0b0000_0000, "帅", PieceColor.Red, PieceType.General);
        makePiece(pieces, (byte)0b0000_0001, "仕", PieceColor.Red, PieceType.Mald);
        makePiece(pieces, (byte)0b0000_0010, "仕", PieceColor.Red, PieceType.Mald);
        makePiece(pieces, (byte)0b0000_0011, "相", PieceColor.Red, PieceType.Elephant);
        makePiece(pieces, (byte)0b0000_0100, "相", PieceColor.Red, PieceType.Elephant);
        makePiece(pieces, (byte)0b0000_0101, "俥", PieceColor.Red, PieceType.Chariot);
        makePiece(pieces, (byte)0b0000_0110, "俥", PieceColor.Red, PieceType.Chariot);
        makePiece(pieces, (byte)0b0000_0111, "傌", PieceColor.Red, PieceType.Horse);
        makePiece(pieces, (byte)0b0000_1000, "傌", PieceColor.Red, PieceType.Horse);
        makePiece(pieces, (byte)0b0000_1001, "炮", PieceColor.Red, PieceType.Catapult);
        makePiece(pieces, (byte)0b0000_1010, "炮", PieceColor.Red, PieceType.Catapult);
        makePiece(pieces, (byte)0b0000_1011, "兵", PieceColor.Red, PieceType.Warrior);
        makePiece(pieces, (byte)0b0000_1100, "兵", PieceColor.Red, PieceType.Warrior);
        makePiece(pieces, (byte)0b0000_1101, "兵", PieceColor.Red, PieceType.Warrior);
        makePiece(pieces, (byte)0b0000_1110, "兵", PieceColor.Red, PieceType.Warrior);
        makePiece(pieces, (byte)0b0000_1111, "兵", PieceColor.Red, PieceType.Warrior);

        makePiece(pieces, (byte)0b0001_0000, "将", PieceColor.Black, PieceType.General);
        makePiece(pieces, (byte)0b0001_0001, "士", PieceColor.Black, PieceType.Mald);
        makePiece(pieces, (byte)0b0001_0010, "士", PieceColor.Black, PieceType.Mald);
        makePiece(pieces, (byte)0b0001_0011, "象", PieceColor.Black, PieceType.Elephant);
        makePiece(pieces, (byte)0b0001_0100, "象", PieceColor.Black, PieceType.Elephant);
        makePiece(pieces, (byte)0b0001_0101, "车", PieceColor.Black, PieceType.Chariot);
        makePiece(pieces, (byte)0b0001_0110, "车", PieceColor.Black, PieceType.Chariot);
        makePiece(pieces, (byte)0b0001_0111, "马", PieceColor.Black, PieceType.Horse);
        makePiece(pieces, (byte)0b0001_1000, "马", PieceColor.Black, PieceType.Horse);
        makePiece(pieces, (byte)0b0001_1001, "砲", PieceColor.Black, PieceType.Catapult);
        makePiece(pieces, (byte)0b0001_1010, "砲", PieceColor.Black, PieceType.Catapult);
        makePiece(pieces, (byte)0b0001_1011, "卒", PieceColor.Black, PieceType.Warrior);
        makePiece(pieces, (byte)0b0001_1100, "卒", PieceColor.Black, PieceType.Warrior);
        makePiece(pieces, (byte)0b0001_1101, "卒", PieceColor.Black, PieceType.Warrior);
        makePiece(pieces, (byte)0b0001_1110, "卒", PieceColor.Black, PieceType.Warrior);
        makePiece(pieces, (byte)0b0001_1111, "卒", PieceColor.Black, PieceType.Warrior);

        return pieces;
    }

    public static Board gainBoard(int width, int height) {
        return new Board(width, height);
    }

    private static void makePiece(Piece[] pieces, byte id, String name, PieceColor color, PieceType type) {
        pieces[id] = new Piece(id, name, color, type);
    }
}
