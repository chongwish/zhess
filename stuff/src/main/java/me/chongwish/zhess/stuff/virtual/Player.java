package me.chongwish.zhess.stuff.virtual;

import java.util.function.Function;

import me.chongwish.zhess.stuff.Board;
import me.chongwish.zhess.stuff.Piece;

public class Player {
    public Player(PieceColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    /*
     * #############
     * Player fields
     * #############
     */

    private PieceColor pieceColor;
    private Player competitor;
    private boolean active = false;

    /*
     * ###############
     * Relation fields
     * ###############
     */

    private Piece[] pieces;
    private Board board;

    /*
     * ############
     * Inner fields
     * ############
     */

    private Piece currentPiece;

    /*
     * ###########
     * Flag fields
     * ###########
     */

    private boolean isHumanFlag = true;
    private boolean thinkTwiceFlag = false;

    /*
     * ###############
     * Function fields
     * ###############
     */

    /**
     * Define the move rule of the piece
     */
    private Function<Player, Move> thinking;

    /*
     * #############
     * Player action
     * #############
     */

    /**
     * Play piece and board
     * @param pieces
     * @param board
     * @return
     */
    public Player take(Piece[] pieces, Board board) {
        this.pieces = pieces;
        this.board = board;
        board.bind(pieces);
        return this;
    }

    /**
     * Play with other palyer
     * @param competitor
     */
    public void with(Player competitor) {
        this.competitor = competitor;
        competitor.take(pieces, board);
        competitor.setCompetitor(this);
    }

    /**
     * Pick a piece in the position
     * @param position
     * @return
     */
    public Player pickFrom(int position) {
        if (board.isPointEmpty(position)) {
            throw new RuntimeException("Can not pick piece from this point!");
        }
        if (!board.isColorAtPoint(pieceColor, position)) {
            throw new RuntimeException("It's not your piece!");
        }
        currentPiece = board.atPoint(position);
        return this;
    }

    /**
     * Pick a piece by its foreign id
     * @param foreignId
     * @return
     */
    public Player pick(byte foreignId) {
        byte id = Piece.findByForeignId(foreignId);
        return pick(pieces[id]);
    }

    /**
     * Pick a piece
     * @param piece
     * @return
     */
    public Player pick(Piece piece) {
        if (!piece.getColor().equals(pieceColor)) {
            throw new RuntimeException("It's not your piece!");
        }
        if (piece.getBoard() == null) {
            throw new RuntimeException("Piece is not on the board!");
        }
        currentPiece = piece;
        return this;
    }

    /**
     * Put a piece to the given position
     * @param position
     */
    public void putOnto(int position) {
        if (!active) {
            throw new RuntimeException("It's not your turn!");
        }

        int[] positions = currentPiece.getNextPositions();
        for (int p: positions) {
            if (p == position) {
                currentPiece.move(position);
                
                active = false;
                competitor.setActive(true);
                return ;
            }
        }

        throw new RuntimeException("This position is not legal!");
    }

    /**
     * Move a piece on the board
     * @param move
     */
    public void move(Move move) {
        pick(pieces[move.getPieceId()]).putOnto(move.getPosition());
    }

    /**
     * Get a move by thinking
     * @return
     */
    public Move thought() {
        thinkTwiceFlag = false;
        return thinking.apply(this);
    }

    /**
     * How the player thinks
     * @param thinking
     */
    public void thinkLike(Function<Player, Move> thinking) {
        thinkTwiceFlag = true;
        this.thinking = thinking;
    }
    
    public boolean isNeedThinkTwice() {
        return thinkTwiceFlag;
    }

    public void setHumanFlag() {
        this.isHumanFlag = true;
    }

    public void unsetHumanFlag() {
        this.isHumanFlag = false;
    }

    public boolean isHuman() {
        return this.isHumanFlag;
    }

    /*
     * #########
     * Get & Set
     * #########
     */

    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public Piece[] getPieces() {
        return pieces;
    }

    public Board getBoard() {
        return board;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCompetitor(Player competitor) {
        this.competitor = competitor;
    }

    public Player getCompetitor() {
        return competitor;
    }
}
