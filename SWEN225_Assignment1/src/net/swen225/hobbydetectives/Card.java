package net.swen225.hobbydetectives;

/**
 * Cards used as solution, or held in hand to refute guesses.
 */
public interface Card {

  /**
   * The name of the card, to be displayed.
   * 
   * @return this name.
   */
  public String value();

}
