package net.swen225.hobbydetectives.tests;

import net.swen225.hobbydetectives.board.Board;
import net.swen225.hobbydetectives.card.CharacterCard;
import net.swen225.hobbydetectives.player.Player;
import net.swen225.hobbydetectives.ui.bean.BoardBean;
import net.swen225.hobbydetectives.ui.gui.GameFrame;

import javax.swing.*;
import java.util.Set;

public class DisplayGameFrame {

    private static final BoardBean bean = new BoardBean();

    static {
        Set<Player> players = Set.of(new Player(CharacterCard.LUCILLA, 11, 1), new Player(CharacterCard.BERT, 1, 9),
                new Player(CharacterCard.MALINA, 9, 22), new Player(CharacterCard.PERCY, 22, 14));

        bean.players(players);
        bean.board(new Board(players));

        bean.stepsLeft(5);
        bean.visible(true);

        bean.canGuess(true);
        bean.canMoveDown(false);
        bean.canMoveLeft(true);
        bean.canMoveUp(false);
        bean.canMoveRight(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.render(bean);
        });
    }
}
