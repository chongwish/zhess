package me.chongwish.zhess.stuff;

import java.util.function.BiFunction;
import java.util.function.Function;

import me.chongwish.zhess.stuff.virtual.PieceColor;
import me.chongwish.zhess.stuff.virtual.PieceType;

public class Piece {
    public Piece(byte id, String name, PieceColor color, PieceType type) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.type = type;

        this.foreignId = (byte)(id | 0b0010_0000);
    }

    /*
     * ############
     * Piece fields
     * ############
     */

    private byte id;
    private byte foreignId;
    private String name;
    private PieceType type;
    private PieceColor color;

    /*
     * ###############
     * Position fields
     * ###############
     */

    private Board board;
    private int position = Integer.MAX_VALUE;

    /*
     * #########################################
     * Function fields for extending dynamically
     * #########################################
     */

    /**
     * Generate next position
     */
    private Function<Piece, int[]> positionGenerator = (piece) -> new int[0];

    /**
     * Validate whether a check happend
     */
    private BiFunction<Piece, Integer, Boolean> checkValidator = (piece, position) -> false;

    /*
     * ############ 
     * Piece action
     * ############ 
     */

    /**
     * Find id of piece by it's foreign id
     * @param foreignId
     * @return
     */
    public static byte findByForeignId(byte foreignId) {
        return (byte)(foreignId & 0b0001_1111);
    }

    /**
     * Get all the positions that this piece may go
     * @return 
     */
    public int[] getNextPositions() {
        return positionGenerator.apply(this);
    }

    /**
     * Whether a check happend
     * @param position
     * @return
     */
    public boolean check(int position) {
        return checkValidator.apply(this, position);
    }

    /**
     * Decouple the piece and the board
     */
    public void clearBoard() {
        board = null;
    }

    /**
     * Put a piece on the board without any rule
     * @param board
     * @param position
     */
    public void place(Board board, int position) {
        if (this.position != Integer.MAX_VALUE) {
            board.makePointEmpty(this.position);
        }

        this.board = board;
        this.position = position;

        board.markPoint(position, this);
    }

    /**
     * Move a piece on the board with some rule
     * @param position
     */
    public void move(int position) {
        if (board == null) {
            throw new RuntimeException("Piece must be on the board!");
        }

        board.makePointEmpty(this.position);

        this.position = position;

        board.markPoint(position, this);
    }

    
    /*
     * #######
     * Get&Set
     * #######
     */

    public byte getId() {
        return id;
    }

    public byte getForeignId() {
        return foreignId;
    }

    public String getName() {
        return name;
    }

    public PieceType getType() {
        return type;
    }

    public PieceColor getColor() {
        return color;
    }

    public Board getBoard() {
        return board;
    }

    public int getPosition() {
        return position;
    }

    public void setPositionGenerator(Function<Piece, int[]> positionGenerator) {
        this.positionGenerator = positionGenerator;
    }

    public Function<Piece, int[]> getPositionGenerator() {
        return positionGenerator;
    }

    public void setCheckValidator(BiFunction<Piece, Integer, Boolean> checkGenerator) {
        this.checkValidator = checkGenerator;
    }

    public BiFunction<Piece, Integer, Boolean> getCheckValidator() {
        return checkValidator;
    }
}
