package me.chongwish.zhess.game.classic.rule;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

import me.chongwish.zhess.stuff.Board;
import me.chongwish.zhess.stuff.Piece;
import me.chongwish.zhess.stuff.virtual.PieceColor;

public class MoveRule {
    public static Function<Piece, int[]> General() {
        return (piece) -> {
            Board board = piece.getBoard();
            int position = piece.getPosition();
            PieceColor color = piece.getColor();

            int[] relativeTargetPositions = new int[] { -9, 1, 9, -1 };
            int[] validPositions = new int[] { 3, 4, 5, 12, 13, 14, 21, 22, 23, 66, 67, 68, 75, 76, 77, 84, 85, 86 };
            int[] targetPositions = new int[5];
            Arrays.setAll(targetPositions, x -> Integer.MAX_VALUE);

            int targetPosition;
            int count = 0;

            // king to king
            byte otherGeneralForeignId = (byte)(piece.getForeignId() ^ 0b0001_0000);
            int otherGeneralPosition = board.findPositionByForeignIdAndRange(otherGeneralForeignId, validPositions);
            if (isVertical(position, otherGeneralPosition)) {
                boolean isNoGuard = true;
                int minPosition = position;
                int maxPosition = otherGeneralPosition;

                if (position > otherGeneralForeignId) {
                    minPosition = otherGeneralPosition;
                    maxPosition = position;
                }

                for (int i = minPosition + 9; i < maxPosition - 9; i = i + 9) {
                    if (!board.isPointEmpty(i)) {
                        isNoGuard = false;
                        break;
                    }
                }

                if (isNoGuard) {
                    count = 1;
                    targetPositions[4] = otherGeneralPosition;
                }
            }

            for (int i = 0; i < relativeTargetPositions.length; ++i) {
                targetPosition = position + relativeTargetPositions[i];

                // on the board
                if (!board.isPointValid(targetPosition)) {
                    continue;
                }

                // our pieces lock
                if (board.isColorAtPoint(color, targetPosition)) {
                    continue;
                }

                // my area range
                if (Arrays.binarySearch(validPositions, targetPosition) < 0) {
                    continue;
                }

                targetPositions[i] = targetPosition;
                ++count;
            }

            return generateArray(targetPositions, count);
        };
    }

    public static Function<Piece, int[]> Mald() {
        return (piece) -> {
            Board board = piece.getBoard();
            int position = piece.getPosition();
            PieceColor color = piece.getColor();

            int[] relativeTargetPositions = new int[] { -8, 10, 8, -10 };
            int[] validPositions = new int[] { 3, 5, 13, 21, 23, 66, 68, 76, 84, 86 };

            int targetPosition;
            int count = 0;

            for (int i = 0; i < relativeTargetPositions.length; ++i) {
                targetPosition = position + relativeTargetPositions[i];

                // on the board
                if (!board.isPointValid(targetPosition)) {
                    relativeTargetPositions[i] = Integer.MAX_VALUE;
                    continue;
                }

                // our pieces lock
                if (board.isColorAtPoint(color, targetPosition)) {
                    relativeTargetPositions[i] = Integer.MAX_VALUE;
                    continue;
                }

                // my area range
                if (Arrays.binarySearch(validPositions, targetPosition) < 0) {
                    relativeTargetPositions[i] = Integer.MAX_VALUE;
                    continue;
                }

                relativeTargetPositions[i] = targetPosition;
                ++count;
            }

            return generateArray(relativeTargetPositions, count);
        };
    }

    public static Function<Piece, int[]> Elephant() {
        return (piece) -> {
            Board board = piece.getBoard();
            int position = piece.getPosition();
            PieceColor color = piece.getColor();

            int[] relativeTargetPositions = new int[] { -16, 20, 16, -20 };
            int[] validPositions = new int[] { 2, 6, 18, 22, 26, 38, 42, 47, 51, 63, 67, 71, 83, 87 };

            int targetPosition;
            int count = 0;

            for (int i = 0; i < relativeTargetPositions.length; ++i) {
                targetPosition = position + relativeTargetPositions[i];

                // on the board
                if (!board.isPointValid(targetPosition)) {
                    relativeTargetPositions[i] = Integer.MAX_VALUE;
                    continue;
                }

                // our pieces lock
                if (board.isColorAtPoint(color, targetPosition)) {
                    relativeTargetPositions[i] = Integer.MAX_VALUE;
                    continue;
                }

                // my area range
                if (Arrays.binarySearch(validPositions, targetPosition) < 0) {
                    relativeTargetPositions[i] = Integer.MAX_VALUE;
                    continue;
                }

                relativeTargetPositions[i] = targetPosition;
                ++count;
            }

            return generateArray(relativeTargetPositions, count);
        };
    }

    public static Function<Piece, int[]> Chariot() {
        return (piece) -> {
            Board board = piece.getBoard();
            int position = piece.getPosition();
            PieceColor color = piece.getColor();

            int[] steps = new int[] { -9, 1, 9, -1 };
            int[] targetPositions = new int[17];
            int count = 0;
            Function<Integer, BiFunction<Integer, Integer, Boolean>> fn = i ->  i % 2 == 0 ? MoveRule::isVertical : MoveRule::isHorizontal;

            for (int i = 0; i < steps.length; ++i) {
                int targetPosition = position + steps[i];
                while (board.isPointValid(targetPosition) && fn.apply(i).apply(position, targetPosition) && board.isPointEmpty(targetPosition)) {
                    targetPositions[count++] = targetPosition;
                    targetPosition += steps[i];
                }
                if (board.isPointValid(targetPosition) && fn.apply(i).apply(position, targetPosition) && !board.isColorAtPoint(color, targetPosition)) {
                    targetPositions[count++] = targetPosition;
                }
            }

            for (int i = count; i < targetPositions.length; ++i) {
                targetPositions[i] = Integer.MAX_VALUE;
            }

            return generateArray(targetPositions, count);
        };
    }

    public static Function<Piece, int[]> Horse() {
        return (piece) -> {
            Board board = piece.getBoard();
            PieceColor color = piece.getColor();

            int positionX = piece.getPosition() % 9;
            int positionY = piece.getPosition() / 9;

            int[] relativeTargetPositionsX = new int[] { 1, 2, 2, 1, -1, -2, -2, -1 };
            int[] relativeTargetPositionsY = new int[] { -2, -1, 1, 2, 2, 1, -1, -2 };
            int[] relativeLegPositionsX = new int[] { 0, 1, 1, 0, 0, -1, -1, 0 };
            int[] relativeLegPositionsY = new int[] { -1, 0, 0, 1, 1, 0, 0, -1 };
            int[] targetPositions = new int[8];
            Arrays.setAll(targetPositions, i -> Integer.MAX_VALUE);

            int targetPositionX;
            int targetPositionY;
            int targetPosition;
            int legPosition;
            int count = 0;

            for (int i = 0; i < 8; ++i) {
                targetPositionX = positionX + relativeTargetPositionsX[i];
                targetPositionY = positionY + relativeTargetPositionsY[i];
                targetPosition = targetPositionY * 9 + targetPositionX;
                legPosition = 9 * (positionY + relativeLegPositionsY[i]) + positionX + relativeLegPositionsX[i];

                // on the board
                if (targetPositionX < 0 || targetPositionX > 8 || targetPositionY < 0 || targetPositionY > 9) {
                    continue;
                }

                // horse leg
                if (!board.isPointEmpty(legPosition)) {
                    continue;
                }

                // our pieces lock
                if (board.isColorAtPoint(color, targetPosition)) {
                    continue;
                }

                targetPositions[i] = targetPosition;
                ++count;
            }

            return generateArray(targetPositions, count);
        };
    }

    public static Function<Piece, int[]> Catapult() {
        return (piece) -> {
            Board board = piece.getBoard();
            int position = piece.getPosition();
            PieceColor color = piece.getColor();

            int[] steps = new int[] { -9, 1, 9, -1 };
            int[] targetPositions = new int[17];
            int count = 0;
            Function<Integer, BiFunction<Integer, Integer, Boolean>> fn = i ->  i % 2 == 0 ? MoveRule::isVertical : MoveRule::isHorizontal;
            boolean hasPivot = false;

            for (int i = 0; i < steps.length; ++i) {
                int targetPosition = position + steps[i];
                while (board.isPointValid(targetPosition) && fn.apply(i).apply(position, targetPosition)) {
                    if (!board.isPointEmpty(targetPosition)) {
                        hasPivot = true;
                        targetPosition += steps[i];
                        break;
                    }
                    
                    targetPositions[count++] = targetPosition;
                    targetPosition += steps[i];
                }
                while (hasPivot && board.isPointValid(targetPosition) && fn.apply(i).apply(position, targetPosition)) {
                    if (!board.isPointEmpty(targetPosition) && !board.isColorAtPoint(color, targetPosition)) {
                        targetPositions[count++] = targetPosition;
                        break;
                    }
                    targetPosition += steps[i];
                }
            }

            for (int i = count; i < targetPositions.length; ++i) {
                targetPositions[i] = Integer.MAX_VALUE;
            }

            return generateArray(targetPositions, count);
        };
    }

    public static Function<Piece, int[]> Warrior() {
        return (piece) -> {
            Board board = piece.getBoard();
            int position = piece.getPosition();
            PieceColor color = piece.getColor();

            boolean hasGone = color.equals(PieceColor.Red) ? position >= 45 : position < 45;
            int step = color.equals(PieceColor.Red) ? 9 : -9;
            int[] specialSteps = new int[] { 1, -1 };
            int[] targetPositions = new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE };
            int count = 0;

            int targetPosition = position + step;
            if (board.isPointValid(targetPosition)) {
                if (!board.isColorAtPoint(color, targetPosition)) {
                    targetPositions[count++] = targetPosition;
                }
            }

            if (hasGone) {
                for (int specialStep : specialSteps) {
                    targetPosition = position + specialStep;
                    if (board.isPointValid(targetPosition) && isCross(position, targetPosition)) {
                        if (!board.isColorAtPoint(color, targetPosition)) {
                            targetPositions[count++] = targetPosition;
                        }
                    }
                }
            }

            return generateArray(targetPositions, count);
        };
    }

    private static int[] generateArray(int[] positions, int count) {
        int[] result = new int[count];

        for (int n: positions) {
            if (n != Integer.MAX_VALUE) {
                result[--count] = n;
            }
        }

        return result;
    }

    private static boolean isCross(int position1, int position2) {
        return position1 / 9 == position2 / 9 || position1 % 9 == position2 % 9;
    }

    private static boolean isVertical(int position1, int position2) {
        return position1 % 9 == position2 % 9;
    }

    private static boolean isHorizontal(int position1, int position2) {
        return position1 / 9 == position2 / 9;
    }
}
