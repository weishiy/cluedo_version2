package net.swen225.hobbydetectives.ui.view;

import net.swen225.hobbydetectives.ui.bean.BoardBean;
import net.swen225.hobbydetectives.ui.controller.Controller;

public interface BoardUI {
    /**
     * Subscribes the controller to this UI's input.
     *
     * @param controller The controller which manages/interprets the input.
     */
    void addController(Controller controller);

    /**
     * Renders the game board using the given <code>boardBean</code>.
     *
     * @param boardBean The data used to render the board.
     */
    void render(BoardBean boardBean);
}
