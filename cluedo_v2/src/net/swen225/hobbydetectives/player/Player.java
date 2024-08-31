package net.swen225.hobbydetectives.player;

import net.swen225.hobbydetectives.card.CharacterCard;
import net.swen225.hobbydetectives.card.model.Card;

import java.util.*;

public class Player {

    /**
     * The character this player is playing
     */
    private final CharacterCard characterCard;

    /**
     * Current location of the player
     */
    private int x, y;

    /**
     * Player's hand: what cards they possess.
     */
    private final Set<Card> hand = new HashSet<>();

    private boolean active;
    private boolean isWinner;

    /***
     * Creates a new Player object
     *
     * @param x Int - X coordinate of the player's starting location
     * @param y Int - Y coordinate of the player's starting location
     * @param hand Variable - Card - A variable amount of refutation cards that the player may
     *        have
     */
    public Player(CharacterCard characterCard, int x, int y, Card... hand) {
        this.characterCard = characterCard;
        this.x = x;
        this.y = y;
        this.active = true;
        Collections.addAll(this.hand, hand);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void x(int x) {
        this.x = x;
    }

    public void y(int y) {
        this.y = y;
    }

    public CharacterCard characterCard() {
        return characterCard;
    }

    public boolean active() {
        return active;
    }

    public void active(boolean active) {
        this.active = active;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void isWinner(boolean isWinner) {
        this.isWinner = isWinner;
    }

    /**
     * Returns the player's hand
     *
     * @return the hand of the player.
     */
    public Set<Card> hand() {
        return Collections.unmodifiableSet(hand);
    }

    public void addCard(Card card) {
        hand.add(card);
    }

}
