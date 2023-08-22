package net.swen225.hobbydetectives.ui.gui;

import net.swen225.hobbydetectives.board.Board;
import net.swen225.hobbydetectives.board.Door;
import net.swen225.hobbydetectives.board.Estate;
import net.swen225.hobbydetectives.player.Player;
import net.swen225.hobbydetectives.ui.bean.BoardBean;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class BoardPanel extends JPanel {


    /**
     * Contains the latest state of the board.
     */
    private BoardBean bean = null;

    //Used to write the Positions
    private static String firstLetterInName(Player player) {
        return player.characterCard().toString().substring(0, 1).toUpperCase();
    }

    /**
     * Returns dynamic cell length dependent on this panel's size and the size of the board.
     *
     * @param board     The board which generates the tiles.
     * @param component The component which displays the tiles.
     * @return The cell length.
     */
    private static int getCellLength(JComponent component, Board board) {

        int widthInTiles = board.getWidth();
        int widthInPixels = component.getWidth();
        int widthRatio = Math.floorDiv(widthInPixels, widthInTiles);

        int heightInTiles = board.getHeight();
        int heightInPixels = component.getHeight();
        int heightRatio = Math.floorDiv(heightInPixels, heightInTiles);

        //Return the smallest of the two, so all cells fit even if one dimension doesn't.
        return Math.min(widthRatio, heightRatio);
    }

    public void render(BoardBean bean) {
        this.bean = bean;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Calling to clear the artifacts!
        super.paintComponent(g);

        int cellLength = getCellLength(this, bean.board());

        //Draw empty space
        g.setColor(Color.MAGENTA);
        g.fillRect(0, 0, bean.board().getWidth() * cellLength, bean.board().getHeight() * cellLength);

        drawEstates(g, cellLength);

        drawGreyAreas(g, cellLength);

        drawPlayers(g, cellLength);

//        // Draw grid
//        for (var i = 0; i < boardSize; i++) {
//            for (var j = 0; j < boardSize; j++) {
//                var x = tileToPixelX(j);
//                var y = tileToPixelY(i);
//                g.setColor(Color.LIGHT_GRAY);
//                g.drawLine(x, y, x, y + CELL_SIZE);
//                g.drawLine(x, y, x + CELL_SIZE, y);
//            }
//        }
//
//        // Draw canvas border
//        ((Graphics2D) g).setStroke(new BasicStroke(outerBorderSize));
//        g.setColor(Color.BLACK);
//        g.drawRect(X_OFFSET, Y_OFFSET, gridSize, gridSize);
    }

    private void drawEstate(Graphics g, int cellLength, Estate estate) {
        //Draw floor and walls
        int left = estate.x1() * cellLength;
        int top = estate.y1() * cellLength;

        //Coordinates are inclusive, add one to get far edge.
        int right = (estate.x2() + 1) * cellLength;
        int bottom = (estate.y2() + 1) * cellLength;

        int width = right - left;
        int height = bottom - top;

        //Floor
        g.setColor(Color.WHITE);
        g.fillRect(left, top, width, height);

        //Walls
        g.setColor(Color.GRAY);
        g.fillRect(left, top, width, cellLength); //top wall
        g.fillRect(left, bottom - cellLength, width, cellLength); //bottom wall
        g.fillRect(left, top, cellLength, height); //left wall
        g.fillRect(right - cellLength, top, cellLength, height); //right wall

        //Draw doors
        g.setColor(Color.YELLOW);
        for (Door door : estate.doors()) {
            int leftDoor = door.x() * cellLength;
            int topDoor = door.y() * cellLength;
            g.fillRect(leftDoor, topDoor, cellLength, cellLength);
        }

        //TODO: Draw Estate names
    }

    //Draws all estates
    private void drawEstates(Graphics g, int cellLength) {
        for (Estate estate : bean.board().getRooms()) {
            drawEstate(g, cellLength, estate);
        }
    }

    private void drawGreyAreas(Graphics g, int cellLength) {
        g.setColor(Color.GRAY);
        for (Estate greyArea : bean.board().getGreyAreas()) {
            int left = greyArea.x1() * cellLength;
            int top = greyArea.y1() * cellLength;
            //Coordinates are inclusive, add one to get far edge.
            int right = (greyArea.x2() + 1) * cellLength;
            int bottom = (greyArea.y2() + 1) * cellLength;

            int width = right - left;
            int height = bottom - top;

            g.fillRect(left, top, width, height);
        }
    }

    private void drawPlayers(Graphics g, int cellLength) {
        Set<Player> players = bean.players();
        //Each Point corresponds to the first letter of the players in that spot.
        //Multiple players may be in the same spot if, for example, they're in the same estate.
        Map<Point, String> pointToLetters = players.stream().collect(Collectors.groupingBy(p -> new Point(p.x(), p.y()),
                Collectors.mapping(BoardPanel::firstLetterInName, Collectors.joining())));

        //FIXME:Players inside estates are drawn inside the doorways (Jeremy)

        for (var entry : pointToLetters.entrySet()) {
            int left = entry.getKey().x * cellLength;
            int top = entry.getKey().y * cellLength;
            String letters = entry.getValue();
            //TODO: Modify font so that characters are correct size (Jeremy)
            //Ensures characters are drawn inside cell
            Graphics clippedGraphic = g.create(left, top, cellLength, cellLength);
            clippedGraphic.setColor(Color.BLACK);
            clippedGraphic.drawString(letters, 0, cellLength);
        }

    }

}
