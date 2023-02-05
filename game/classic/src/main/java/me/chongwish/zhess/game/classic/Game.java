package me.chongwish.zhess.game.classic;

import java.util.function.BiConsumer;

import me.chongwish.zhess.stuff.Board;
import me.chongwish.zhess.stuff.Piece;
import me.chongwish.zhess.stuff.virtual.Factory;
import me.chongwish.zhess.stuff.virtual.Move;
import me.chongwish.zhess.stuff.virtual.PieceColor;
import me.chongwish.zhess.stuff.virtual.Player;

public class Game {
    private Piece[] pieces;
    private Board board;

    private Player currentPlayer;

    private volatile boolean isRunning = true;

    /*
     * #######
     * Prepare
     * #######
     */

    {
        pieces = Factory.gainChineseChessPieces();
        board = Factory.gainBoard(9, 10);

        currentPlayer = new Player(PieceColor.Red);
        currentPlayer.take(pieces, board).with(new Player(PieceColor.Black));

        Preparation.definePieceRule(pieces);
        Preparation.definePiecePosition(pieces, board);
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

    public synchronized void pause() {
        this.isRunning = false;
    }

    public synchronized void resume() {
        this.isRunning = true;
    }

    public void begin() {
        while (true) {
            while (!isRunning) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (isEnd()) {
                break;
            }

            boolean hasError = false;
            do {
                try {
                    Move move = currentPlayer.thought();
                    currentPlayer.move(move);

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
        return pieces[16].getBoard() == null || pieces[0].getBoard() == null;
    }

    public Player getWinner() {
        PieceColor color = pieces[16].getBoard() == null ? PieceColor.Red: (pieces[0].getBoard() == null ? PieceColor.Black : null);
        if (color == null) {
            return null;
        }
        if (currentPlayer.getPieceColor().equals(color)) {
            return currentPlayer;
        }
        return currentPlayer.getCompetitor();
    }
}
