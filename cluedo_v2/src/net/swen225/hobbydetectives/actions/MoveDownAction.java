package net.swen225.hobbydetectives.actions;

import net.swen225.hobbydetectives.actions.model.Action;
import net.swen225.hobbydetectives.player.Player;
import net.swen225.hobbydetectives.player.PlayerTurn;

public class MoveDownAction implements Action {

    private final PlayerTurn turn;
    private final Player player;

    public MoveDownAction(PlayerTurn turn, Player player) {
        this.turn = turn;
        this.player = player;
    }

    @Override
    public void perform() {
        player.y(player.y() + 1);
        turn.decreaseStepsLeft();
    }

}
