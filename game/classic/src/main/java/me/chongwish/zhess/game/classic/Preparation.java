package me.chongwish.zhess.game.classic;

import me.chongwish.zhess.game.classic.rule.MoveRule;
import me.chongwish.zhess.stuff.Board;
import me.chongwish.zhess.stuff.Piece;
import me.chongwish.zhess.stuff.virtual.Player;

public class Preparation {
    public static void definePieceRule(Piece[] pieces) {
        pieces[0].setPositionGenerator(MoveRule.General());
        pieces[1].setPositionGenerator(MoveRule.Mald());
        pieces[2].setPositionGenerator(MoveRule.Mald());
        pieces[3].setPositionGenerator(MoveRule.Elephant());
        pieces[4].setPositionGenerator(MoveRule.Elephant());
        pieces[5].setPositionGenerator(MoveRule.Chariot());
        pieces[6].setPositionGenerator(MoveRule.Chariot());
        pieces[7].setPositionGenerator(MoveRule.Horse());
        pieces[8].setPositionGenerator(MoveRule.Horse());
        pieces[9].setPositionGenerator(MoveRule.Catapult());
        pieces[10].setPositionGenerator(MoveRule.Catapult());
        pieces[11].setPositionGenerator(MoveRule.Warrior());
        pieces[12].setPositionGenerator(MoveRule.Warrior());
        pieces[13].setPositionGenerator(MoveRule.Warrior());
        pieces[14].setPositionGenerator(MoveRule.Warrior());
        pieces[15].setPositionGenerator(MoveRule.Warrior());
        pieces[16].setPositionGenerator(MoveRule.General());
        pieces[17].setPositionGenerator(MoveRule.Mald());
        pieces[18].setPositionGenerator(MoveRule.Mald());
        pieces[19].setPositionGenerator(MoveRule.Elephant());
        pieces[20].setPositionGenerator(MoveRule.Elephant());
        pieces[21].setPositionGenerator(MoveRule.Chariot());
        pieces[22].setPositionGenerator(MoveRule.Chariot());
        pieces[23].setPositionGenerator(MoveRule.Horse());
        pieces[24].setPositionGenerator(MoveRule.Horse());
        pieces[25].setPositionGenerator(MoveRule.Catapult());
        pieces[26].setPositionGenerator(MoveRule.Catapult());
        pieces[27].setPositionGenerator(MoveRule.Warrior());
        pieces[28].setPositionGenerator(MoveRule.Warrior());
        pieces[29].setPositionGenerator(MoveRule.Warrior());
        pieces[30].setPositionGenerator(MoveRule.Warrior());
        pieces[31].setPositionGenerator(MoveRule.Warrior());
    }

    public static void definePiecePosition(Piece[] pieces, Board board) {
        pieces[0].place(board, 4);
        pieces[1].place(board, 3);
        pieces[2].place(board, 5);
        pieces[3].place(board, 2);
        pieces[4].place(board, 6);
        pieces[5].place(board, 0);
        pieces[6].place(board, 8);
        pieces[7].place(board, 1);
        pieces[8].place(board, 7);
        pieces[9].place(board, 19);
        pieces[10].place(board, 25);
        pieces[11].place(board, 27);
        pieces[12].place(board, 29);
        pieces[13].place(board, 31);
        pieces[14].place(board, 33);
        pieces[15].place(board, 35);
        pieces[16].place(board, 85);
        pieces[17].place(board, 84);
        pieces[18].place(board, 86);
        pieces[19].place(board, 83);
        pieces[20].place(board, 87);
        pieces[21].place(board, 81);
        pieces[22].place(board, 89);
        pieces[23].place(board, 82);
        pieces[24].place(board, 88);
        pieces[25].place(board, 64);
        pieces[26].place(board, 70);
        pieces[27].place(board, 54);
        pieces[28].place(board, 56);
        pieces[29].place(board, 58);
        pieces[30].place(board, 60);
        pieces[31].place(board, 62);
    }

    public static void defineFirstPlayer(Player player) {
        player.setActive(true);
    }
}
