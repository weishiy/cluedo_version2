package net.swen225.hobbydetectives.ui.gui;

import javax.swing.*;

/**
 * GUI class for displaying the game board of Hobby Detectives.
 */
public class GameFrame extends JFrame {

    private final ControlPanel controlPanel;
    private final BoardPanel boardPanel;

    /**
     * Constructor for the GameFrame class.
     */

    public GameFrame() {

        setTitle("Hobby Detectives Game");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        var gamePanel = new BoardPanel();
        gamePanel.setBounds(10, 10, 400, 400);
        add(gamePanel);

        controlPanel = new ControlPanel();

        add(controlPanel);

        boardPanel = new BoardPanel();

        add(boardPanel);
    }


}
