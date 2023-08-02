package net.swen225.hobbydetectives;


import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


import java.util.HashSet;
import java.util.Set;


public class Board {



  /**
   * Associates each location with the correct estate card.
   */
  public static final Map<Locations, EstateCard> locationsToEstateCard =
      Map.of(Locations.VISITATION_VILLA, EstateCard.VISITATION_VILLA, Locations.HAUNTED_HOUSE,
          EstateCard.HAUNTED_HOUSE, Locations.MANIC_MANOR, EstateCard.MANIC_MANOR,
          Locations.CALAMITY_CASTLE, EstateCard.CALAMITY_CASTLE, Locations.PERIL_PALACE,
          EstateCard.PERIL_PALACE);

  /***
   * Returns a Tile object from the specified coordinates of the board
   * 
   * @param x Integer - Represents X axis
   * @param y Integer - Represents Y axis
   * @return Tile - Tile found at X and Y coordinates
   */
  public Tile inspectTile(int x, int y) {
    return board[x][y];
  }

  /***
   * Returns the tile of a given directional input based off of a given tile
   * 
   * @param from Tile - Tile to measure direction from
   * @param d Direction - direction to get the next tile from
   * @return Tile - Tile object given by the direction
   */
  public Tile getDirectionTile(Tile from, Direction d) {
    switch (d) {
      case NORTH -> {
        return inspectTile(from.x(), from.y() + 1);
      }
      case SOUTH -> {
        return inspectTile(from.x(), from.y() - 1);
      }
      case EAST -> {
        return inspectTile(from.x() + 1, from.y());
      }
      case WEST -> {
        return inspectTile(from.x() - 1, from.y());
      }
      default -> throw new IllegalArgumentException();
    }
  }

  public static final int SIZE = 24;

  private final Set<Room> rooms = new HashSet<>();
  private final Tile[][] board = new Tile[SIZE][SIZE];

  /***
   * Creates a new board for the game to operate on.
   *
   */
  public Board() {
    // the basics on setting up a room and a door
    // HAUNTED_HOUSE
    var hauntedHouse = new Room(2, 2, 6, 6, Locations.HAUNTED_HOUSE);
    hauntedHouse.addDoor(3, 6);
    hauntedHouse.addDoor(6, 5);
    rooms.add(hauntedHouse);

    // MANIC_MANOR
    var manicManor = new Room(2, 17, 6, 21, Locations.MANIC_MANOR);
    hauntedHouse.addDoor(5, 17);
    hauntedHouse.addDoor(6, 20);
    rooms.add(manicManor);

    // VISITATION_VILLA
    var visitationVilla = new Room(10, 9, 13, 14, Locations.VISITATION_VILLA);
    visitationVilla.addDoor(10, 12);
    visitationVilla.addDoor(12, 9);
    visitationVilla.addDoor(13, 11);
    visitationVilla.addDoor(11, 14);
    rooms.add(visitationVilla);

    // CALAMITY_CASTLE
    var calamityCastle = new Room(17, 2, 21, 6, Locations.CALAMITY_CASTLE);
    calamityCastle.addDoor(17, 3);
    calamityCastle.addDoor(18, 6);
    rooms.add(calamityCastle);

    // PERIL_PALACE
    var perilPlace = new Room(17, 17, 21, 21, Locations.PERIL_PALACE);
    perilPlace.addDoor(17, 18);
    perilPlace.addDoor(20, 17);
    rooms.add(perilPlace);

    // four grey areas
    rooms.add(new Room(11, 5, 12, 6, Locations.GREY_AREA));
    rooms.add(new Room(5, 11, 6, 12, Locations.GREY_AREA));
    rooms.add(new Room(11, 17, 12, 18, Locations.GREY_AREA));
    rooms.add(new Room(17, 11, 18, 12, Locations.GREY_AREA));

    updateTiles();
  }

  /***
   * Creates the full board representation and plots spaces between rooms
   */
  public void updateTiles() {
    for (int x = 0; x < SIZE; x++) {
      for (int y = 0; y < SIZE; y++) {
        board[x][y] = new Tile(x, y, TileType.HALLWAY, Locations.BOARD);
      }
    }

    rooms.forEach(r -> {
      // floor and walls
      for (int x = r.getX1(); x <= r.getX2(); x++) {
        for (int y = r.getY1(); y <= r.getY2(); y++) {
          if (x == r.getX1() || x == r.getX2() || y == r.getY1() || y == r.getY2()) {
            board[x][y] = new Tile(x, y, TileType.WALL, r.getLocations());
          } else {
            // not ideal, but a room can be a room (literally) or a grey area. doesn't want to
            // create
            // another class for grey area
            if (r.getLocations() == Locations.GREY_AREA) {
              board[x][y] = new Tile(x, y, TileType.GREY_AREA, r.getLocations());
            } else {
              board[x][y] = new Tile(x, y, TileType.ROOM, r.getLocations());
            }
          }
        }
      }

      // doors (overwriting walls)
      r.getDoors().forEach(v -> board[v.getKey()][v.getValue()] =
          new Tile(v.getKey(), v.getValue(), TileType.DOOR, r.getLocations()));
    });
  }



  public Set<Room> getRooms() {
    return rooms;
  }
}
