package net.swen225.hobbydetectives.gui;

import net.swen225.hobbydetectives.card.model.Card;
import net.swen225.hobbydetectives.player.Player;

import java.util.Collections;
import java.util.Set;

/**
 * State used to render guess dialogues.
 */
public class GuessBean {
    /**
     * The player to display to.
     * <p>
     * Either the guesser or the refuter.
     */
    private Player currentPlayer;

    /**
     * Whether to display this dialogue.
     */
    private boolean visible;

    /**
     * The cards to display.
     * <p>
     * In a <code>GuessState</code> starting with `CHOOSE_`, these indicate the cards we want the user to select from.
     * Otherwise, these are just cards we want displayed for some reason.
     */
    private Set<Card> cards;

    public void currentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player currentPlayer() {
        return currentPlayer;
    }

    public void visible(boolean visible) {
        this.visible = visible;
    }

    public boolean visible() {
        return visible;
    }

    public void cards(Set<Card> cards) {
        this.cards = Set.copyOf(cards);
    }

    public Set<Card> cards() {
        return Collections.unmodifiableSet(cards);
    }

    /**
     * The current state of the guessing process.
     */
    public enum GuessState {
        /**
         * Game tells guesser which estate they are in, and that it will be part of their refutation.
         */
        CONFIRM_ESTATE,
        /**
         * Game prompts guesser to choose character (killer).
         */
        CHOOSE_CHARACTER,
        /**
         * Game prompts guesser to choose weapon (murder weapon).
         */
        CHOOSE_WEAPON,
        /**
         * Game prompts refuter to choose card to refute with.
         */
        CHOOSE_REFUTATION,
        /**
         * Game tells guesser that their guess was refuted.
         */
        TELL_REFUTATION,
        /**
         * Game tells guesser that their guess was not refuted.
         */
        TELL_NOT_REFUTATION
    }
}
