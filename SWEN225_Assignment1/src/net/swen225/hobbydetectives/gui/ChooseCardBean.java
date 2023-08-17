package net.swen225.hobbydetectives.gui;

import net.swen225.hobbydetectives.card.model.Card;

import java.util.Collections;
import java.util.Set;

/**
 * Asks the current player to select a card.
 */
public class ChooseCardBean {
    /**
     * What to ask the current player.
     */
    private String promptText;

    /**
     * Cards the current player is asked to select.
     */
    private Set<Card> cards;

    public void promptText(String promptText) {
        this.promptText = promptText;
    }

    public String promptText() {
        return promptText;
    }

    public void cards(Set<Card> cards) {
        this.cards = Set.copyOf(cards);
    }

    public Set<Card> cards() {
        return Collections.unmodifiableSet(cards);
    }

}
