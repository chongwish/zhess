package me.chongwish.zhess.game.classic.rule;

import java.util.function.BiFunction;

import me.chongwish.zhess.stuff.Piece;

public class CheckRule {
    public static BiFunction<Piece, Integer, Boolean> Horse() {
        return (piece, position) -> {
            for (int nextPosition: piece.getNextPositions()) {
                if (nextPosition == position) {
                    return true;
                }
            }
            return false;
        };
    }
}
