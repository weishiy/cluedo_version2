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

    private final JLabel stepsLeftLabel = new JLabel("Error: not set");

    private final JLabel playerNameLabel = new JLabel("Error: not set");

    /**
     * The current player's hand
     */
    private final JLabel cardsLabel = new JLabel("Error: not set");

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

        add(new TextContainer());

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

        stepsLeftLabel.setText("Steps left: %d".formatted(bean.stepsLeft()));
        if (bean.currentPlayer() != null) {
            cardsLabel.setVisible(true);
            cardsLabel.setText("Your cards: %s".formatted(bean.currentPlayer().hand().toString()));

            playerNameLabel.setVisible(true);
            playerNameLabel.setText(bean.currentPlayer().characterCard().name());
        } else {
            cardsLabel.setVisible(false);
            playerNameLabel.setVisible(false);
        }

        repaint();
    }

    private static class MovementButton extends JButton {
        public static final int MINIMUM_LENGTH = 50;

        public MovementButton(String text) {
            super(text);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            setMinimumSize(new Dimension(MINIMUM_LENGTH, MINIMUM_LENGTH));

            //Have each button stretch to fill space.
            setPreferredSize(null);
        }
    }

    private static class OtherActionButton extends JButton {
        public OtherActionButton(String text) {
            super(text);

            setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            setMinimumSize(new Dimension(100, 50));

            //Have each button stretch to fill space.
            setPreferredSize(null);
            setAlignmentX(CENTER_ALIGNMENT);
        }
    }

    /**
     * Contains buttons for controlling movement
     */
    private class MovementContainer extends JPanel {
        public static final int MINIMUM_LENGTH = MovementButton.MINIMUM_LENGTH * 3;

        public MovementContainer() {
            super(new GridBagLayout());
            setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
            setMinimumSize(new Dimension(MINIMUM_LENGTH, MINIMUM_LENGTH));

            add(upButton, constraints(1, 0));
            add(downButton, constraints(1, 2));
            add(leftButton, constraints(0, 1));
            add(rightButton, constraints(2, 1));
        }

        private static GridBagConstraints constraints(int gridx, int gridy) {
            final float weight = 1.0f / 3.0f;

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.weightx = weight;
            constraints.weighty = weight;
            constraints.fill = GridBagConstraints.BOTH;

            constraints.gridx = gridx;
            constraints.gridy = gridy;

            return constraints;
        }
    }

    private class TextContainer extends JPanel {
        public TextContainer() {
            super();
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//            setAlignmentY(TOP_ALIGNMENT);

            add(playerNameLabel);
            add(Box.createVerticalStrut(10));
            add(stepsLeftLabel);
            add(Box.createVerticalStrut(10));
            add(cardsLabel);
            add(Box.createVerticalGlue());
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
