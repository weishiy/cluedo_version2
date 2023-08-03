package net.swen225.hobbydetectives;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Player {

  /** Starting location of the player. */
  private final int x, y, turnID;

  /** Player name */
  private final String name;
  private final Color color;

  /**
   * Player's current location on the board.
   * <p></p>
   * Should be <code>null</code> whenever they are inside a room.
   */
  private Tile currentTileLocation;

  /**
   * Where they are on the board.
   * <p></p>
   * When outside, it should be <code>BOARD</code>, otherwise, it should be the location of an
   * estate.
   */
  private Locations currentRoom = Locations.BOARD;
  /**
   * This player's hand: what cards they possess.
   */
  private final List<Card> currentCards = new ArrayList<>();

  /** Whether the player is eligible to continue playing and moving. */
  private boolean active = true;


  /***
   * Creates a new Player object
   * 
   * @param name String - name of the player
   * @param color Color - colour representation of the player
   * @param x Int - X coordinate of the player's starting location
   * @param y Int - Y coordinate of the player's starting location
   * @param turnID Int - used for the sorting of players in the specified turn order
   * @param cards Variable - Card - A variable amount of refutation cards that the player may
   *        have
   */
  public Player(String name, Color color, int x, int y, int turnID, Card... cards) {
    this.name = name;
    this.color = color;
    this.x = x;
    this.y = y;
    this.turnID = turnID;
    Collections.addAll(currentCards, cards);
  }

  // Note: should we just make the private final fields x and y public, since they are already
  // final?

  //Agreed, I shall make them final. -Joseph
  public int x() {
    return x;
  }

  public int y() {
    return y;
  }

  public int turnID(){ return turnID; }

  // Ditto as above.
  public String getName() {
    return name;
  }

  public Color getColor() {
    return color;
  }

  public void setCurrentRoom(Locations currentRoom) {
    this.currentRoom = currentRoom;
  }

  public Locations getCurrentRoom() {
    return currentRoom;
  }

  public void setCurrentTileLocation(Tile currentTileLocation) {
    this.currentTileLocation = currentTileLocation;
  }

  public Tile getCurrentTileLocation() {
    return currentTileLocation;
  }


  /**
   * Returns the player's hand
   * 
   * @return the hand of the player.
   */
  public Set<Card> hand() {
    return currentCards.stream().collect(Collectors.toUnmodifiableSet());
  }

  /***
   * Checks if the player's refutation card contains a given query
   * 
   * @param query String - query to search by
   * @return True if the player does, else false.
   */
  @Deprecated(since = "03/08/23")
  public boolean queryCards(String query) { // Using Strings seem problematic
    for (Card card : currentCards)
      if (card.value().equals(query))
        return true;
    return false;
  }


  /***
   * Moves the player into the given tile or room depending on the data of the tile
   * 
   * @param newTileLoc Tile - New tile to be moved into
   */
  public void move(Tile newTileLoc) {
    // checks if it's a door or not
    if (newTileLoc.tileType() == TileType.DOOR ||
    // player is actually impossible to enter the room, but just in case
        newTileLoc.tileType() == TileType.ROOM) {
      setCurrentRoom(newTileLoc.locations());
    }
    setCurrentTileLocation(newTileLoc);
  }

  /**
   * Moves the player into the given room directly
   * 
   * @param location the new room to move the player in
   */
  public void teleport(Locations location) {
    // location must be a room. Perhaps better place to store which locations are Rooms?
    final Set<Locations> rooms = Set.of(Locations.CALAMITY_CASTLE, Locations.HAUNTED_HOUSE,
        Locations.MANIC_MANOR, Locations.PERIL_PALACE, Locations.VISITATION_VILLA);
    assert rooms.contains(location) : "Must be a room to teleport into";
    setCurrentRoom(location);
  }


}
