package net.swen225.hobbydetectives.ui.bean;

import net.swen225.hobbydetectives.board.Board;
import net.swen225.hobbydetectives.player.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class BoardBeanBuilder {

    /* Contains data relating to locations of estates and grey areas. */
    private Board board;
    /* Contains all players in the game, including their names and location data. */
    private final Set<Player> players = new HashSet<>();
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

    public BoardBeanBuilder withBoard(Board board) {
        this.board = board;
        return this;
    }

    public BoardBeanBuilder withPlayers(Collection<Player> players) {
        this.players.addAll(players);
        return this;
    }

    public BoardBeanBuilder withCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        return this;
    }

    public BoardBeanBuilder withVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public BoardBeanBuilder withCanMoveUp(boolean canMoveUp) {
        this.canMoveUp = canMoveUp;
        return this;
    }

    public BoardBeanBuilder withCanMoveDown(boolean canMoveDown) {
        this.canMoveDown = canMoveDown;
        return this;
    }

    public BoardBeanBuilder withCanMoveLeft(boolean canMoveLeft) {
        this.canMoveLeft = canMoveLeft;
        return this;
    }

    public BoardBeanBuilder withCanMoveRight(boolean canMoveRight) {
        this.canMoveRight = canMoveRight;
        return this;
    }

    public BoardBeanBuilder withCanGuess(boolean canGuess) {
        this.canGuess = canGuess;
        return this;
    }

    public BoardBeanBuilder withStepsLeft(int stepsLeft) {
        this.stepsLeft = stepsLeft;
        return this;
    }

    public BoardBeanBuilder withFalsyDefaults() {
        board = null;
        players.clear();
        currentPlayer = null;
        visible = false;
        canMoveUp = false;
        canMoveDown = false;
        canMoveLeft = false;
        canMoveRight = false;
        canGuess = false;
        stepsLeft = 0;
        return this;
    }

    public BoardBean build() {
        var boardBean = new BoardBean();
        boardBean.board(board);
        boardBean.players(players);
        boardBean.currentPlayer(currentPlayer);
        boardBean.canMoveUp(canMoveUp);
        boardBean.canMoveDown(canMoveDown);
        boardBean.canMoveLeft(canMoveLeft);
        boardBean.canMoveRight(canMoveRight);
        boardBean.canGuess(canGuess);
        boardBean.stepsLeft(stepsLeft);
        return boardBean;
    }
}
