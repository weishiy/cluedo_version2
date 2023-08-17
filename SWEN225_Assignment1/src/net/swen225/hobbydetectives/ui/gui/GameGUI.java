package net.swen225.hobbydetectives.ui.gui;

import net.swen225.hobbydetectives.Game;

import javax.swing.*;


/**
 * GUI class for displaying the game board of Hobby Detectives.
 * <p>
 * This class is deprecated as the game interface must be text-based.
 */
@Deprecated
public class GameGUI extends JFrame {
    private Game game; // board object

    private ControlPanel controlPanel;

    /**
     * Constructor for the GameGUI class.
     *
     * @param game The Game object which contains board, player, etc.
     */

    public GameGUI(Game game) {
        this.game = game;

        setTitle("Hobby Detectives Game");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        var gamePanel = new BoardPanel(game);
        gamePanel.setBounds(10, 10, 400, 400);
        add(gamePanel);

        controlPanel = new ControlPanel();

        add(controlPanel);

    }


}
