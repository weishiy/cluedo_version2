package net.swen225.hobbydetectives;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Prompt {
  private Game game;
  private Scanner scan;

  public Prompt(Game game) {
    this.game = game;
    scan = new Scanner(System.in);
  }

  public void changeRefuter(Player player) {
    // Print 100 empty lines to clear terminal history
    IntStream.range(0, 100).forEach(a -> System.out.println());

    System.out.println("Refuting...");
    System.out.println("Current refuter is: " + player.characterCard().toString());
    System.out.println("Press Enter key to continue...");
    try {
      System.in.read();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Print 100 empty lines to clear terminal history
    IntStream.range(0, 100).forEach(a -> System.out.println());
  }

  /**
   * Hands off control to the specified player.
   * 
   * Asks to be handed to the given player, and waits
   * 
   * @param player
   */
  public void changePlayer(Player player) {
    // Print 100 empty lines to clear terminal history
    IntStream.range(0, 100).forEach(a -> System.out.println());

    System.out.println("Refutation ended");
    System.out.println("Current player is: " + player.characterCard().toString());
    System.out.println("Press Enter key to continue...");
    try {
      System.in.read();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    // Print 100 empty lines to clear terminal history
    IntStream.range(0, 100).forEach(a -> System.out.println());
  }

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
    System.out.println(message + " " + card.toString());
    System.out.println("Press Enter key to continue...");
    try {
      System.in.read();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Displays the given card triple.
   * 
   * @param toDisplay message to pass.
   * @param triple the cards to display.
   */
  public void displayCardTriple(String toDisplay, CardTriple triple) {
    System.out.println(toDisplay + " " + triple);
  }

  /**
   * Just displays the String.
   * 
   * @param toDisplay the String to display.
   */
  public void display(String toDisplay) {
    System.out.println(toDisplay);
  }



}
