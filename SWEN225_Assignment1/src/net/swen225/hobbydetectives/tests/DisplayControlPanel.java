package net.swen225.hobbydetectives.tests;

import net.swen225.hobbydetectives.board.Board;
import net.swen225.hobbydetectives.card.CharacterCard;
import net.swen225.hobbydetectives.player.Player;
import net.swen225.hobbydetectives.ui.bean.BoardBean;
import net.swen225.hobbydetectives.ui.gui.ControlPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class DisplayControlPanel {
    public static final boolean DO_DEBUG = false;
    private static final BoardBean bean = new BoardBean();

    static {
        Set<Player> players = Set.of(new Player(CharacterCard.LUCILLA, 11, 1), new Player(CharacterCard.BERT, 1, 9),
                new Player(CharacterCard.MALINA, 9, 22), new Player(CharacterCard.PERCY, 22, 14));

        bean.players(players);
        bean.board(new Board(players));
        bean.stepsLeft(-1);

        bean.canGuess(true);
        bean.canMoveDown(true);
        bean.canMoveLeft(true);
        bean.canMoveUp(true);
        bean.canMoveRight(true);

        bean.currentPlayer(players.iterator().next());
    }

    /**
     * Prints bounds of given component and that component's children.
     *
     * @param doDebug   Whether to debug.
     * @param component The component to debug.
     */
    private static void debug(boolean doDebug, Component component) {
        if (doDebug) {
            System.out.println(component.getClass().getName());
            System.out.println(component.getBounds());

            System.out.println();

            if (component instanceof Container container) {
                for (Component component1 : container.getComponents()) {
                    debug(true, component1);
                }
            }

        }
    }

    private static void debug(Component component) {
        debug(DO_DEBUG, component);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("DisplayControlPanel");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ControlPanel controlPanel = new ControlPanel() {
                @Override
                public void paint(Graphics g) {
                    debug(this);
                    super.paint(g);
                }
            };

            controlPanel.render(bean);

            frame.setContentPane(controlPanel);
            frame.setMinimumSize(controlPanel.getMinimumSize());
            frame.setVisible(true);
        });
    }
}
