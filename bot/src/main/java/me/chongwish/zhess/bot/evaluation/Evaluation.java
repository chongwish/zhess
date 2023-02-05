package me.chongwish.zhess.bot.evaluation;

import me.chongwish.zhess.stuff.virtual.PieceColor;

public interface Evaluation {
    public int getValue(PieceColor color);
}
