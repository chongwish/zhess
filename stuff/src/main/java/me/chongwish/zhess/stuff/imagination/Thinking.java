package me.chongwish.zhess.stuff.imagination;

import java.util.function.Function;

import me.chongwish.zhess.stuff.virtual.Move;
import me.chongwish.zhess.stuff.virtual.Player;

public class Thinking {
    public static Function<Player, Move> Human(Function<Player, Move> action) {
        return (player) -> {
            player.setHumanFlag();
            Move move = action.apply(player);
            return move;
        };
    }

    public static Function<Player, Move> Bot(Function<Player, Move> action, long millis) {
        return (player) -> {
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            player.unsetHumanFlag();
            Move move = action.apply(player);
            return move;
        };
    }
}
