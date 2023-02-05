package me.chongwish.zhess.stuff;

import me.chongwish.zhess.stuff.virtual.PieceColor;

public class Board {
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        points = new byte[width * height];
    }

    /*
     * ############
     * Board fields
     * ############
     */

    /**
     * Board width
     */
    private int width;

    /**
     * Board height
     */
    private int height;

    /**
     * 1-d array to keep the size of board
     */
    private byte[] points;

    /*
     * ############
     * Piece fields
     * ############
     */

    /**
     * Pieces that is only on the board
     */
    private Piece[] pieces;

    /*
     * ############
     * Board action
     * ############
     */

     /**
      * Bind board and piece
      * @param pieces
      * @return
      */
     public Board bind(Piece[] pieces) {
        this.pieces = pieces;
        return this;
     }

    /**
     * Reset the size of board
     * @param width
     * @param length
     */
    public void setCapacity(int width, int length) {
        points = new byte[width * length];
    }

    /**
     * Is the given position on the board
     * @param position
     * @return
     */
    public boolean isPointValid(int position) {
        // return position >= 0 && position < points.length;
        if (position < 0) {
            return false;
        }
        int x = position % width;
        int y = position / 9;
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Is nothing in the given position
     * @param position
     * @return
     */
    public boolean isPointEmpty(int position) {
        if (!isPointValid(position)) {
            throw new RuntimeException("The board position is not valid!");
        }
        return points[position] == 0;
    }

    /**
     * Is the given color piece in the given position
     * @param color
     * @param position
     * @return
     */
    public boolean isColorAtPoint(PieceColor color, int position) {
        if (isPointEmpty(position)) {
            return false;
        }
        return (points[position] & 0b0001_0000) == (color.equals(PieceColor.Red) ? 0 : 0b0001_0000);
    }

    /**
     * Clean the piece information on the board, but piece keeps the information as before
     * @param position
     */
    public void makePointEmpty(int position) {
        points[position] = 0;
    }

    /**
     * Record a piece on the board
     * @param position
     * @param piece
     */
    public void markPoint(int position, Piece piece) {
        if (!isPointEmpty(position)) {
            byte id = Piece.findByForeignId(points[position]);
            pieces[id].clearBoard();
        }
        points[position] = piece.getForeignId();
        //pieces[piece.getId()] = piece;
    }

    /**
     * Reverse all the pieces on the board up and down
     * @param positions
     * @return
     */
    public static int[] reverseBoard(int[] positions, int width, int height) {
        int[] result = new int[positions.length];

        for (int i = 0; i < height; ++i) {
            for (int j = 0; j < width; ++j) {
                result[(height - 1 - i) * width + j] = positions[i * width + j];
            }
        }
        
        return result;
    }

    /**
     * Get the piece in the given position
     * @param position
     * @return
     */
    public Piece atPoint(int position) {
        if (isPointEmpty(position)) {
            return null;
        }
        byte id = Piece.findByForeignId(points[position]);
        return pieces[id];
    }

    public int findPositionByForeignId(byte foreignId) {
        for (int i = 0; i < points.length; ++i) {
            if (points[i] == foreignId) {
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }

    public int findPositionByForeignIdAndRange(byte foreignId, int[] range) {
        for (int i: range) {
            if (i > 0 && i < points.length && points[i] == foreignId) {
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }
}
