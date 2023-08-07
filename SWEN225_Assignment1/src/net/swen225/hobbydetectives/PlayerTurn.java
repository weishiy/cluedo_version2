package net.swen225.hobbydetectives;

import net.swen225.hobbydetectives.actions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlayerTurn {
    private final Game game;
    private final Player currentPlayer;
    private final List<Player> nextPlayers = new ArrayList<>();

    private int stepsLeft;

    public PlayerTurn(Game game, Player currentPlayer, List<Player> nextPlayers) {
        this.game = game;
        this.currentPlayer = currentPlayer;
        this.nextPlayers.addAll(nextPlayers);
    }

    public void run() {
        try {
            // move all these System.out.* to Prompt?

            // Print 100 empty lines to clear terminal history
            IntStream.range(0, 100).forEach(a -> System.out.println());
            System.out.println("Next player is: " + currentPlayer.characterCard().toString());
            System.out.println("Press Enter key to roll...");

            System.in.read();

            // Print 100 empty lines to clear terminal history
            IntStream.range(0, 100).forEach(a -> System.out.println());

            stepsLeft = rollTwoDices();
            while (stepsLeft > 0) {
                game.boardRenderer().render();

                System.out.println("You are: " + currentPlayer.characterCard().toString());
                System.out.println("Your cards: ");
                System.out.println("    " + currentPlayer.hand().stream().map(Card::toString).collect(Collectors.joining(", ")));

                System.out.println("You've " + stepsLeft + " steps left. ");

                var validInput = false;
                while (!validInput) {
                    System.out.println("Choose any of below to continue: ");
                    var allowedActions = getAllowedActions();
                    allowedActions.forEach(a -> System.out.println("    " + a.description()));
                    var s = new Scanner(System.in);
                    var userInput = s.next();

                    var action = allowedActions.stream().filter(a -> a.accept(userInput)).findFirst().orElse(null);
                    if (action != null) {
                        action.perform();
                        validInput = true;
                    } else {
                        System.out.println("Invalid input: " + userInput);
                    }
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private List<Action> getAllowedActions() {
        var allowedActions = new ArrayList<Action>();

        if (game.board().canEnter(currentPlayer.x(), currentPlayer.y() - 1)) {
            allowedActions.add(new MoveUpAction(this, currentPlayer) {
            });
        }
        if (game.board().canEnter(currentPlayer.x() - 1, currentPlayer.y())) {
            allowedActions.add(new MoveLeftAction(this, currentPlayer));
        }
        if (game.board().canEnter(currentPlayer.x(), currentPlayer.y() + 1)) {
            allowedActions.add(new MoveDownAction(this, currentPlayer));
        }
        if (game.board().canEnter(currentPlayer.x() + 1, currentPlayer.y())) {
            allowedActions.add(new MoveRightAction(this, currentPlayer));
        }
        if (game.board().getEstateAt(currentPlayer.x(), currentPlayer.y()) != null) {
            allowedActions.add(new GuessAction(game, currentPlayer, nextPlayers));
        }
        allowedActions.add(new AccuseAction(game, this, currentPlayer));
        allowedActions.add(new EndTurnAction(this));

        return allowedActions;
    }

    public void decreaseStepsLeft() {
        stepsLeft--;
    }

    public void endTurn() {
        stepsLeft = 0;
    }

    private int rollTwoDices() {
        // Math.ceil ensures we get values 1-6 rather than 0-5
        return (int) Math.ceil(Math.random() * 6) + (int) Math.ceil(Math.random() * 6);
    }

}
