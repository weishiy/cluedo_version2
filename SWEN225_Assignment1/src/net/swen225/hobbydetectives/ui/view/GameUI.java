package net.swen225.hobbydetectives.ui.view;

import net.swen225.hobbydetectives.card.model.Card;
import net.swen225.hobbydetectives.ui.bean.BoardBean;
import net.swen225.hobbydetectives.ui.bean.ChooseCardBean;
import net.swen225.hobbydetectives.ui.bean.PauseMessageBean;

import java.util.concurrent.Future;

/**
 * Manages the view of the game.
 */
public interface GameUI extends PauseMessageUI, ChooseCardUI, BoardUI {

}

