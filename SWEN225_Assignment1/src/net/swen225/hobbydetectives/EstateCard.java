package net.swen225.hobbydetectives;

import java.util.Arrays;

/**
 * Cards representing estates, or rooms, where the murder took place.
 */
public enum EstateCard implements Card {
  VISITATION_VILLA("Visitation Villa"), HAUNTED_HOUSE("Haunted House"), MANIC_MANOR(
      "Manic Manor"), PERIL_PALACE("Peril Palace"), CALAMITY_CASTLE("Calamity Castle");

  private final String displayValue;

  private EstateCard(String displayValue) {
    this.displayValue = displayValue;
  }

  @Override
  public String toString() {
    return displayValue;
  }

  public static EstateCard fromString(String value) {
    return Arrays.stream(EstateCard.values()).filter(c -> c.toString().equalsIgnoreCase(value)).findFirst().orElse(null);
  }
}
