package net.swen225.hobbydetectives.ui.view;

/**
 * Manages input sent from <code>GameUI</code>.
 */
public interface Controller {
    /**
     * Asks this controller to process this action.
     *
     * @param action The action to process.
     */
    void process(MovementActions action);

}
