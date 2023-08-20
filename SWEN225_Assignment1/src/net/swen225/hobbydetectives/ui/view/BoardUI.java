package net.swen225.hobbydetectives.ui.view;

import net.swen225.hobbydetectives.ui.bean.BoardBean;
import net.swen225.hobbydetectives.ui.controller.Controller;

public interface BoardUI {
    /**
     * Subscribes the controller to this UI's input.
     * <p>
     * Only one controller can be subscribed at a time. If called with null, unsets the current controller.
     *
     * @param controller The controller which manages/interprets the input.
     */
    void setController(Controller controller);

    /**
     * Renders the game board using the given <code>boardBean</code>.
     *
     * @param boardBean The data used to render the board.
     */
    void render(BoardBean boardBean);
}
