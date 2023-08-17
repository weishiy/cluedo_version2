package net.swen225.hobbydetectives.ui.gui;

import net.swen225.hobbydetectives.Game;
import net.swen225.hobbydetectives.board.Board;

import javax.swing.*;
import java.awt.*;


/**
 * GUI class for displaying the game board of Hobby Detectives.
 * <p>
 * This class is deprecated as the game interface must be text-based.
 */
@Deprecated
public class BoardPanel extends JFrame {
    private Game game; // board object

    private ControlPanel controlPanel;

    /**
     * Constructor for the BoardPanel class.
     *
     * @param game The Game object which contains board, player, etc.
     */

    public BoardPanel(Game game) {
        this.game = game;

        setTitle("Hobby Detectives Game");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        var gamePanel = new GamePanel(game);
        gamePanel.setBounds(10, 10, 400, 400);
        add(gamePanel);

        controlPanel = new ControlPanel();

        add(controlPanel);

    }


    private static final class GamePanel extends JPanel {
        private final Game game; // board object
        private final int cellSize = 16;
        private int xOffset = 10;
        private int yOffset = 10;

        private GamePanel(Game game) {
            this.game = game;
        }

        @Override
        protected void paintComponent(Graphics g) {
            // Calling to clear the artifacts!
            super.paintComponent(g);

            var board = game.getBoard();

            var boardSize = Board.SIZE;
            var gridSize = boardSize * cellSize;
            var outerBorderSize = 4; // Adjust this value to set the thickness of the outer border

            // Draw tiles
            for (var i = 0; i < boardSize; i++) {
                for (var j = 0; j < boardSize; j++) {
                    var x = getXAxis(j);
                    var y = getYAxis(i);
                    var tile = board.inspectTile(i, j);
                    g.setColor(tile.tileType().color()); // set the color depends on tile
                    g.fillRect(x, y, cellSize, cellSize);
                }
            }

            board.getRoomNamesAndPositions().forEach((roomName, position) -> {
                g.setColor(Color.black);
                g.drawString(roomName, getXAxis(position.getValue()), getYAxis(position.getKey()));
            });

            // Draw players
            game.getPlayerList().forEach(p -> {
                g.setColor(p.getColor());
                g.fillOval(getXAxis(p.getCurrentTileLocation().y()), getYAxis(p.getCurrentTileLocation().x()), cellSize, cellSize);
                g.setColor(Color.black);
                g.drawString(p.getName(), getXAxis(p.getCurrentTileLocation().y()), getYAxis(p.getCurrentTileLocation().x()));
            });

            // Draw grid
            for (var i = 0; i < boardSize; i++) {
                for (var j = 0; j < boardSize; j++) {
                    var x = getXAxis(j);
                    var y = getYAxis(i);
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawLine(x, y, x, y + cellSize);
                    g.drawLine(x, y, x + cellSize, y);
                }
            }

            // Draw canvas border
            ((Graphics2D) g).setStroke(new BasicStroke(outerBorderSize));
            g.setColor(Color.BLACK);
            g.drawRect(xOffset, yOffset, gridSize, gridSize);
        }

        private int getXAxis(int j) {
            return j * cellSize + xOffset;
        }

        private int getYAxis(int i) {
            return i * cellSize + yOffset;
        }
    }

}
