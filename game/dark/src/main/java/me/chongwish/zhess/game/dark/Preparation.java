package me.chongwish.zhess.game.dark;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import me.chongwish.zhess.game.dark.rule.MoveRule;
import me.chongwish.zhess.stuff.Board;
import me.chongwish.zhess.stuff.Piece;
import me.chongwish.zhess.stuff.virtual.Player;

public class Preparation {
    public static void definePieceRule(Piece[] pieces, boolean[] states) {
        MoveRule rule = new MoveRule(states);

        for (Piece piece: pieces) {
            piece.setPositionGenerator(rule.Normal);
        }
    }

    public static void definePiecePosition(Piece[] pieces, Board board, boolean[] states) {
        List<Integer> positions = IntStream.range(0, 32).boxed().collect(Collectors.toList());
        Collections.shuffle(positions);

        for (int i = 0; i < 32; ++i) {
            pieces[i].place(board, positions.get(i));
            states[i] = false;
        }
    }

    public static void defineFirstPlayer(Player player) {
        player.setActive(true);
    }
}
