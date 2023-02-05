package me.chongwish.zhess.bot.search;

import me.chongwish.zhess.bot.quantization.ScoredMove;
import me.chongwish.zhess.stuff.virtual.PieceColor;

public interface Search {
    public ScoredMove search(PieceColor color);
}
