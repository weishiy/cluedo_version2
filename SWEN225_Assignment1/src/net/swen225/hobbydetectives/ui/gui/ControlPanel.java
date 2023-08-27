package net.swen225.hobbydetectives.ui.gui;

import net.swen225.hobbydetectives.ui.bean.BoardBean;
import net.swen225.hobbydetectives.ui.controller.Controller;
import net.swen225.hobbydetectives.ui.controller.MovementActions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Establishes the controls used during movement, including guessing and accusation.
 */
public class ControlPanel extends JPanel {
    //Buttons displayed in this panel.
    //Movement buttons
    private final JButton upButton = new MovementButton("Up");
    private final JButton downButton = new MovementButton("Down");
    private final JButton leftButton = new MovementButton("Left");
    private final JButton rightButton = new MovementButton("Right");
    //Other buttons
    private final JButton guessButton = new OtherActionButton("Guess");
    private final JButton accuseButton = new OtherActionButton("Accuse");
    private final JButton endTurnButton = new OtherActionButton("End Turn");
    //Text labels
    private final JLabel stepsLeftLabel = new TextLabel();
    private final JLabel playerNameLabel = new TextLabel();
    /**
     * The current player's hand
     */
    private final JLabel cardsLabel = new TextLabel();
    /**
     * Controller to pass-back input to.
     */
    private Controller controller = null;

    /**
     * Creates a new ControlPanel.
     */
    public ControlPanel() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        initialiseListeners();

        add(new MovementContainer()); //Contains direction buttons
        add(new TextContainer()); //Contains text labels
        add(new OtherActionsContainer()); //Miscellaneous buttons
    }

    private void initialiseListeners() {
        class Listener implements ActionListener {
            private final MovementActions action;

            public Listener(MovementActions action) {
                this.action = action;
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (controller != null) {
                    controller.process(action);
                }
            }
        }

        upButton.addActionListener(new Listener(MovementActions.UP));
        downButton.addActionListener(new Listener(MovementActions.DOWN));
        leftButton.addActionListener(new Listener(MovementActions.LEFT));
        rightButton.addActionListener(new Listener(MovementActions.RIGHT));

        accuseButton.addActionListener(new Listener(MovementActions.ACCUSE));
        guessButton.addActionListener(new Listener(MovementActions.GUESS));
        endTurnButton.addActionListener(new Listener(MovementActions.END_TURN));
    }

    /**
     * Associates the given controller with input this object receives.
     *
     * @param controller The controller to pass input to.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Renders this object, using the given bean to enable certain buttons.
     *
     * @param bean The bean telling which buttons/functionality are enabled.
     */
    public void render(BoardBean bean) {
        upButton.setEnabled(bean.canMoveUp());
        downButton.setEnabled(bean.canMoveDown());
        leftButton.setEnabled(bean.canMoveLeft());
        rightButton.setEnabled(bean.canMoveRight());

        guessButton.setEnabled(bean.canGuess());

        //Current player is null when changing players.
        if (bean.currentPlayer() != null) {
            cardsLabel.setVisible(true);
            cardsLabel.setText("Your cards: %s".formatted(bean.currentPlayer().hand().toString()));

            playerNameLabel.setVisible(true);
            playerNameLabel.setText(bean.currentPlayer().characterCard().name());

            stepsLeftLabel.setVisible(true);
            stepsLeftLabel.setText("Steps left: %d".formatted(bean.stepsLeft()));

            //Buttons shouldn't be enabled when player isn't set.
            endTurnButton.setEnabled(true);
            accuseButton.setEnabled(true);
        } else {
            cardsLabel.setVisible(false);
            playerNameLabel.setVisible(false);
            stepsLeftLabel.setVisible(false);

            //Buttons shouldn't be enabled when player isn't set.
            endTurnButton.setEnabled(false);
            accuseButton.setEnabled(false);
        }

        repaint();
    }

    private static class TextLabel extends JLabel {
        public TextLabel() {
            super();
            setAlignmentX(CENTER_ALIGNMENT);
        }
    }

    private static class MovementButton extends JButton {
        public static final int MINIMUM_LENGTH = 75;

        public MovementButton(String text) {
            super(text);

            setMinimumSize(new Dimension(MINIMUM_LENGTH, MINIMUM_LENGTH));
        }
    }

    private static class OtherActionButton extends JButton {
        public OtherActionButton(String text) {
            super(text);

            setMinimumSize(new Dimension(100, 50));

            setAlignmentX(CENTER_ALIGNMENT);
        }

        @Override
        public Dimension getMaximumSize() {
            //The button tries to fill all horizontal space.
            Dimension size = super.getMaximumSize();
            size.width = Integer.MAX_VALUE;
            return size;
        }
    }

    /**
     * Contains buttons for controlling movement
     */
    private class MovementContainer extends JPanel {
        public static final int MINIMUM_LENGTH = MovementButton.MINIMUM_LENGTH * 3;

        public MovementContainer() {
            super(null);

            setMinimumSize(new Dimension(MINIMUM_LENGTH, MINIMUM_LENGTH));

            InnerPanel innerPanel = new InnerPanel();
            add(innerPanel);

            //Whenever this container is resized, make inner panel square.
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    innerPanel.makeSquare();
                }
            });

            //Initialises size so the above listener fires. Else, inner panel isn't initially sized.
            setSize(new Dimension(MINIMUM_LENGTH, MINIMUM_LENGTH));
        }


        /**
         * Square-shaped panel that stretches to fill its container.
         */
        private class InnerPanel extends JPanel {
            public InnerPanel() {
                super(new GridBagLayout());
                //Arrange buttons in grid according to arrow-keys location.
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

            public void makeSquare() {
                Container container = getParent();

                //Largest length that contains both width and height
                int length = Math.min(container.getWidth(), container.getHeight());

                int centerX = container.getWidth() / 2;
                int centerY = container.getHeight() / 2;

                int left = centerX - length / 2;
                int top = centerY - length / 2;

                setBounds(left, top, length, length);
            }
        }
    }

    private class TextContainer extends JPanel {
        public TextContainer() {
            super();
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            add(playerNameLabel);
            add(Box.createVerticalStrut(10));
            add(stepsLeftLabel);
            add(Box.createVerticalStrut(10));
            add(cardsLabel);
        }
    }

    private class OtherActionsContainer extends JPanel {
        public OtherActionsContainer() {
            super();
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

            setMinimumSize(new Dimension(15, 60));

            add(guessButton);
            add(accuseButton);
            add(endTurnButton);
        }
    }
}
