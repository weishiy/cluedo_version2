package net.swen225.hobbydetectives.actions;

import net.swen225.hobbydetectives.*;

import java.util.Arrays;
import java.util.Scanner;
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
    public boolean accept(String userInput) {
        return "A".equalsIgnoreCase(userInput);
    }

    @Override
    public String description() {
        return "A - Accuse";
    }

    @Override
    public void perform() {
        var s = new Scanner(System.in);
        CharacterCard characterCard = null;
        EstateCard estateCard = null;
        WeaponCard weaponCard = null;
        while (characterCard == null) {
            System.out.println("Choose a character: ");
            System.out.println("    " + Arrays.stream(CharacterCard.values()).map(CharacterCard::toString).collect(Collectors.joining(", ")));
            var character = s.nextLine();
            characterCard = CharacterCard.fromString(character);
            if (characterCard == null) {
                System.out.println("Invalid input: " + character);
            }
        }
        while (estateCard == null) {
            System.out.println("Choose a estate: ");
            System.out.println("    " + Arrays.stream(EstateCard.values()).map(EstateCard::toString).collect(Collectors.joining(", ")));
            var estate = s.nextLine();
            estateCard = EstateCard.fromString(estate);
            if (estateCard == null) {
                System.out.println("Invalid input: " + estate);
            }
        }
        while(weaponCard == null) {
            System.out.println("Choose a weapon: ");
            System.out.println("    " + Arrays.stream(WeaponCard.values()).map(WeaponCard::toString).collect(Collectors.joining(", ")));
            var weapon = s.nextLine();
            weaponCard = WeaponCard.fromString(weapon);
            if (weaponCard == null) {
                System.out.println("Invalid input: " + weapon);
            }
        }
        var accuse = new CardTriple(characterCard, estateCard, weaponCard);
        if (game.solution().equals(accuse)) {
            player.isWinner(true);
            turn.endTurn();
            game.endGame();
        } else {
            turn.endTurn();
            player.active(false);
        }
    }
}
