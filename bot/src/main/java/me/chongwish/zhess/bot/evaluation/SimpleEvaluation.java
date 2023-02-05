package me.chongwish.zhess.bot.evaluation;

import me.chongwish.zhess.stuff.Board;
import me.chongwish.zhess.stuff.Piece;
import me.chongwish.zhess.stuff.virtual.PieceColor;

public class SimpleEvaluation implements Evaluation {
    public SimpleEvaluation(Piece[] pieces, Board board) {
        this.pieces = pieces;
    }

    private Piece[] pieces;

    private static final int[] pieceTypeIndex = new int[] {
        0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 6, 6, 6,
        0, 1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 6, 6, 6
    };

    private static final int[] pieceTypeValues = new int[] {
        1000, 20, 20, 90, 40, 45, 10
    };

    private static final int[][] redPieceTypePositionValues = new int[][] {
        {
                    0, 0, 0, 15, 20, 15, 0, 0, 0,
                    0, 0, 0, 10, 10, 10, 0, 0, 0,
                    0, 0, 0, 1, 1, 1, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        {
                    0, 0, 0, 30, 0, 30, 0, 0, 0,
                    0, 0, 0, 0, 22, 0, 0, 0, 0,
                    0, 0, 0, 30, 0, 30, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        {
                    0, 0, 30, 0, 0, 0, 30, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    20, 0, 0, 0, 35, 0, 0, 0, 20,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 25, 0, 0, 0, 25, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0
        },
        {
                    150, 160, 150, 160, 150, 160, 150, 160, 150,
                    160, 170, 160, 160, 150, 160, 160, 170, 160,
                    170, 180, 170, 170, 160, 170, 170, 180, 170,
                    170, 190, 200, 220, 240, 220, 200, 190, 170,
                    180, 220, 210, 240, 250, 240, 210, 220, 180,
                    180, 220, 210, 240, 250, 240, 210, 220, 180,
                    180, 220, 210, 240, 250, 240, 210, 220, 180,
                    170, 190, 200, 220, 240, 220, 200, 190, 170,
                    170, 180, 170, 190, 250, 190, 170, 180, 170,
                    160, 170, 160, 150, 150, 150, 160, 170, 160
        },
        {
                    60, 70, 75, 70, 60, 70, 75, 70, 60,
                    70, 75, 75, 70, 50, 70, 75, 75, 70,
                    80, 80, 90, 90, 80, 90, 90, 80, 80,
                    80, 90, 100, 100, 90, 100, 100, 90, 80,
                    90, 100, 100, 110, 100, 110, 100, 100, 90,
                    90, 110, 110, 120, 100, 120, 110, 110, 90,
                    90, 100, 120, 130, 110, 130, 120, 100, 90,
                    90, 100, 120, 125, 120, 125, 120, 100, 90,
                    80, 110, 125, 90, 70, 90, 125, 110, 80,
                    70, 80, 90, 80, 70, 80, 90, 80, 70
        },
        {
                    80, 90, 80, 70, 60, 70, 80, 90, 80,
                    80, 90, 80, 70, 65, 70, 80, 90, 80,
                    90, 100, 80, 80, 70, 80, 80, 100, 90,
                    90, 100, 90, 90, 110, 90, 90, 100, 90,
                    90, 100, 90, 110, 130, 110, 90, 100, 90,
                    90, 110, 90, 110, 130, 110, 90, 110, 90,
                    90, 110, 90, 110, 130, 110, 90, 110, 90,
                    100, 120, 90, 80, 80, 80, 90, 120, 100,
                    110, 125, 100, 70, 60, 70, 100, 125, 110,
                    125, 130, 100, 70, 60, 70, 100, 130, 125
        },
        {
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    0, 0, 0, 0, 0, 0, 0, 0, 0,
                    10, 0, 10, 0, 15, 0, 10, 0, 10,
                    10, 0, 15, 0, 15, 0, 15, 0, 10,
                    15, 20, 20, 20, 20, 20, 20, 20, 15,
                    20, 25, 25, 30, 30, 30, 25, 25, 20,
                    25, 30, 30, 40, 40, 40, 30, 30, 25,
                    25, 30, 40, 50, 60, 50, 40, 30, 25,
                    10, 10, 10, 20, 25, 20, 10, 10, 10
        }
    };

    private static final int[][] blackPieceTypePositionValues = new int[][] {
        Board.reverseBoard(redPieceTypePositionValues[0], 9, 10),
        Board.reverseBoard(redPieceTypePositionValues[1], 9, 10),
        Board.reverseBoard(redPieceTypePositionValues[2], 9, 10),
        Board.reverseBoard(redPieceTypePositionValues[3], 9, 10),
        Board.reverseBoard(redPieceTypePositionValues[4], 9, 10),
        Board.reverseBoard(redPieceTypePositionValues[5], 9, 10),
        Board.reverseBoard(redPieceTypePositionValues[6], 9, 10)
    };

    private static final int[][][] pieceTypePositionValues = new int[][][] { redPieceTypePositionValues, blackPieceTypePositionValues };


    public int getValue(PieceColor color) {
        int value = 0;

        int pieceTypePositionI = PieceColor.Red.equals(color) ? 0 : 1;
        int i = PieceColor.Red.equals(color) ? 0 : 16;

        value = sumTypeValueAndPositionValue(i, pieceTypePositionI) 
              - sumTypeValueAndPositionValue(((i + 16) ^ 0b0011_0000) - 16, pieceTypePositionI ^ 1);

        return value;
    }

    private int sumTypeValueAndPositionValue(int i, int pieceTypePositionI) {
        int result = 0;
        int maxI = i + 16;
        for (; i < maxI; ++i) {
            if (pieces[i].getBoard() != null) {
                result += (pieceTypeValues[pieceTypeIndex[i]] + pieceTypePositionValues[pieceTypePositionI][pieceTypeIndex[i]][pieces[i].getPosition()]);
            }
        }
        return result;
    }
}
