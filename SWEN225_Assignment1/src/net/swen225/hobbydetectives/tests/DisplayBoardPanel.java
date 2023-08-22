package net.swen225.hobbydetectives.tests;

import net.swen225.hobbydetectives.board.Board;
import net.swen225.hobbydetectives.ui.bean.BoardBean;
import net.swen225.hobbydetectives.ui.gui.BoardPanel;

import javax.swing.*;
import java.util.Set;

public class DisplayBoardPanel {
    private static final BoardBean bean = new BoardBean();

    static {
        bean.board(new Board());
        bean.players(Set.of());
        bean.stepsLeft(-1);
        bean.visible(true);

        bean.canGuess(false);
        bean.canMoveDown(false);
        bean.canMoveLeft(false);
        bean.canMoveUp(false);
        bean.canMoveRight(false);
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("DisplayBoardPanel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            BoardPanel boardPanel = new BoardPanel();
            frame.setContentPane(boardPanel);

            boardPanel.render(bean);

            //Display the window.
            frame.setMinimumSize(boardPanel.getMinimumSize());
            frame.setVisible(true);
        });
    }
}
