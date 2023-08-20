package net.swen225.hobbydetectives.actions;

import net.swen225.hobbydetectives.actions.model.Action;
import net.swen225.hobbydetectives.player.PlayerTurn;

public class EndTurnAction implements Action {

    private final PlayerTurn turn;

    public EndTurnAction(PlayerTurn turn) {
        this.turn = turn;
    }

    @Override
    public void perform() {
        turn.endTurn();
    }

}
