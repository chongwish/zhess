package me.chongwish.zhess.appearance.text;


import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.input.MouseAction;
import com.googlecode.lanterna.input.MouseActionType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.MouseCaptureMode;
import com.googlecode.lanterna.terminal.Terminal;

import me.chongwish.zhess.bot.Bot;
import me.chongwish.zhess.bot.codename.Apple;
import me.chongwish.zhess.bot.codename.Banana;
import me.chongwish.zhess.game.classic.Game;
import me.chongwish.zhess.stuff.Board;
import me.chongwish.zhess.stuff.Piece;
import me.chongwish.zhess.stuff.imagination.Thinking;
import me.chongwish.zhess.stuff.virtual.Move;
import me.chongwish.zhess.stuff.virtual.PieceColor;
import me.chongwish.zhess.stuff.virtual.Player;

public class Main {
    private static DefaultTerminalFactory defaultTerminalFactory;
    private static Terminal terminal;
    private static Screen screen;
    private static TerminalSize terminalSize;

    private static volatile boolean isUsed = false;

    private static TextGraphics boardTextGraphics;
    private final static String boardPointChar = "・";

    private static TextGraphics redPiecesTextGraphics;
    private static TextGraphics blackPiecesTextGraphics;

    private static TextGraphics hintTextGraphics;
    private static TextGraphics selectedTextGraphics;
    private static Piece selectedPiece;

    private static int xBegin;
    private static int yBegin;

    private static Game game;
    private static Bot appleBot;
    private static Bot bananaBot;

    private static int blackPlayerTypeIndex = 0;
    private static int redPlayerTypeIndex = 0;

    private static Player redPlayer;
    private static Player blackPlayer;

    public static void main(String[] args) {
        try {
            initLanterna();

            game = new Game();
            game.hookInit(Main::init);
            game.hookPlayerInit((redPlayer, blackPlayer) -> {
                redPlayer.thinkLike(Thinking.Human(Main::humanAction));
                blackPlayer.thinkLike(Thinking.Human(Main::humanAction));
                Main.redPlayer = redPlayer;
                Main.blackPlayer = blackPlayer;
            });
            game.begin();

            showWinner();
            
            screen.close();
            terminal.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Move appleBotAction(Player player) {
        Move move = appleBot.sync(player.getPieces()).think(player.getPieceColor());
        Piece piece = player.getPieces()[move.getPieceId()];
        botAction(move, piece);
        return move;
    }

    public static Move bananaBotAction(Player player) {
        Move move = bananaBot.sync(player.getPieces()).think(player.getPieceColor());
        Piece piece = player.getPieces()[move.getPieceId()];
        botAction(move, piece);
        return move;
    }

    private static void botAction(Move move, Piece piece) {
        try {
            isUsed = false;
            removeSelectedPiece();
            int originX = xBegin + piece.getPosition() % 9 * 2;
            int originY = yBegin + piece.getPosition() / 9;
            int targetX = xBegin + move.getPosition() % 9 * 2;
            int targetY = yBegin + move.getPosition() / 9;
            TextGraphics textGraphics = screen.newTextGraphics();
            textGraphics.setBackgroundColor(boardTextGraphics.getBackgroundColor());
            textGraphics.setForegroundColor(textGraphics.getCharacter(originX, originY).getForegroundColor());
            textGraphics.putString(targetX, targetY, textGraphics.getCharacter(originX, originY).getCharacterString());
            boardTextGraphics.putString(originX, originY, boardPointChar);
            screen.refresh();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void removeSelectedPiece() {
        if (selectedPiece != null) {
            int originX = xBegin + selectedPiece.getPosition() % 9 * 2;
            int originY = yBegin + selectedPiece.getPosition() / 9;
            TextGraphics textGraphics = screen.newTextGraphics();
            textGraphics.setBackgroundColor(boardTextGraphics.getBackgroundColor());
            for (int p : selectedPiece.getNextPositions()) {
                int targetX = xBegin + p % 9 * 2;
                int targetY = yBegin + p / 9;
                textGraphics.setForegroundColor(textGraphics.getCharacter(targetX, targetY).getForegroundColor());
                textGraphics.putString(targetX, targetY,
                        textGraphics.getCharacter(targetX, targetY).getCharacterString());
            }
            textGraphics.setForegroundColor(textGraphics.getCharacter(originX, originY).getForegroundColor());
            textGraphics.putString(originX, originY, textGraphics.getCharacter(originX, originY).getCharacterString());
            selectedPiece = null;
        }
    }

    public static Move humanAction(Player player) {
        try {
            isUsed = true;
            while (true) {
                if (player.isNeedThinkTwice()) {
                    return player.thought();
                }

                Thread.sleep(100);
                KeyStroke keyStroke = terminal.pollInput();

                if (keyStroke != null && keyStroke.getKeyType() == KeyType.MouseEvent) {
                    MouseAction action = (MouseAction) keyStroke;

                    addjustPlayer(action.getPosition().getColumn(), action.getPosition().getRow());

                    int x = (action.getPosition().getColumn() - xBegin) / 2;
                    int y = action.getPosition().getRow() - yBegin;

                    boolean isClicked = action.getActionType() == MouseActionType.CLICK_DOWN;
                    boolean isAreaValid = x >= 0 && x < 9 && y >= 0 && y < 10;

                    if (!isClicked || !isAreaValid) {
                        continue;
                    }

                    if (selectedPiece != null) {
                        int movePosition = Integer.MAX_VALUE;
                        int originX = xBegin + selectedPiece.getPosition() % 9 * 2;
                        int originY = yBegin + selectedPiece.getPosition() / 9;
                        TextGraphics textGraphics = screen.newTextGraphics();
                        textGraphics.setBackgroundColor(boardTextGraphics.getBackgroundColor());
                        for (int p: selectedPiece.getNextPositions()) {
                            int targetX = xBegin + p % 9 * 2;
                            int targetY = yBegin + p / 9;
                            if (x == p % 9 && y == p / 9) {
                                movePosition = p;
                                textGraphics.setForegroundColor(textGraphics.getCharacter(originX, originY).getForegroundColor());
                                textGraphics.putString(targetX, targetY, textGraphics.getCharacter(originX, originY).getCharacterString());
                                boardTextGraphics.putString(originX, originY, boardPointChar);
                            } else {
                                textGraphics.setForegroundColor(textGraphics.getCharacter(targetX, targetY).getForegroundColor());
                                textGraphics.putString(targetX, targetY, textGraphics.getCharacter(targetX, targetY).getCharacterString());
                            }
                        }
                        if (movePosition != Integer.MAX_VALUE) {
                            screen.refresh();
                            Move move = new Move();
                            move.setPieceId(selectedPiece.getId());
                            move.setPosition(movePosition);
                            selectedPiece = null;
                            isUsed = false;
                            return move;
                        } else {
                            textGraphics.setForegroundColor(textGraphics.getCharacter(originX, originY).getForegroundColor());
                            textGraphics.putString(originX, originY, textGraphics.getCharacter(originX, originY).getCharacterString());
                        }
                    }

                    selectedPiece = player.getBoard().atPoint(y * 9 + x);
                    if (selectedPiece == null) {
                        continue;
                    }

                    if (!selectedPiece.getColor().equals(player.getPieceColor())) {
                        selectedPiece = null;
                        continue;
                    }

                    selectedTextGraphics.setForegroundColor(selectedTextGraphics.getCharacter(x * 2 + xBegin, y + yBegin).getForegroundColor());
                    selectedTextGraphics.putString(x * 2 + xBegin, y + yBegin, selectedPiece.getName());
                    for (int p : selectedPiece.getNextPositions()) {
                        int targetX = xBegin + p % 9 * 2;
                        int targetY = yBegin + p / 9;
                        hintTextGraphics.setForegroundColor(hintTextGraphics.getCharacter(targetX, targetY).getForegroundColor());
                        hintTextGraphics.putString(targetX, targetY,
                                boardTextGraphics.getCharacter(targetX, targetY).getCharacterString());
                    }
                    screen.refresh();
                }
                defineShortcut(keyStroke);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void validTerminalSize() throws IOException {
        terminalSize = terminal.getTerminalSize();
        if (terminalSize.getColumns() < 40 || terminalSize.getRows() < 20) {
            throw new RuntimeException("Terminal size must be large than 40.");
        }
    }

    public static void initLanterna() throws Exception {
        defaultTerminalFactory = new DefaultTerminalFactory();
        //defaultTerminalFactory.setMouseCaptureMode(MouseCaptureMode.CLICK);
        defaultTerminalFactory.setMouseCaptureMode(MouseCaptureMode.CLICK_RELEASE_DRAG);

        terminal = defaultTerminalFactory.createTerminal();
        terminal.setCursorVisible(false);

        validTerminalSize();

        screen = new TerminalScreen(terminal);
        screen.startScreen();

        CompletableFuture.runAsync(() -> {
            try {
                while (true) {
                    Thread.sleep(100);
                    if (!isUsed) {
                        KeyStroke keyStroke = terminal.pollInput();
                        defineShortcut(keyStroke);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void init(Piece[] pieces, Board board) {
        try {
            xBegin = (terminalSize.getColumns() - 2 * 9) / 2;
            yBegin = (terminalSize.getRows() - 10) / 2;

            boardTextGraphics = screen.newTextGraphics();
            boardTextGraphics.setForegroundColor(TextColor.ANSI.WHITE_BRIGHT);
            boardTextGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
            for (int y = yBegin - 4; y < yBegin + 14; ++y) {
                boardTextGraphics.putString(xBegin - 8, y, " ".repeat(34));
            }
            for (int y = yBegin; y < yBegin + 10; ++y) {
                boardTextGraphics.putString(xBegin, y, boardPointChar.repeat(9));
            }
            String[] scNumber = new String[] { "一", "二", "三", "四", "五", "六", "七", "八", "九", "十" };
            for (int y = 0; y < 10; ++y) {
                boardTextGraphics.putString(xBegin - 6, yBegin + y, scNumber[y]);
            }
            boardTextGraphics.putString(xBegin, yBegin - 3, "壹贰叁肆伍陆柒捌玖");

            redPiecesTextGraphics = screen.newTextGraphics();
            redPiecesTextGraphics.setForegroundColor(TextColor.ANSI.RED);
            redPiecesTextGraphics.setBackgroundColor(TextColor.ANSI.WHITE);

            redPiecesTextGraphics.putString(xBegin - 1, yBegin + 11, "<" + " ".repeat(18) + ">");
            redPiecesTextGraphics.putString(xBegin + (18 - "Human".length()) / 2, yBegin + 11, "Human");

            blackPiecesTextGraphics = screen.newTextGraphics();
            blackPiecesTextGraphics.setForegroundColor(TextColor.ANSI.GREEN);
            blackPiecesTextGraphics.setBackgroundColor(TextColor.ANSI.WHITE);

            blackPiecesTextGraphics.putString(xBegin - 1, yBegin + 12, "<" + " ".repeat(18) + ">");
            blackPiecesTextGraphics.putString(xBegin + (18 - "Human".length()) / 2, yBegin + 12, "Human");

            hintTextGraphics = screen.newTextGraphics();
            hintTextGraphics.setBackgroundColor(TextColor.ANSI.BLUE_BRIGHT);

            selectedTextGraphics = screen.newTextGraphics();
            selectedTextGraphics.setBackgroundColor(TextColor.ANSI.YELLOW);

            TextGraphics piecesTextGraphics;
            for (Piece piece : pieces) {
                piecesTextGraphics = PieceColor.Red.equals(piece.getColor()) ? redPiecesTextGraphics
                        : blackPiecesTextGraphics;
                int x = piece.getPosition() % 9 * 2;
                int y = piece.getPosition() / 9;
                piecesTextGraphics.putString(x + xBegin, y + yBegin, piece.getName());
            }
            screen.refresh();

            appleBot = new Apple(pieces);
            bananaBot = new Banana(pieces);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addjustPlayer(int column, int row) throws IOException {
        boolean isRed;
        boolean isPlus;
        if (column == xBegin - 1 && row == yBegin + 11) {
            isRed = true;
            isPlus = false;
        } else if (column == xBegin - 1 && row == yBegin + 12) {
            isRed = false;
            isPlus = false;
        } else if (column == xBegin + 18 && row == yBegin + 11) {
            isRed = true;
            isPlus = true;
        } else if (column == xBegin + 18 && row == yBegin + 12) {
            isRed = false;
            isPlus = true;
        } else {
            return;
        }
        String[] nameList = new String[] { "Human", "AppleBot", "BananaBot" };
        int index;
        int y;
        int x;
        TextGraphics textGraphics;
        Player player;
        if (isRed) {
            redPlayerTypeIndex = (isPlus ? redPlayerTypeIndex + 1 : redPlayerTypeIndex - 1) % nameList.length;
            index = redPlayerTypeIndex;
            y = yBegin + 11; 
            textGraphics = redPiecesTextGraphics;
            player = redPlayer;
        } else {
            blackPlayerTypeIndex = (isPlus ? blackPlayerTypeIndex + 1 : blackPlayerTypeIndex - 1) % nameList.length;
            index = blackPlayerTypeIndex;
            y = yBegin + 12;
            textGraphics = blackPiecesTextGraphics;
            player = blackPlayer;
        }
        index = (index + nameList.length) % nameList.length;
        x = xBegin + (18 - nameList[index].length()) / 2;
        boardTextGraphics.putString(xBegin + 2, y, " ".repeat(14));
        textGraphics.putString(x, y, nameList[index]);
        screen.refresh();

        switch (nameList[index]) {
            case "Human":
                player.thinkLike(Thinking.Human(Main::humanAction));
            break;
            case "AppleBot":
                player.thinkLike(Thinking.Bot(Main::appleBotAction, 1000));
            break;
            case "BananaBot":
                player.thinkLike(Thinking.Bot(Main::bananaBotAction, 1000));
            break;
        }
    }

    private static void showWinner() throws IOException {
        Player winner = game.getWinner();
        String name = winner.getPieceColor().toString();
        boardTextGraphics.putString(xBegin - 1, yBegin + 11, " ".repeat(20));
        boardTextGraphics.putString(xBegin - 1, yBegin + 12, " ".repeat(20));
        boardTextGraphics.putString(xBegin - 1, yBegin + 12, "Winner is " + name + " Player");
        screen.refresh();
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void defineShortcut(KeyStroke keyStroke) throws IOException {
        if (keyStroke != null && keyStroke.getKeyType() == KeyType.MouseEvent) {
            MouseAction action = (MouseAction) keyStroke;
            addjustPlayer(action.getPosition().getColumn(), action.getPosition().getRow());
        }
    }
}
