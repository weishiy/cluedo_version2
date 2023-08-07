package net.swen225.hobbydetectives.actions;

import net.swen225.hobbydetectives.Game;
import net.swen225.hobbydetectives.Player;
import net.swen225.hobbydetectives.RefutationController;

import java.util.ArrayList;
import java.util.List;

public class GuessAction implements Action {

    private final Game game;
    private final Player guesser;
    private final List<Player> nextPlayers = new ArrayList<>();

    public GuessAction(Game game, Player guesser, List<Player> nextPlayers) {
        this.game = game;
        this.guesser = guesser;
        this.nextPlayers.addAll(nextPlayers);
    }

    @Override
    public boolean accept(String userInput) {
        return "G".equalsIgnoreCase(userInput);
    }

    @Override
    public String description() {
        return "G - Guess";
    }

    @Override
    public void perform() {
        var controller = new RefutationController(game, guesser, nextPlayers);
    }
}
