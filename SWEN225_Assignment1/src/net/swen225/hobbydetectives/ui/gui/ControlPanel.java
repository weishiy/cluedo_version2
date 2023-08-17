package net.swen225.hobbydetectives.ui.gui;

import net.swen225.hobbydetectives.ui.bean.MovementBean;
import net.swen225.hobbydetectives.ui.view.Controller;
import net.swen225.hobbydetectives.ui.view.MovementActions;

import javax.swing.*;

public class ControlPanel extends JPanel {

    //Buttons displayed in this panel.
    private final JButton upButton;
    private final JButton downButton;
    private final JButton leftButton;
    private final JButton rightButton;
    private final JButton guessButton;
    private final JButton accuseButton;

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

    public void addController(Controller controller) {
        upButton.addActionListener(e -> controller.process(MovementActions.UP));
        downButton.addActionListener(e -> controller.process(MovementActions.DOWN));
        leftButton.addActionListener(e -> controller.process(MovementActions.LEFT));
        rightButton.addActionListener(e -> controller.process(MovementActions.RIGHT));
        guessButton.addActionListener(e -> controller.process(MovementActions.GUESS));
        accuseButton.addActionListener(e -> controller.process(MovementActions.ACCUSE));
    }

    public void render(MovementBean bean) {
        //TODO: Make it so buttons render (appear) conditionally.
        upButton.setEnabled(bean.canMoveUp());
        downButton.setEnabled(bean.canMoveDown());
        leftButton.setEnabled(bean.canMoveLeft());
        rightButton.setEnabled(bean.canMoveRight());

        guessButton.setEnabled(bean.canGuess());
    }
}
