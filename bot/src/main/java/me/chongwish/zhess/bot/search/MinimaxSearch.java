package me.chongwish.zhess.bot.search;

import me.chongwish.zhess.bot.evaluation.Evaluation;
import me.chongwish.zhess.bot.quantization.ScoredMove;
import me.chongwish.zhess.bot.search.pruning.AlphaBetaPruning;
import me.chongwish.zhess.stuff.Board;
import me.chongwish.zhess.stuff.Piece;
import me.chongwish.zhess.stuff.virtual.PieceColor;

public class MinimaxSearch implements Search {
    private Piece[] pieces;
    private Board board;
    private Evaluation evaluation;

    private ScoredMove[] bestMoves;

    private static final int DEFAULT_DEPTH = 1;
    private int depth = DEFAULT_DEPTH;

    private AlphaBetaPruning alphaBetaPruning;

    public MinimaxSearch(Piece[] pieces, Board board, Evaluation evaluation) {
        this.pieces = pieces;
        this.board = board;
        this.evaluation = evaluation;
    }

    public void setEvaluation(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public void resetDepth() {
        this.depth = DEFAULT_DEPTH;
    }

    public MinimaxSearch enableAlphaBetaPruning() {
        alphaBetaPruning = new AlphaBetaPruning(depth * 2);
        return this;
    }

    public ScoredMove search(PieceColor color) {
        bestMoves = new ScoredMove[depth];
        for (int i = 0; i < bestMoves.length; ++i) {
            bestMoves[i] = new ScoredMove();
        }
        if (alphaBetaPruning != null) {
            alphaBetaPruning.initAlphaBeta(depth * 2);
        }
        search(color, depth * 2);
        return bestMoves[bestMoves.length - 1];
    }

    private int search(PieceColor color, int level) {
        if (level <= 0) {
            return evaluation.getValue(color);
        }

        int maxI = PieceColor.Red.equals(color) ? 16 : 32;
        boolean isMiniLevel = level % 2 == 1 ? true : false;
        Piece currentPiece = null;
        int bestScore = isMiniLevel ? Integer.MAX_VALUE : Integer.MIN_VALUE;

        for (int i = maxI - 16; i < maxI; ++i) {
            currentPiece = pieces[i];

            if (currentPiece.getBoard() == null) {
                continue ;
            }

            for (int nextPosition : currentPiece.getNextPositions()) {
                int previousPosition = currentPiece.getPosition();
                Piece nextPiece = board.atPoint(nextPosition);

                currentPiece.move(nextPosition);

                int score = search(PieceColor.Red.equals(color) ? PieceColor.Black : PieceColor.Red, level - 1);
                if (isMiniLevel) {
                    if (score < bestScore) {
                        bestScore = score;
                    }
                } else {
                    if (score > bestScore) {
                        bestScore = score;
                        bestMoves[level / 2 - 1].set(currentPiece.getId(), nextPosition, bestScore);
                    }
                }

                currentPiece.place(board, previousPosition);
                if (nextPiece != null) {
                    nextPiece.place(board, nextPosition);
                }

                if (alphaBetaPruning != null) {
                    if (alphaBetaPruning.generateStatementFunction(isMiniLevel).apply(level, score)) {
                        return score;
                    }
                    alphaBetaPruning.generateDataFunction(isMiniLevel).accept(level, score);
                }
            }
        }

        return bestScore;
    }
}
