package net.swen225.hobbydetectives.tests;

import net.swen225.hobbydetectives.board.Board;
import net.swen225.hobbydetectives.ui.bean.BoardBean;
import net.swen225.hobbydetectives.ui.gui.GameFrame;

import javax.swing.*;
import java.util.Set;

public class DisplayGameFrame {

    private static final BoardBean bean = new BoardBean();

    static {
        bean.board(new Board());
        bean.players(Set.of());
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
