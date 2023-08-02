package GUI;

import net.swen225.hobbydetectives.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;


/**
 * GUI class for displaying the game board of Hobby Detectives.
 */
public class GameGUI extends JFrame {
    private Game game; // board object
    private Map<Tile, Color> characterColors = new HashMap<>();

    private int cellSize = 16;
    private int xOffset = 50;
    private int yOffset = 50;

    // Functional buttons
    private JButton rollButton;
    private JButton upButton;
    private JButton downButton;
    private JButton rightButton;
    private JButton leftButton;


    /**
     * Constructor for the BoardGUI class.
     *
     * @param game The Game object which contains board, player, etc.
     */

    public GameGUI(Game game) {
        this.game = game;
        setTitle("Hobby Detectives Game");
        setSize(500 + xOffset, 500 + yOffset);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

//    private void addButtons() {
//        // Create and set up the functional buttons
//        rollButton = new JButton("Roll Dice");
//        upButton = new JButton("Up");
//
//        // Add action listeners to the buttons
//        rollButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Your code to handle the "Roll Dice" button click
//            }
//        });
//
//        upButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Your code to handle the "Move Player" button click
//            }
//        });
//
//        // Create a panel to hold the buttons
//        JPanel buttonPanel = new JPanel(null);
//        // Set the bounds (position and size) for each button
//        rollButton.setBounds(100, 200, 100, 30); // x, y, width, height
//        upButton.setBounds(120, 200, 100, 30); // x, y, width, height
//
//        buttonPanel.add(rollButton);
//        buttonPanel.add(upButton);
//
//        // Add the button panel to the frame
//        add(buttonPanel, BorderLayout.SOUTH);
//    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

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

        board.getRooms().forEach(
             r ->  {
                 if (r.getLocations() != Locations.GREY_AREA) {
                     g.setColor(Color.black);
                     g.drawString(r.getLocations().toString(), getXAxis(r.getY1()), getYAxis(r.getX1()));
                 }
             }
        );

        // Draw players
        game.getPlayerList().forEach(p -> {
            g.setColor(p.getColor());
            g.fillOval(getXAxis(p.y()), getYAxis(p.x()), cellSize, cellSize);
            g.setColor(Color.black);
            g.drawString(p.getName(), getXAxis(p.y()), getYAxis(p.x()));
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
