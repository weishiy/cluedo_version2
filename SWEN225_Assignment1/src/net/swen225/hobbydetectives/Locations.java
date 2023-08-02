package net.swen225.hobbydetectives;

/**
 * Places on the board, where <code>Tile</code>s can occupy, or <code>Player</code> can be in.
 * 
 * <code>BOARD</code> represents the outside area, where most tiles are. <code>GREY_AREA</code>
 * represents impassable zones, which are outside, but can't be occupied.
 */
public enum Locations {

  BOARD, HAUNTED_HOUSE, MANIC_MANOR, VISITATION_VILLA, CALAMITY_CASTLE, PERIL_PALACE, GREY_AREA
}
