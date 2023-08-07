package net.swen225.hobbydetectives;

import java.util.Arrays;

/**
 * Cards which represent characters, possible killers.
 */
public enum CharacterCard implements Card {
  LUCILLA("Lucilla"), BERT("Bert"), MALINA("Malina"), PERCY("Percy");

  private final String displayValue;

  private CharacterCard(String displayValue) {
    this.displayValue = displayValue;
  }

  @Override
  public String toString() {
    return displayValue;
  }

  public static CharacterCard fromString(String value) {
    return Arrays.stream(CharacterCard.values()).filter(c -> c.toString().equals(value)).findFirst().orElse(null);
  }

}
