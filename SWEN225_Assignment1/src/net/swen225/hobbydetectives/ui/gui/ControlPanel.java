package net.swen225.hobbydetectives.ui.gui;

import net.swen225.hobbydetectives.ui.bean.BoardBean;
import net.swen225.hobbydetectives.ui.controller.Controller;
import net.swen225.hobbydetectives.ui.controller.MovementActions;

import javax.swing.*;
import java.awt.*;

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

    private final JButton endTurnButton;

    /**
     * Creates a new ControlPanel.
     */
    public ControlPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));

        upButton = new MovementButton("Up");
        downButton = new MovementButton("Down");
        leftButton = new MovementButton("Left");
        rightButton = new MovementButton("Right");


        accuseButton = new OtherActionButton("Accuse");
        guessButton = new OtherActionButton("Guess");
        endTurnButton = new OtherActionButton("End Turn");

        add(new MovementContainer());

        //Add filler between elements
        Dimension minimum = new Dimension(5, getMinimumSize().height);
        Dimension preferred = new Dimension(5, getPreferredSize().height);
        Dimension maximum = new Dimension(Integer.MAX_VALUE, getMaximumSize().height);
        add(new Box.Filler(minimum, preferred, maximum));

        add(new OtherActionsContainer());
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

    private static class MovementButton extends JButton {
        public static final int MINIMUM_LENGTH = 20;

        public MovementButton(String text) {
            super(text);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            setMinimumSize(new Dimension(MINIMUM_LENGTH, MINIMUM_LENGTH));
        }
    }

    private static class OtherActionButton extends JButton {
        public OtherActionButton(String text) {
            super(text);

            setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

            setAlignmentX(CENTER_ALIGNMENT);
        }
    }

    /**
     * Contains buttons for controlling movement
     */
    private class MovementContainer extends JPanel {
        public static final int MINIMUM_LENGTH = MovementButton.MINIMUM_LENGTH * 2;

        public MovementContainer() {
            super(new BorderLayout(0, 0));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            setMinimumSize(new Dimension(MINIMUM_LENGTH, MINIMUM_LENGTH));

            add(upButton, BorderLayout.NORTH);
            add(downButton, BorderLayout.SOUTH);
            add(leftButton, BorderLayout.WEST);
            add(rightButton, BorderLayout.EAST);
        }
    }

    private class OtherActionsContainer extends JPanel {
        public OtherActionsContainer() {
            //Can't refer to `this` in super, thus use setLayout.
            super();
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

            add(guessButton);
            add(accuseButton);
            add(endTurnButton);
        }
    }
}
