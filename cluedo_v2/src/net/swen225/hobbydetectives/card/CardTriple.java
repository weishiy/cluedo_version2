package net.swen225.hobbydetectives.card;

import net.swen225.hobbydetectives.card.model.Card;

import java.util.Objects;
import java.util.Set;

/**
 * Contains one of each type of card, for use in guesses and solutions.
 */
public record CardTriple(CharacterCard character, EstateCard estate, WeaponCard weapon) {

  /**
   * Checks that all values are non-null.
   * 
   * @param character CharacterCard
   * @param estate EstateCard
   * @param weapon WeaponCard
   */
  public CardTriple {
    Objects.requireNonNull(character);
    Objects.requireNonNull(estate);
    Objects.requireNonNull(weapon);
  }

  /**
   * Set of cards contained within this.
   * 
   * @return this set.
   */
  public Set<Card> toSet() {
    return Set.of(character, estate, weapon);
  }

  public String toString() {
    return String.join(", ", character.toString(), estate.toString(), weapon.toString());
  }
}
