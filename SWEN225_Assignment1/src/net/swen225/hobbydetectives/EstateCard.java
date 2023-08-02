package net.swen225.hobbydetectives;

/**
 * Cards representing estates, or rooms, where the murder took place.
 */
public enum EstateCard implements Card {
  VISITATION_VILLA("Visitation Villa"), HAUNTED_HOUSE("Haunted House"), MANIC_MANOR(
      "Manic Manor"), PERIL_PALACE("Peril Palace"), CALAMITY_CASTLE("Calamity Castle");

  private final String value;

  private EstateCard(String value) {
    this.value = value;
  }

  @Override
  public String value() {
    return value;
  }

}
