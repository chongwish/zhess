package me.chongwish.zhess.bot.codename;

import me.chongwish.zhess.bot.Bot;
import me.chongwish.zhess.bot.evaluation.SimpleEvaluation;
import me.chongwish.zhess.bot.search.MinimaxSearch;
import me.chongwish.zhess.bot.search.Search;
import me.chongwish.zhess.stuff.Board;
import me.chongwish.zhess.stuff.Piece;

public class Apple extends Bot {
    public Apple(Piece[] pieces) {
        super(pieces);
    }

    protected Search createSearch(Piece[] pieces, Board board) {
        return new MinimaxSearch(pieces, board, new SimpleEvaluation(pieces, board));
    }
}
