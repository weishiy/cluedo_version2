package net.swen225.hobbydetectives.actions;

import net.swen225.hobbydetectives.Player;
import net.swen225.hobbydetectives.PlayerTurn;
import net.swen225.hobbydetectives.actions.Action;

public class MoveDownAction implements Action {

    private final PlayerTurn turn;
    private final Player player;

    public MoveDownAction(PlayerTurn turn, Player player) {
        this.turn = turn;
        this.player = player;
    }


    @Override
    public boolean accept(String userInput) {
        return "D".equals(userInput);
    }

    @Override
    public String description() {
        return "D - Move down one tile";
    }

    @Override
    public void perform() {
        player.y(player.y() + 1);
        turn.decreaseStepsLeft();
    }

}
