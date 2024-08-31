package net.swen225.hobbydetectives.ui.controller;

/**
 * The possible actions a player can make on the board.
 */
public enum MovementActions {
    /**
     * Directions the player wants to move in.
     */
    UP, DOWN, LEFT, RIGHT,
    /**
     * The player is in an estate, and wants to make a guess.
     */
    GUESS,
    /**
     * The player wants to make an accusation.
     */
    ACCUSE,
    /**
     * The player wants to end the current turn.
     */
    END_TURN
}
