package net.swen225.hobbydetectives;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Controls game during refutation stage.
 * 
 * Asks the given player to make a guess, then turns to each other player in turn to find whether
 * they can refute that guess. If no one can refute the guess, it's stored in `unrefutedGuess`, else
 * `unrefutedGuess` is left empty.
 * 
 * This class was developed without knowing the final stage of the game. Specifically, it assumes
 * certain public methods of <code>Game</code>, <code>Board</code>, <code>Prompt</code> and
 * <code>Player</code>, and also assumes cards are uniquely identifiable by reference, and that
 * there are Card subclasses for estates, weapons and characters.
 */
public class RefutationController {
  private final Game game;
  private final Board board;
  private final Prompt prompt;

  private final Player guesser;
  private final List<Player> nextPlayers = new ArrayList<>();

  private Optional<CardTriple> unrefutedGuess = Optional.empty();

  /**
   * Creates this refutation, and immediately starts the refutation stage.
   * 
   * Assumes the current player is the guesser, i.e. don't have to switch manually.
   * 
   * Can call <code>unrefutedGuess()</code> to get the calculated guess.
   * 
   * @param game The current game.
   * @param guesser The player who is making the guess.
   * 
   */
  public RefutationController(Game game, Player guesser, List<Player> nextPlayers) {
    this.game = game;
    this.board = game.board();
    this.prompt = game.prompt();

    this.guesser = guesser;
    this.nextPlayers.addAll(nextPlayers);

    start();
  }

  /**
   * Returns unrefuted guess made during the refutation.
   * 
   * @return the guess.
   */
  public Optional<CardTriple> unrefutedGuess() {
    return unrefutedGuess;
  }

  /**
   * Starts the refutation process, if successful changes <code>unrefutedGuess</code>
   */
  private void start() {
    CardTriple guess = askGuess();
    boolean isSolution = tryRefuteAll(guess);
    if (isSolution) {
      this.unrefutedGuess = Optional.of(guess);
      giveUnrefuted(guess);
    }
  }

  /**
   * Asks this player for their guess.
   * 
   * @return the guess of the player.
   */
  private CardTriple askGuess() {

    WeaponCard weapon = prompt.promptCard("Guess the murder weapon:", WeaponCard.values());
    CharacterCard characterCard = prompt.promptCard("Guess the killer:", CharacterCard.values());

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
   * Tests each player for refutes, if they can switches to them asking which refute they would like
   * to choose, and then switches to the guesser and delivers the refute.
   * 
   * @param guess the cards compared to refute with.
   * @return <code>true</code> if the guess wasn't refuted, <code>false</code> otherwise.
   */
  private boolean tryRefuteAll(CardTriple guess) {
    Iterator<Player> iter = nextPlayers.iterator();
    for (Player refuter = iter.next(); iter.hasNext(); refuter = iter.next()) {
      Set<Card> refutes = getRefutes(guess, refuter);
      Card refuteWith = null;

      if (refutes.size() == 1) { // The player must refute with one card, doesn't ask for permission
        refuteWith = refutes.iterator().next();
        tellRefute(refuter, refuteWith);
      } else if (!refutes.isEmpty()) { // The player chooses what to refute with
        refuteWith = askRefute(refuter, refutes);
      }

      // The guess was refuted
      if (refuteWith != null) {
        giveRefute(refuteWith);
        return false;
      }
    }

    return true; // No one refutes: this guess is a solution.
  }

  /**
   * Switches to <code>refuter</code>, and asks them to select a refute.
   */
  private Card askRefute(Player refuter, Set<Card> refutes) {
    prompt.changeRefuter(refuter);
    return prompt.promptCard("Select card to refute with:", refutes);
  }

  /**
   * Switches to <code>refuter</code>, and tells them the card they must refute with.
   */
  private void tellRefute(Player refuter, Card refuteWith) {
    prompt.changeRefuter(refuter);
    prompt.displayCard("You must refute with:", refuteWith);
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
    Set<Card> refutes = player.hand().stream().filter(inHand -> guess.toSet().contains(inHand))
        .collect(Collectors.toSet());
    return refutes;
  }

  /**
   * Tells the original guesser they were refuted by this card.
   * 
   * @param card what to refute with.
   */
  private void giveRefute(Card card) {
    prompt.changePlayer(guesser);
    prompt.displayCard("Your guess was refuted:", card);
  }

  /**
   * Tells the guesser their guess wasn't refuted.
   * 
   * @param cards their guess.
   */
  private void giveUnrefuted(CardTriple cards) {
    prompt.changePlayer(guesser);
    prompt.displayCardTriple("Your guess wasn't refuted:", cards);
  }

}
