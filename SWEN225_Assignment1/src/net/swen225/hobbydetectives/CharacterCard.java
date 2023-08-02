package net.swen225.hobbydetectives;

/**
 * Cards which represent characters, possible killers.
 */
public enum CharacterCard implements Card {
  LUCILLA("Lucilla"), BERT("Bert"), MALINA("Malina"), PERCY("Percy");

  private final String value;

  private CharacterCard(String value) {
    this.value = value;
  }

  @Override
  public String value() {
    return value;
  }

}
