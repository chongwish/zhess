package me.chongwish.zhess.game.dark;

import java.util.function.BiConsumer;
import java.util.stream.IntStream;

import me.chongwish.zhess.stuff.Board;
import me.chongwish.zhess.stuff.Piece;
import me.chongwish.zhess.stuff.virtual.Factory;
import me.chongwish.zhess.stuff.virtual.Move;
import me.chongwish.zhess.stuff.virtual.PieceColor;
import me.chongwish.zhess.stuff.virtual.Player;

public class Game {
    private Piece[] pieces;
    private Board board;

    private boolean[] states = new boolean[32];

    private Player currentPlayer;

    /*
     * #######
     * Prepare
     * #######
     */

    {
        pieces = Factory.gainChineseChessPieces();
        board = Factory.gainBoard(8, 4);

        currentPlayer = new Player(PieceColor.Red);
        currentPlayer.take(pieces, board).with(new Player(PieceColor.Black));

        Preparation.definePieceRule(pieces, states);
        Preparation.definePiecePosition(pieces, board, states);
        Preparation.defineFirstPlayer(currentPlayer);
    }

    /*
     * ####
     * Hook
     * ####
     */

    public void hookInit(BiConsumer<Piece[], Board> init) {
        init.accept(pieces, board);
    }

    public void hookPlayerInit(BiConsumer<Player, Player> initPlayer) {
        if (initPlayer == null) {
            throw new RuntimeException("Please define player's behavior first!");
        }
        initPlayer.accept(currentPlayer, currentPlayer.getCompetitor());
    }

    /*
     * ####
     * Game
     * ####
     */

    public void begin() {
        while (true) {
            if (isEnd()) {
                break;
            }

            boolean hasError = false;
            do {
                try {
                    Move move = currentPlayer.thought();
                    if (move.getPieceId() > 31) {
                        int selectedPosition = move.getPosition();
                        if (selectedPosition < 0 || selectedPosition >= 32) {
                            throw new RuntimeException("The range of piece must be from 0 to 31");
                        }

                        if (states[selectedPosition]) {
                            throw new RuntimeException("This piece has been selected");
                        }

                        states[selectedPosition] = true;
                        currentPlayer.setActive(false);
                        currentPlayer.getCompetitor().setActive(true);
                    } else {
                        currentPlayer.move(move);
                    }

                    hasError = false;
                } catch (Exception e) {
                    hasError = true;

                    e.printStackTrace();
                }
            } while (hasError);

            currentPlayer = currentPlayer.getCompetitor();
        }
    }
    
    public boolean isEnd() {
        return IntStream.range(0, 16).filter(i -> pieces[i].getBoard() == null).count() == 16 || IntStream.range(16, 32).filter(i -> pieces[i].getBoard() == null).count() == 16;
    }

    public boolean[] getStates() {
        return states;
    }

}
