package net.swen225.hobbydetectives.ui.gui;

import net.swen225.hobbydetectives.card.model.Card;
import net.swen225.hobbydetectives.ui.bean.BoardBean;
import net.swen225.hobbydetectives.ui.bean.ChooseCardBean;
import net.swen225.hobbydetectives.ui.bean.PauseMessageBean;
import net.swen225.hobbydetectives.ui.controller.Controller;
import net.swen225.hobbydetectives.ui.view.BoardUI;
import net.swen225.hobbydetectives.ui.view.GameUI;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import static javax.swing.JOptionPane.INFORMATION_MESSAGE;

/**
 * GUI class for displaying the game board of Hobby Detectives.
 */
public class GameFrame extends JFrame implements GameUI {

    private final ControlPanel controlPanel = new ControlPanel();
    private final BoardPanel boardPanel = new BoardPanel();

    /**
     * Constructor for the GameFrame class.
     */

    public GameFrame() {

        setTitle("Hobby Detectives Game");
        setMinimumSize(new Dimension(
                //Minimum width can contain the sum components' minimum width.
                boardPanel.getMinimumSize().width + controlPanel.getMinimumSize().width + 10,
                //Minimum height can contain max of components' minimum heights.
                Math.max(boardPanel.getMinimumSize().height, controlPanel.getMinimumSize().height)));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //This JFrame is just wrapper over content pane, so we pass content pane to BoxLayout.
        setLayout(new BoxLayout(getContentPane(), BoxLayout.LINE_AXIS));

        boardPanel.setPreferredSize(new Dimension(5000, 5000));
//        boardPanel.setAlignmentY(TOP_ALIGNMENT);
        add(boardPanel);

        add(Box.createHorizontalStrut(10));

        add(controlPanel);

        //Sets size to fill the window.
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setVisible(true);
    }


    @Override
    public void setController(Controller controller) {
        controlPanel.setController(controller);
    }

    @Override
    public void render(BoardBean boardBean) {
        controlPanel.render(boardBean);
        boardPanel.render(boardBean);
    }

    @Override
    public Card render(ChooseCardBean chooseCardBean) {
        var cards = chooseCardBean.cards().stream().collect(Collectors.toMap(Card::toString, e -> e));

        var response = (String) JOptionPane.showInputDialog(
                this,
                chooseCardBean.promptText(),
                "Message",
                JOptionPane.QUESTION_MESSAGE,
                null,
                cards.keySet().toArray(new String[0]),
                null);

        return cards.get(response);
    }

    @Override
    public void render(PauseMessageBean pauseMessageBean) {
        JOptionPane.showMessageDialog(
                this,
                pauseMessageBean.messageText(),
                "Message",
                INFORMATION_MESSAGE);
    }
}
