package net.swen225.hobbydetectives.ui.view;

import net.swen225.hobbydetectives.card.model.Card;
import net.swen225.hobbydetectives.ui.bean.ChooseCardBean;

import java.util.concurrent.Future;

public interface ChooseCardUI {
    /**
     * Creates a dialogue for selecting a card, and returns a future containing the selected card.
     *
     * @param chooseCardBean The data used to make the dialogue. Contains cards to select from.
     * @param <T>            The type of the selected card.
     * @return The future that receives the selected card.
     */
    <T extends Card> Future<T> render(ChooseCardBean chooseCardBean);
}
