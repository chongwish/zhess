package me.chongwish.zhess.bot.quantization;

import me.chongwish.zhess.stuff.virtual.Move;

public class ScoredMove extends Move {
    private int score = Integer.MIN_VALUE;

    public int getScore() {
        return score;
    }

    public void set(byte pieceId, int position, int score)  {
        setPieceId(pieceId);
        setPosition(position);
        this.score = score;
    }
}
