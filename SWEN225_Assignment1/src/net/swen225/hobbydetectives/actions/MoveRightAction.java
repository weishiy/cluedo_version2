package net.swen225.hobbydetectives.actions;

import net.swen225.hobbydetectives.actions.model.Action;
import net.swen225.hobbydetectives.player.Player;
import net.swen225.hobbydetectives.player.PlayerTurn;

public class MoveRightAction implements Action {

    private final PlayerTurn turn;
    private final Player player;

    public MoveRightAction(PlayerTurn turn, Player player) {
        this.turn = turn;
        this.player = player;
    }

    @Override
    public boolean accept(String userInput) {
        return "R".equalsIgnoreCase(userInput);
    }

    @Override
    public String description() {
        return "R - Move right one tile";
    }

    @Override
    public void perform() {
        player.x(player.x() + 1);
        turn.decreaseStepsLeft();
    }

}
