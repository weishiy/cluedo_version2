package net.swen225.hobbydetectives;

import java.util.Set;

public class Prompt {
  private Game game;

  public Prompt(Game game) {
    this.game = game;
  }

  /**
   * Hands off control to the specified player.
   * 
   * Asks to be handed to the given player, and waits
   * 
   * @param player
   */
  public void changePlayer(Player player) {}

  /**
   * Asks the player a question, accepting only certain responses.
   * 
   * Poses <code>question</code>, and the possible responses to the screen. Waits for the player to
   * type in one of the responses given, repeating the prompt as necessary.
   * 
   * @param question
   * @param acceptable
   * @return
   */
  public String prompt(String question, Set<String> acceptableResponses) {
    // TODO: Stub
    return null;
  }

  /**
   * Prompts the current player to select a card.
   * 
   * Prints this question to the screen, and acceptable cards, and waits for the player to select a
   * card by name. If the player doesn't enter a valid response, loops.
   * 
   * @param question posed to the current player.
   * @param acceptableCards the possible cards to choose from.
   * @return The card the player chose.
   */
  public <R extends Card> R promptCard(String question, Set<R> acceptableCards) {
    // TODO: Stub
    return null;
  }

  /**
   * Prompts the current player to select a card.
   * 
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
    // TODO Auto-generated method stub

  }

  /**
   * Displays the given card triple.
   * 
   * @param toDisplay message to pass.
   * @param triple the cards to display.
   */
  public void displayCardTriple(String toDisplay, CardTriple triple) {
    // TODO Auto-generated method stub

  }

  /**
   * Just displays the String.
   * 
   * @param toDisplay the String to display.
   */
  public void display(String toDisplay) {
    // TODO Auto-generated method stub

  }



}
