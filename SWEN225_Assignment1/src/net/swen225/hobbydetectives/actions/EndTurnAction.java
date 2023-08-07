package net.swen225.hobbydetectives.actions;

import net.swen225.hobbydetectives.PlayerTurn;

public class EndTurnAction implements Action {

    private final PlayerTurn turn;

    public EndTurnAction(PlayerTurn turn) {
        this.turn = turn;
    }

    @Override
    public boolean accept(String userInput) {
        return "E".equalsIgnoreCase(userInput);
    }

    @Override
    public String description() {
        return "E - End turn";
    }

    @Override
    public void perform() {
        turn.endTurn();
    }

}
