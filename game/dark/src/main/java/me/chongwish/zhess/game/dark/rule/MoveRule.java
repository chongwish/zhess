package me.chongwish.zhess.game.dark.rule;

import java.util.Arrays;
import java.util.function.Function;

import me.chongwish.zhess.stuff.Board;
import me.chongwish.zhess.stuff.Piece;
import me.chongwish.zhess.stuff.virtual.PieceColor;

public class MoveRule {
    public MoveRule(boolean[] states) {
        if (states.length != 32) {
            throw new RuntimeException("It needs a 32 length array!");
        }
        this.states = states;
    }

    private static int values[] = new int[] {
        7, 6, 6, 5, 5, 4, 4, 3, 3, 2, 2, 1, 1, 1, 1, 1,
        7, 6, 6, 5, 5, 4, 4, 3, 3, 2, 2, 1, 1, 1, 1, 1
    };

    private boolean[] states;
    
    final public Function<Piece, int[]> Normal = (piece) -> {
        Board board = piece.getBoard();
        int position = piece.getPosition();
        PieceColor color = piece.getColor();

        int[] relativeTargetPositions = new int[] { -8, 1, 8, -1 };
        int[] targetPositions = new int[4];
        Arrays.setAll(targetPositions, x -> Integer.MAX_VALUE);

        int targetPosition;
        int count = 0;

        if (!states[position]) {
            return new int[] {};
        }
        
        for (int i = 0; i < relativeTargetPositions.length; ++i) {
            targetPosition = position + relativeTargetPositions[i];

            if (!board.isPointValid(targetPosition)) {
                continue;
            }

            if (!states[targetPosition]) {
                continue;
            }
            
            if (!board.isPointEmpty(targetPosition)) {
                if (board.isColorAtPoint(color, targetPosition)) {
                    continue;
                } else {
                    int targetValue = values[board.atPoint(targetPosition).getId()];
                    int value = values[piece.getId()];
                    if (value < targetValue && !(value == 1 && targetValue == 7)) {
                        continue;
                    } else if (value == 7 && targetValue == 1) {
                        continue;
                    }
                }
            }

            targetPositions[i] = targetPosition;
            ++count;
        }

        int[] result = new int[count];
        for (int n: targetPositions) {
            if (n != Integer.MAX_VALUE) {
                result[--count] = n;
            }
        }

        return result;
    };
}
