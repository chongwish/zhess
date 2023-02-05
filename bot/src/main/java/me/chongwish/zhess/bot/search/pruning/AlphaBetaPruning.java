package me.chongwish.zhess.bot.search.pruning;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class AlphaBetaPruning {
    private int[] alphaBeta;

    public AlphaBetaPruning(int n) {
        initAlphaBeta(n);
    }

    public void initAlphaBeta(int n) {
        alphaBeta = new int[n];
        int[] preValues = new int[] { Integer.MAX_VALUE, Integer.MIN_VALUE };
        for (int i = 0; i < alphaBeta.length; ++i) {
            alphaBeta[i] = preValues[i % 2];
        }
    }

    public BiFunction<Integer, Integer, Boolean> generateStatementFunction(boolean isMiniLevel) {
        // It doesn't need to compare if at the first level
        if (isMiniLevel) {
            return (level, score) -> {
                return level < alphaBeta.length - 1 && score > alphaBeta[level];
            };
        }
        return (level, score) -> {
            return level < alphaBeta.length - 1 && score < alphaBeta[level];
        };
    }

    public BiConsumer<Integer, Integer> generateDataFunction(boolean isMiniLevel) {
        // Save the best score at current level
        if (isMiniLevel) {
            return (level, score) -> {
                if (score < alphaBeta[level - 1]) {
                    alphaBeta[level - 1] = score;
                }
            };
        }
        return (level, score) -> {
            if (score > alphaBeta[level - 1]) {
                alphaBeta[level - 1] = score;
            }
        };
    }
}
