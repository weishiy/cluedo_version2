package net.swen225.hobbydetectives.ui.gui;

import net.swen225.hobbydetectives.ui.bean.BoardBean;
import net.swen225.hobbydetectives.ui.controller.Controller;
import net.swen225.hobbydetectives.ui.controller.MovementActions;

import javax.swing.*;

/**
 * Establishes the controls used during movement, including guessing and accusation.
 */
public class ControlPanel extends JPanel {

    //Buttons displayed in this panel.
    private final JButton upButton;
    private final JButton downButton;
    private final JButton leftButton;
    private final JButton rightButton;
    private final JButton guessButton;
    private final JButton accuseButton;

    /**
     * Creates a new ControlPanel.
     */
    public ControlPanel() {
        setBounds(0, 420, 450, 100);

        upButton = new JButton("Up");
        downButton = new JButton("Down");
        leftButton = new JButton("Left");
        rightButton = new JButton("Right");
        accuseButton = new JButton("Accuse");
        guessButton = new JButton("Guess");

        add(upButton);
        add(downButton);
        add(leftButton);
        add(rightButton);
        add(guessButton);
        add(accuseButton);
    }

    /**
     * Associates the given controller with input this object receives.
     *
     * @param controller The controller to pass input to.
     */
    public void addController(Controller controller) {
        upButton.addActionListener(e -> controller.process(MovementActions.UP));
        downButton.addActionListener(e -> controller.process(MovementActions.DOWN));
        leftButton.addActionListener(e -> controller.process(MovementActions.LEFT));
        rightButton.addActionListener(e -> controller.process(MovementActions.RIGHT));
        guessButton.addActionListener(e -> controller.process(MovementActions.GUESS));
        accuseButton.addActionListener(e -> controller.process(MovementActions.ACCUSE));
    }

    /**
     * Renders this object, using the given bean to enable certain buttons.
     *
     * @param bean The bean telling which buttons/functionality are enabled.
     */
    public void render(BoardBean bean) {
        //TODO: Make it so buttons render (appear) conditionally.
        upButton.setEnabled(bean.canMoveUp());
        downButton.setEnabled(bean.canMoveDown());
        leftButton.setEnabled(bean.canMoveLeft());
        rightButton.setEnabled(bean.canMoveRight());

        guessButton.setEnabled(bean.canGuess());

        repaint();
    }
}
