package net.swen225.hobbydetectives.ui.view;

import net.swen225.hobbydetectives.card.model.Card;
import net.swen225.hobbydetectives.ui.bean.BoardBean;
import net.swen225.hobbydetectives.ui.bean.ChooseCardBean;
import net.swen225.hobbydetectives.ui.bean.PauseMessageBean;

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
     * Creates a dialogue for selecting a card, and waits for the player to select the card.
     *
     * @param chooseCardBean The data used to make the dialogue. Contains cards to select from.
     * @param <T>            The type of the selected card.
     * @return The selected card.
     */
    <T extends Card> T render(ChooseCardBean chooseCardBean);

    /**
     * Creates a dialogue and waits for the player to dismiss it.
     *
     * @param pauseMessageBean The data used to make the dialogue.
     */
    void render(PauseMessageBean pauseMessageBean);
}

