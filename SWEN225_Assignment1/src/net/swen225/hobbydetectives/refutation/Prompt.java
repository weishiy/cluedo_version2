package net.swen225.hobbydetectives.refutation;

import net.swen225.hobbydetectives.card.CardTriple;
import net.swen225.hobbydetectives.card.model.Card;
import net.swen225.hobbydetectives.player.Player;

import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Prompt {

  private final Scanner scan;

  public Prompt() {
    this.scan = new Scanner(System.in);
  }

  public void changeRefuter(Player player) {
    // Print 100 empty lines to clear terminal history
    IntStream.range(0, 100).forEach(a -> System.out.println());

    System.out.println("Refuting...");
    System.out.println("Current refuter is: " + player.characterCard().toString());
    waitForEnter();
  }

  /**
   * Hands off control to the specified player.
   * <p>
   * Asks to be handed to the given player, and waits
   * 
   * @param player Player - the current player in the refutation
   */
  public void changePlayer(Player player) {
    // Print 100 empty lines to clear terminal history
    IntStream.range(0, 100).forEach(a -> System.out.println());

    System.out.println("Refutation ended");
    System.out.println("Current player is: " + player.characterCard().toString());

    waitForEnter();
  }

  /**
   * Prompts the current player to select a card.
   * <p></p>
   * Prints this question to the screen, and acceptable cards, and waits for the player to select a
   * card by name. If the player doesn't enter a valid response, loops.
   * 
   * @param question posed to the current player.
   * @param acceptableCards the possible cards to choose from.
   * @return The card the player chose.
   */

  public <R extends Card> R promptCard(String question, Set<R> acceptableCards) {
    while (true) {
      System.out.println(question);
      System.out.println("    " + acceptableCards.stream().map(Card::toString).collect(Collectors.joining(", ")));
      var s = new Scanner(System.in);
      var userInput = s.nextLine();
      var card = acceptableCards.stream().filter(c -> c.toString().equalsIgnoreCase(userInput)).findFirst().orElse(null);
      if (card == null) {
        System.out.println("Invalid input: " + userInput);
      } else {
        return card;
      }
    }
  }

  /**
   * Prompts the current player to select a card.
   * <p>
   * Prints this question to the screen, and acceptable cards, and waits for the player to select a
   * card by name. If the player doesn't enter a valid response, loops. This method is a wrapper for
   * <code>promptCard(String, Set)</code> for simpler use.
   * 
   * @param question posed to the current player.
   * @param acceptableCards the possible cards to choose from.
   * @return The card the player chose.
   */
  @SafeVarargs
  public final <R extends Card> R promptCard(String question, R... acceptableCards) {
    return promptCard(question, Set.of(acceptableCards));
  }

  /**
   * Prints the given card with the message.
   * 
   * @param message the message to print.
   * @param card the card to print.
   */
  public void displayCard(String message, Card card) {
    System.out.println(message + " " + card.toString());
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ignored) {}
    waitForEnter();
  }

  /**
   * Displays the given card triple.
   * 
   * @param toDisplay message to pass.
   * @param triple the cards to display.
   */
  public void displayCardTriple(String toDisplay, CardTriple triple) {
    System.out.println(toDisplay + " " + triple);
    try {
      Thread.sleep(2000);
    } catch (InterruptedException ignored) {}
    waitForEnter();
  }

  /**
   *  Waits for the user to press Enter
   */
public void waitForEnter() {
  System.out.println("Press Enter key to continue...");
  scan.nextLine();
}

}
