package net.swen225.hobbydetectives.actions;

import net.swen225.hobbydetectives.actions.model.Action;
import net.swen225.hobbydetectives.player.Player;
import net.swen225.hobbydetectives.player.PlayerTurn;

public class MoveUpAction implements Action {

    private final PlayerTurn turn;
    private final Player player;

    public MoveUpAction(PlayerTurn turn, Player player) {
        this.turn = turn;
        this.player = player;
    }

    @Override
    public boolean accept(String userInput) {
        return "U".equalsIgnoreCase(userInput);
    }

    @Override
    public String description() {
        return "U - Move up one tile";
    }

    @Override
    public void perform() {
        player.y(player.y() - 1);
        turn.decreaseStepsLeft();
    }

}
