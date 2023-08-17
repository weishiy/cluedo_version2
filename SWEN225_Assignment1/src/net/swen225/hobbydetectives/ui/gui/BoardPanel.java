package net.swen225.hobbydetectives.ui.gui;

import net.swen225.hobbydetectives.board.Board;
import net.swen225.hobbydetectives.board.Door;
import net.swen225.hobbydetectives.board.Estate;
import net.swen225.hobbydetectives.ui.bean.BoardBean;

import javax.swing.*;
import java.awt.*;

final class BoardPanel extends JPanel {
    private final int CELL_SIZE = 16;
    private final int X_OFFSET = 10;
    private final int Y_OFFSET = 10;

    /**
     * Contains the latest state of the board.
     */
    private BoardBean bean = null;

    private void render(BoardBean bean) {
        this.bean = bean;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        BoardBean bean = this.bean;
        ...
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Calling to clear the artifacts!
        super.paintComponent(g);

        //Draw space
        //Draw rooms
        //Draw floors and walls
        //Draw doors
        //Draw room name

        //Draw grey areas

        //Draw players

        var board = game.getBoard();

        var boardSize = Board.SIZE;
        var gridSize = boardSize * CELL_SIZE;
        var outerBorderSize = 4; // Adjust this value to set the thickness of the outer border

        // Draw tiles
        for (var i = 0; i < boardSize; i++) {
            for (var j = 0; j < boardSize; j++) {
                var x = tileToPixelX(j);
                var y = tileToPixelY(i);
                var tile = board.inspectTile(i, j);
                g.setColor(tile.tileType().color()); // set the color depends on tile
                g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }

        board.getRoomNamesAndPositions().forEach((roomName, position) -> {
            g.setColor(Color.black);
            g.drawString(roomName, tileToPixelX(position.getValue()), tileToPixelY(position.getKey()));
        });

        // Draw players
        game.getPlayerList().forEach(p -> {
            g.setColor(p.getColor());
            g.fillOval(tileToPixelX(p.getCurrentTileLocation().y()), tileToPixelY(p.getCurrentTileLocation().x()), CELL_SIZE, CELL_SIZE);
            g.setColor(Color.black);
            g.drawString(p.getName(), tileToPixelX(p.getCurrentTileLocation().y()), tileToPixelY(p.getCurrentTileLocation().x()));
        });

        // Draw grid
        for (var i = 0; i < boardSize; i++) {
            for (var j = 0; j < boardSize; j++) {
                var x = tileToPixelX(j);
                var y = tileToPixelY(i);
                g.setColor(Color.LIGHT_GRAY);
                g.drawLine(x, y, x, y + CELL_SIZE);
                g.drawLine(x, y, x + CELL_SIZE, y);
            }
        }

        // Draw canvas border
        ((Graphics2D) g).setStroke(new BasicStroke(outerBorderSize));
        g.setColor(Color.BLACK);
        g.drawRect(X_OFFSET, Y_OFFSET, gridSize, gridSize);
    }

    private void drawEstate(Graphics g, Estate estate) {
        //Draw floor and walls
        int left = tileToPixelX(estate.x1());
        int top = tileToPixelY(estate.y1());

        int right = tileToPixelX(estate.x2());
        int bottom = tileToPixelY(estate.y2());

        int width = right - left;
        int height = bottom - top;

        //Floor
        g.setColor(Color.WHITE);
        g.fillRect(left, top, width, height);

        //Walls
        g.setColor(Color.GRAY);
        g.fillRect(left, top, width, CELL_SIZE); //top wall
        g.fillRect(left, bottom - CELL_SIZE, width, CELL_SIZE); //bottom wall
        g.fillRect(left, top, CELL_SIZE, height); //left wall
        g.fillRect(right - CELL_SIZE, top, CELL_SIZE, height); //right wall

        //Draw doors
        g.setColor(Color.YELLOW);
        for (Door door : estate.doors()) {
            int leftDoor = tileToPixelX(door.x());
            int topDoor = tileToPixelY(door.y());
            g.fillRect(leftDoor, topDoor, CELL_SIZE, CELL_SIZE);
        }

        //TODO: Draw Estate names
    }

    //Returns the pixel location (top-left) of the given tile coordinate
    private int tileToPixelX(int j) {
        return j * CELL_SIZE + X_OFFSET;
    }

    private int tileToPixelY(int i) {
        return i * CELL_SIZE + Y_OFFSET;
    }
}
