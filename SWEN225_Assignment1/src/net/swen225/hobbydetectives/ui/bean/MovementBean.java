package net.swen225.hobbydetectives.ui.bean;

/**
 * Stores state relating to what actions a player can do while moving.
 */
public class MovementBean {
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
