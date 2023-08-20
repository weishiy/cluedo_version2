package net.swen225.hobbydetectives.player;

import net.swen225.hobbydetectives.Game;
import net.swen225.hobbydetectives.actions.*;
import net.swen225.hobbydetectives.actions.model.Action;
import net.swen225.hobbydetectives.board.Board;
import net.swen225.hobbydetectives.ui.bean.BoardBeanBuilder;
import net.swen225.hobbydetectives.ui.controller.Controller;
import net.swen225.hobbydetectives.ui.controller.MovementActions;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class PlayerTurn implements Controller {
    private final Game game;
    private final Board board;
    private final Player currentPlayer;
    private final List<Player> nextPlayers = new ArrayList<>();

    private int stepsLeft;
    private boolean hasGuessed = false;
    private Map<MovementActions, Action> allowedActions = new HashMap<>();

    private volatile CompletableFuture<MovementActions> future;

    public PlayerTurn(Game game, Player currentPlayer, List<Player> nextPlayers) {
        this.game = game;
        this.board = game.board();
        this.currentPlayer = currentPlayer;
        this.nextPlayers.addAll(nextPlayers);
        this.stepsLeft = rollTwoDices();
    }

    public void run() throws InterruptedException, ExecutionException {
        while(stepsLeft > 0) {
            calculateAllowedActions();
            var boardBean = new BoardBeanBuilder()
                    .withBoard(board)
                    .withPlayers(board.players())
                    .withCurrentPlayer(currentPlayer)
                    .withVisible(true)
                    .withStepsLeft(stepsLeft)
                    .withCanMoveUp(allowedActions.containsKey(MovementActions.UP))
                    .withCanMoveDown(allowedActions.containsKey(MovementActions.DOWN))
                    .withCanMoveLeft(allowedActions.containsKey(MovementActions.LEFT))
                    .withCanMoveRight(allowedActions.containsKey(MovementActions.RIGHT))
                    .withCanGuess(allowedActions.containsKey(MovementActions.GUESS))
                    .build();
            game.ui().render(boardBean);

            // wait for user input.
            game.ui().setController(this);
            future = new CompletableFuture<>();
            var action = future.get();
            future = null;
            game.ui().setController(null);

            allowedActions.get(action).perform();
        }
    }

    private void calculateAllowedActions() {
        allowedActions.clear();

        if (board.canEnter(currentPlayer.x(), currentPlayer.y() - 1)) {
            allowedActions.put(MovementActions.UP, new MoveUpAction(this, currentPlayer));
        }
        if (board.canEnter(currentPlayer.x() - 1, currentPlayer.y())) {
            allowedActions.put(MovementActions.LEFT, new MoveLeftAction(this, currentPlayer));
        }
        if (board.canEnter(currentPlayer.x(), currentPlayer.y() + 1)) {
            allowedActions.put(MovementActions.DOWN, new MoveDownAction(this, currentPlayer));
        }
        if (board.canEnter(currentPlayer.x() + 1, currentPlayer.y())) {
            allowedActions.put(MovementActions.RIGHT, new MoveRightAction(this, currentPlayer));
        }
        if (board.getEstateAt(currentPlayer.x(), currentPlayer.y()) != null && !hasGuessed) {
            allowedActions.put(MovementActions.GUESS, new GuessAction(this, game, currentPlayer, nextPlayers));
        }
        allowedActions.put(MovementActions.ACCUSE, new AccuseAction(game, this, currentPlayer));
        allowedActions.put(MovementActions.END_TURN, new EndTurnAction(this));
    }

    public void decreaseStepsLeft() {
        stepsLeft--;
    }

    public void hasGuessed(boolean hasGuessed) {
        this.hasGuessed = hasGuessed;
    }

    public void endTurn() {
        stepsLeft = 0;
    }

    public int stepsLeft() {
        return stepsLeft;
    }

    private int rollTwoDices() {
        // Math.ceil ensures we get values 1-6 rather than 0-5
        return (int) Math.ceil(Math.random() * 6) + (int) Math.ceil(Math.random() * 6);
    }

    @Override
    public void process(MovementActions action) {
        if(future != null) {
            future.complete(action);
        }
    }
}
