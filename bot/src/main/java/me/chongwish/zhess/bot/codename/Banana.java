package me.chongwish.zhess.bot.codename;

import me.chongwish.zhess.bot.Bot;
import me.chongwish.zhess.bot.evaluation.SimpleEvaluation;
import me.chongwish.zhess.bot.search.MinimaxSearch;
import me.chongwish.zhess.bot.search.Search;
import me.chongwish.zhess.stuff.Board;
import me.chongwish.zhess.stuff.Piece;

public class Banana extends Bot {
    public Banana(Piece[] pieces) {
        super(pieces);
    }

    protected Search createSearch(Piece[] pieces, Board board) {
        MinimaxSearch minimaxSearch = new MinimaxSearch(pieces, board, new SimpleEvaluation(pieces, board));
        minimaxSearch.setDepth(2);
        minimaxSearch.enableAlphaBetaPruning();
        return minimaxSearch;
    }
}
