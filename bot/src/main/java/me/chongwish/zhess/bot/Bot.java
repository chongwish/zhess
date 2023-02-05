package me.chongwish.zhess.bot;


import me.chongwish.zhess.bot.quantization.ScoredMove;
import me.chongwish.zhess.bot.search.Search;
import me.chongwish.zhess.stuff.Board;
import me.chongwish.zhess.stuff.Piece;
import me.chongwish.zhess.stuff.virtual.Factory;
import me.chongwish.zhess.stuff.virtual.PieceColor;

public abstract class Bot {
    private Piece[] piecesOnBrain;
    private Board boardOnBrain;
    private Search search;

    protected Bot(Piece[] pieces) {
        piecesOnBrain = new Piece[pieces.length];
        boardOnBrain = Factory.gainBoard(9, 10).bind(piecesOnBrain);

        for (int i = 0; i < pieces.length; ++i) {
            Piece piece = pieces[i];
            if (piece != null) {
                piecesOnBrain[i] = new Piece(piece.getId(), piece.getName(), piece.getColor(), piece.getType());
                piecesOnBrain[i].setCheckValidator(piece.getCheckValidator());
                piecesOnBrain[i].setPositionGenerator(piece.getPositionGenerator());
            }
        }

        this.search = createSearch(piecesOnBrain, boardOnBrain);
    }

    abstract protected Search createSearch(Piece[] pieces, Board board);

    public Bot sync(Piece[] pieces) {
        for (Piece piece: piecesOnBrain) {
            if (piece != null && piece.getBoard() != null) {
                boardOnBrain.makePointEmpty(piece.getPosition());
            }
        }

        for (int i = 0; i < pieces.length; ++i) {
            Piece piece = pieces[i];
            if (piece == null) {
                piecesOnBrain[i] = null;
            } else {
                if (piece.getBoard() == null) {
                    piecesOnBrain[i].clearBoard();
                } else {
                    piecesOnBrain[i].place(boardOnBrain, piece.getPosition());
                }
            }
        }

        return this;
    }

    public ScoredMove think(PieceColor color) {
        return search.search(color);
    }
}
