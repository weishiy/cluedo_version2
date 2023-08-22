package net.swen225.hobbydetectives.tests;

import net.swen225.hobbydetectives.board.Board;
import net.swen225.hobbydetectives.ui.bean.BoardBean;
import net.swen225.hobbydetectives.ui.gui.ControlPanel;

import javax.swing.*;
import java.util.Set;

public class DisplayControlPanel {
    private static final BoardBean bean = new BoardBean();

    static {
        bean.board(new Board());
        bean.players(Set.of());
        bean.stepsLeft(-1);
        bean.visible(true);

        bean.canGuess(true);
        bean.canMoveDown(true);
        bean.canMoveLeft(true);
        bean.canMoveUp(true);
        bean.canMoveRight(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("DisplayControlPanel");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ControlPanel controlPanel = new ControlPanel();

            controlPanel.render(bean);

            frame.setContentPane(controlPanel);
            frame.setMinimumSize(controlPanel.getMinimumSize());
            frame.setVisible(true);
        });
    }
}
