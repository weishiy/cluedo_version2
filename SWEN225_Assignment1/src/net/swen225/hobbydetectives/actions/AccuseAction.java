package net.swen225.hobbydetectives.actions;

import net.swen225.hobbydetectives.*;
import net.swen225.hobbydetectives.actions.model.Action;
import net.swen225.hobbydetectives.card.CardTriple;
import net.swen225.hobbydetectives.card.CharacterCard;
import net.swen225.hobbydetectives.card.EstateCard;
import net.swen225.hobbydetectives.card.WeaponCard;
import net.swen225.hobbydetectives.player.Player;
import net.swen225.hobbydetectives.player.PlayerTurn;
import net.swen225.hobbydetectives.ui.bean.BoardBeanBuilder;
import net.swen225.hobbydetectives.ui.bean.ChooseCardBean;
import net.swen225.hobbydetectives.ui.bean.PauseMessageBean;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class AccuseAction implements Action {
    private final Game game;
    private final PlayerTurn turn;
    private final Player player;

    public AccuseAction(Game game, PlayerTurn turn, Player player) {
        this.game = game;
        this.turn = turn;
        this.player = player;
    }

    @Override
    public void perform() {
        try {
            game.ui().render(new BoardBeanBuilder().withFalsyDefaults().withBoard(game.board()).withPlayers(game.board().players()).withCurrentPlayer(player).withStepsLeft(turn.stepsLeft()).build());

            var chooseCharacterDialogue = new ChooseCardBean();
            chooseCharacterDialogue.promptText("Accuse the killer:");
            chooseCharacterDialogue.cards(Set.copyOf(Arrays.asList(CharacterCard.values())));
            var chooseCharacterFuture = game.ui().render(chooseCharacterDialogue);
            var characterCard = (CharacterCard) chooseCharacterFuture.get();

            var chooseEstateDialogue = new ChooseCardBean();
            chooseEstateDialogue.promptText("Accuse the killer:");
            chooseEstateDialogue.cards(Set.copyOf(Arrays.asList(EstateCard.values())));
            var chooseEstateFuture = game.ui().render(chooseEstateDialogue);
            var estateCard = (EstateCard) chooseEstateFuture.get();


            var chooseWeaponDialogue = new ChooseCardBean();
            chooseWeaponDialogue.promptText("Accuse the murder weapon:");
            chooseWeaponDialogue.cards(Set.copyOf(Arrays.asList(WeaponCard.values())));
            var chooseWeaponFuture = game.ui().render(chooseWeaponDialogue);
            var weaponCard = (WeaponCard) chooseWeaponFuture.get();

            var accuse = new CardTriple(characterCard, estateCard, weaponCard);
            if (game.solution().equals(accuse)) {
                player.isWinner(true);
                turn.endTurn();
            } else {
                var accuseFailedMessage = new PauseMessageBean();
                accuseFailedMessage.messageText("Accuse failed. You're removed from the Game. ");
                var accuseFailedFuture = game.ui().render(accuseFailedMessage);
                accuseFailedFuture.get();

                player.active(false);
                turn.endTurn();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
