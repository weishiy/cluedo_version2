package net.swen225.hobbydetectives.actions;

import net.swen225.hobbydetectives.Game;
import net.swen225.hobbydetectives.actions.model.Action;
import net.swen225.hobbydetectives.board.Board;
import net.swen225.hobbydetectives.card.CardTriple;
import net.swen225.hobbydetectives.card.CharacterCard;
import net.swen225.hobbydetectives.card.EstateCard;
import net.swen225.hobbydetectives.card.model.Card;
import net.swen225.hobbydetectives.card.WeaponCard;
import net.swen225.hobbydetectives.player.Player;
import net.swen225.hobbydetectives.player.PlayerTurn;
import net.swen225.hobbydetectives.ui.bean.BoardBeanBuilder;
import net.swen225.hobbydetectives.ui.bean.ChooseCardBean;
import net.swen225.hobbydetectives.ui.bean.PauseMessageBean;
import net.swen225.hobbydetectives.ui.view.GameUI;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class GuessAction implements Action {
  private final PlayerTurn playerTurn;
  private final Game game;
  private final Board board;
  private final GameUI ui;

  private final Player guesser;
  private final List<Player> nextPlayers = new ArrayList<>();

  /**
   * Creates this refutation, and immediately starts the refutation stage.
   * <p>
   * Assumes the current player is the guesser, i.e. don't have to switch manually.
   * <p>
   * Can call <code>unrefutedGuess()</code> to get the calculated guess.
   *
   * @param game The current game.
   * @param guesser The player who is making the guess.
   *
   */
  public GuessAction(PlayerTurn playerTurn, Game game, Player guesser, List<Player> nextPlayers) {
    this.playerTurn = playerTurn;
    this.game = game;
    this.board = game.board();
    this.ui = game.ui();

    this.guesser = guesser;
    this.nextPlayers.addAll(nextPlayers);
  }

  /**
   * Starts the refutation process, if successful changes <code>unrefutedGuess</code>
   */
  @Override
  public void perform() {
    try {
      playerTurn.hasGuessed(true);

      CardTriple guess = null;
      guess = askGuess();

      boolean isSolution = tryRefuteAll(guess);
      if (isSolution) {
        //Optional<CardTriple> unrefutedGuess = Optional.of(guess); Unsure if we still need this?
        giveUnrefuted(guess);
      }
    } catch (ExecutionException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Asks this player for their guess.
   *
   * @return the guess of the player.
   */
  private CardTriple askGuess() throws ExecutionException, InterruptedException {
    game.ui().render(new BoardBeanBuilder().withFalsyDefaults().withBoard(board).withPlayers(board.players()).withCurrentPlayer(guesser).withStepsLeft(playerTurn.stepsLeft()).build());

    var chooseWeaponDialogue = new ChooseCardBean();
    chooseWeaponDialogue.promptText("Guess the murder weapon:");
    chooseWeaponDialogue.cards(Set.copyOf(Arrays.asList(WeaponCard.values())));
    var chooseWeaponFuture = ui.render(chooseWeaponDialogue);
    WeaponCard weapon = (WeaponCard) chooseWeaponFuture.get();

    var chooseCharacterDialogue = new ChooseCardBean();
    chooseCharacterDialogue.promptText("Guess the killer:");
    chooseCharacterDialogue.cards(Set.copyOf(Arrays.asList(CharacterCard.values())));
    var chooseCharacterFuture = ui.render(chooseCharacterDialogue);
    CharacterCard characterCard = (CharacterCard) chooseCharacterFuture.get();

    EstateCard estate = board.getEstateAt(guesser.x(), guesser.y()).estateCard();
    assert estate != null : "The player making the guess is not in an estate.";

    Player guessedCharacter = game.findPlayer(characterCard);
    assert guessedCharacter != null : "The character wasn't associated to a card.";
    // move the guessed character to guesser's location
    guessedCharacter.x(guesser.x());
    guessedCharacter.y(guesser.y());

    return new CardTriple(characterCard, estate, weapon);
  }

  /**
   * Tests each player for refutes, if they can switch to them asking which refute they would like
   * to choose, and then switches to the guesser and delivers the refute.
   *
   * @param guess the cards compared to refute with.
   * @return <code>true</code> if the guess wasn't refuted, <code>false</code> otherwise.
   */
  private boolean tryRefuteAll(CardTriple guess) throws ExecutionException, InterruptedException {
    Iterator<Player> iter = nextPlayers.iterator();
    for (Player refuter = iter.next(); iter.hasNext(); refuter = iter.next()) {
      Set<Card> refutes = getRefutes(guess, refuter);
      Card refuteWith;

      if(refutes.isEmpty()){
        //the player has no cards to refute with
        continue;
      } else if (refutes.size() == 1) { // The player must refute with one card, doesn't ask for permission
        refuteWith = refutes.iterator().next();
        tellRefute(refuter, refuteWith);
      } else { // The player chooses what to refute with
        refuteWith = askRefute(refuter, refutes);
      }

      // The guess was refuted
      if (refuteWith != null) {
        giveRefute(refuteWith);
        return false;
      }
    }

    return true; // No one refutes: this guess is a possible solution.
  }

  /**
   * Switches to <code>refuter</code>, and asks them to select a refute.
   */
  private Card askRefute(Player refuter, Set<Card> refutes) throws ExecutionException, InterruptedException {
    changingPlayer("Current refuter is: " + refuter.characterCard().toString());

    game.ui().render(new BoardBeanBuilder().withFalsyDefaults().withBoard(board).withPlayers(board.players()).withCurrentPlayer(refuter).build());
    var chooseCardDialogue = new ChooseCardBean();
    chooseCardDialogue.promptText("Select card to refute with:");
    chooseCardDialogue.cards(refutes);
    var chooseCardFuture = ui.render(chooseCardDialogue);
    return chooseCardFuture.get();
  }

  /**
   * Switches to <code>refuter</code>, and tells them the card they must refute with.
   */
  private void tellRefute(Player refuter, Card refuteWith) throws ExecutionException, InterruptedException {
    changingPlayer("Current refuter is: " + refuter.characterCard().toString());

    game.ui().render(new BoardBeanBuilder().withFalsyDefaults().withBoard(board).withPlayers(board.players()).withCurrentPlayer(refuter).build());
    promptAndWait("You must refute with: " + refuteWith.toString());
  }

  /**
   * Returns the cards player has that can refute guess.
   *
   * @param guess what to search for.
   * @param player who to search.
   * @return cards that were found, the intersection.
   */
  private Set<Card> getRefutes(CardTriple guess, Player player) {
    // Simple filter and collector
      return player.hand().stream().filter(inHand -> guess.toSet().contains(inHand))
        .collect(Collectors.toSet());
  }

  /**
   * Tells the original guesser they were refuted by this card.
   *
   * @param card what to refute with.
   */
  private void giveRefute(Card card) throws ExecutionException, InterruptedException {
    changingPlayer("Current player is: " + guesser.characterCard().toString());

    game.ui().render(new BoardBeanBuilder().withFalsyDefaults().withBoard(board).withPlayers(board.players()).withCurrentPlayer(guesser).withStepsLeft(playerTurn.stepsLeft()).build());
    promptAndWait("Your guess was refuted: " + card.toString());
  }



  /**
   * Tells the guesser their guess wasn't refuted.
   *
   * @param cards their guess.
   */
  private void giveUnrefuted(CardTriple cards) throws ExecutionException, InterruptedException {
    game.ui().render(new BoardBeanBuilder().withFalsyDefaults().withBoard(board).withPlayers(board.players()).withCurrentPlayer(guesser).withStepsLeft(playerTurn.stepsLeft()).build());
    promptAndWait("Your guess wasn't refuted: " + cards);
  }

  private void changingPlayer(String message) throws InterruptedException, ExecutionException {
    game.ui().render(new BoardBeanBuilder().withFalsyDefaults().withBoard(board).withPlayers(board.players()).build());
    promptAndWait(message);
  }

  private void promptAndWait(String message) throws InterruptedException, ExecutionException {
    var changingPlayerMessage = new PauseMessageBean();
    changingPlayerMessage.messageText(message);
    var changingPlayerFuture = ui.render(changingPlayerMessage);
    changingPlayerFuture.get();
  }


}
