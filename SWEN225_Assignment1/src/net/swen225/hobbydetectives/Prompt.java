package net.swen225.hobbydetectives;

import java.util.Scanner;
import java.util.Set;

public class Prompt {
  private Game game;
  private Scanner scan;

  public Prompt(Game game) {
    this.game = game;
    scan = new Scanner(System.in);
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
   * @param question String - Question to pose to the player
   * @param acceptableResponses Set<String> - List of all possible responses that will be accepted for this question
   * @return VALID::[response] when accepted, or ERROR::Incorrect Response when failed.
   */
  public String prompt(String question, Set<String> acceptableResponses) {
    System.out.println(question + "/n");
    String userInput = scan.nextLine();
    /*this is a very hacky way of doing error codes and responses, if this were elixir
    * I would be having a much better time.
    * Alas, I am not.
    * Change it how you wish!
    * -Joseph
    */
    for(String possibleResponse:acceptableResponses){
      if(possibleResponse.equalsIgnoreCase(userInput))
        return "VALID::" + possibleResponse;
      //why possibleResponse over user input? because of the equalsIgnoreCase! Makes it easier if we
      //just stick to casing used in the set, rather than adapting it to everything
    }
    return "ERROR::Incorrect Response";
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
