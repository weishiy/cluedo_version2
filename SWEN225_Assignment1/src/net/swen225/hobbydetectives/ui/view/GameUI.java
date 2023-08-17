package net.swen225.hobbydetectives.ui.view;

import net.swen225.hobbydetectives.card.model.Card;
import net.swen225.hobbydetectives.ui.bean.BoardBean;
import net.swen225.hobbydetectives.ui.bean.ChooseCardBean;
import net.swen225.hobbydetectives.ui.bean.PauseMessageBean;

import java.util.concurrent.Future;

/**
 * Manages the view of the game.
 */
public interface GameUI {
    /**
     * Renders the game board using the given <code>boardBean</code>.
     *
     * @param boardBean The data used to render the board.
     */
    void render(BoardBean boardBean);

    /**
     * Creates a dialogue for selecting a card, and returns a future containing the selected card.
     *
     * @param chooseCardBean The data used to make the dialogue. Contains cards to select from.
     * @param <T>            The type of the selected card.
     * @return The future that receives the selected card.
     */
    <T extends Card> Future<T> render(ChooseCardBean chooseCardBean);

    /**
     * Creates a dialogue that waits to be dismissed, returning a future that indicates dismissal.
     *
     * @param pauseMessageBean The data used to make the dialogue.
     * @return The future that indicates the dialogue has been dismissed. Contains <code>null</code>.
     */
    Future<?> render(PauseMessageBean pauseMessageBean);
}

