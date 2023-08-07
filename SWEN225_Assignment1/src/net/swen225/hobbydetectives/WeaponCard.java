package net.swen225.hobbydetectives;

import java.util.Arrays;

public enum WeaponCard implements Card {
  BROOM("Broom"), SCISSORS("Scissors"), KNIFE("Knife"), SHOVEL("Shovel"), IPAD("iPad");

  private final String displayValue;

  WeaponCard(String displayValue) {
    this.displayValue = displayValue;
  }

  @Override
  public String toString() {
    return displayValue;
  }

  public static WeaponCard fromString(String value) {
    return Arrays.stream(WeaponCard.values()).filter(c -> c.toString().equalsIgnoreCase(value)).findFirst().orElse(null);
  }
}
