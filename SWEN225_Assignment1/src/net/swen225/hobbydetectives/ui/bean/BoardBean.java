package net.swen225.hobbydetectives.ui.bean;

import net.swen225.hobbydetectives.board.Board;
import net.swen225.hobbydetectives.player.Player;

import java.util.Collections;
import java.util.Set;

/**
 * Represents information used to render the Board of the game.
 */
public class BoardBean  {
    /* Contains data relating to locations of estates and grey areas. */
    private Board board;
    /* Contains all players in the game, including their names and location data. */
    private Set<Player> players;
    /* The player whose turn it is */
    private Player currentPlayer;

    /* Whether we are in between turns. When changing players, the board is hidden, and input is limited. */
    private boolean visible;
    //The allowed movements of the player
    private boolean canMoveUp;
    private boolean canMoveDown;
    private boolean canMoveLeft;
    private boolean canMoveRight;
    /**
     * If the player is in an estate, they can guess
     */
    private boolean canGuess;
    /**
     * How many steps the current player can take.
     */
    private int stepsLeft;

    public void board(Board board) {
        this.board = board;
    }

    public Board board() {
        return board;
    }

    public void players(Set<Player> players) {
        this.players = Set.copyOf(players);
    }

    public Set<Player> players() {
        return Collections.unmodifiableSet(players);
    }

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

    public void canMoveUp(boolean canMoveUp) {
        this.canMoveUp = canMoveUp;
    }

    public boolean canMoveUp() {
        return canMoveUp;
    }

    public void canMoveDown(boolean canMoveDown) {
        this.canMoveDown = canMoveDown;
    }

    public boolean canMoveDown() {
        return canMoveDown;
    }

    public void canMoveLeft(boolean canMoveLeft) {
        this.canMoveLeft = canMoveLeft;
    }

    public boolean canMoveLeft() {
        return canMoveLeft;
    }

    public void canMoveRight(boolean canMoveRight) {
        this.canMoveRight = canMoveRight;
    }

    public boolean canMoveRight() {
        return canMoveRight;
    }

    public void canGuess(boolean canGuess) {
        this.canGuess = canGuess;
    }

    public boolean canGuess() {
        return canGuess;
    }

    public void stepsLeft(int stepsLeft) {
        this.stepsLeft = stepsLeft;
    }

    public int stepsLeft() {
        return stepsLeft;
    }

}
