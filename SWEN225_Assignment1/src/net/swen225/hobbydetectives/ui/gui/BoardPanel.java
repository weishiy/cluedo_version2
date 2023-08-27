package net.swen225.hobbydetectives.ui.gui;

import net.swen225.hobbydetectives.board.Board;
import net.swen225.hobbydetectives.board.Door;
import net.swen225.hobbydetectives.board.Estate;
import net.swen225.hobbydetectives.player.Player;
import net.swen225.hobbydetectives.ui.bean.BoardBean;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public final class BoardPanel extends JPanel {


    /**
     * Contains the latest state of the board.
     */
    private BoardBean bean = null;

    public BoardPanel() {
        setMinimumSize(new Dimension(150, 150));
    }

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

    /**
     * Draws a string, bounded to a set area.
     *
     * @param g        The graphics instance to draw with.
     * @param drawArea The bounds where we want to write in.
     * @param string   The string we want to write.
     */
    private static void drawString(Graphics2D g, Rectangle2D drawArea, String string) {
        //Solution from: https://stackoverflow.com/a/4658361. Scale large font down.
        //Large font to base initial image.
        final Font font = new Font(null, Font.PLAIN, 24);

        FontMetrics metrics = g.getFontMetrics(font);
        //In baseline-relative coordinates, so x=0, y is negative.
        Rectangle2D fontSpace = metrics.getStringBounds(string, g);

        AffineTransform transform = proportionalTransform(drawArea, fontSpace);

        Graphics2D g1 = (Graphics2D) g.create();

        g1.transform(transform);
        g1.drawString(string, 0, 0);
    }

    private static void drawString(Graphics2D g, String string) {
        Rectangle2D bounds = g.getClipBounds();
        drawString(g, bounds, string);
    }

    /**
     * Creates a transform that maps from the oldUserSpace to newUserSpace, but scales proportionally.
     *
     * @param newUserSpace The new space to map to.
     * @param oldUserSpace The old space to map from.
     * @return A transform that transforms points from the old space to the new space.
     */
    private static AffineTransform proportionalTransform(Rectangle2D newUserSpace, Rectangle2D oldUserSpace) {
        //"Origin space" refers to space where (0,0) marks center of system.

        //Methods on transform are concatenated from the right. If you're thinking of individual steps to get from a
        //point in oldUserSpace to a point in newUserSpace, follow transformations backwards.

        AffineTransform transform = new AffineTransform();

        //Finally translate from origin space to new user space.
        transform.translate(newUserSpace.getX(), newUserSpace.getY());

        //Scale transform to size of draw area, keeping x and y proportional.
        double scale = Math.min(newUserSpace.getWidth() / oldUserSpace.getWidth(),
                newUserSpace.getHeight() / oldUserSpace.getHeight());

        transform.scale(scale, scale);

        //Initial translate from old user space to origin space.
        transform.translate(-oldUserSpace.getX(), -oldUserSpace.getY());

        return transform;
    }

    public void render(BoardBean bean) {
        this.bean = bean;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Calling to clear the artifacts!
        super.paintComponent(g);

        //requires to have non-null bean to draw board.
        if (bean == null || bean.board() == null) {
            return;
        }

        int cellLength = getCellLength(this, bean.board());

        //Clip the original graphics to a centered square.
        int length = Math.min(getWidth(), getHeight());

        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        int left = centerX - length / 2;
        int top = centerY - length / 2;

        Graphics squareGraphic = g.create(left, top, length, length);

        //Draw empty space
        squareGraphic.setColor(Color.MAGENTA);
        //We use `cellLength * width/height` rather than local var `length`, so our empty space only includes valid cells.
        squareGraphic.fillRect(0, 0, cellLength * bean.board().getWidth(), cellLength * bean.board().getHeight());

        drawEstates(squareGraphic, cellLength);

        drawGreyAreas(squareGraphic, cellLength);

        drawPlayers(squareGraphic, cellLength);

        drawGrid(squareGraphic, cellLength);
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

    private void drawGrid(Graphics g, int cellLength) {
        g.setColor(Color.LIGHT_GRAY);

        //Vertical lines
        for (var i = 1; i < bean.board().getWidth(); i++) {
            int x = i * cellLength;
            g.drawLine(x, 0, x, bean.board().getHeight() * cellLength);
        }

        //Horizontal lines
        for (var j = 1; j < bean.board().getHeight(); j++) {
            int y = j * cellLength;
            g.drawLine(0, y, bean.board().getWidth() * cellLength, y);
        }
    }

}
