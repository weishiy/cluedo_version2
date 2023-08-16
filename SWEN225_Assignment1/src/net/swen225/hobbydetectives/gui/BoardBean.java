package net.swen225.hobbydetectives.gui;

import net.swen225.hobbydetectives.board.Board;
import net.swen225.hobbydetectives.player.Player;

import java.util.Collections;
import java.util.Set;

/**
 * Represents information used to render the Board of the game.
 */
public class BoardBean {
    /* Contains data relating to locations of estates and grey areas. */
    private Board board;
    /* Contains all players in the game, including their names and location data. */
    private Set<Player> players;
    /* The player whose turn it is */
    private Player currentPlayer;

    /* Whether we are in between turns. When changing players, the board is hidden, and input is limited. */
    private boolean changingPlayers;

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

    public void changingPlayers(boolean changingPlayers) {
        this.changingPlayers = changingPlayers;
    }

    public boolean changingPlayers() {
        return changingPlayers;
    }


}
