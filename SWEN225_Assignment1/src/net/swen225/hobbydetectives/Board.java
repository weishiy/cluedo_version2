package net.swen225.hobbydetectives;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Board {

    /**
     * Associates each location with the correct estate card.
     */
    public static final Map<Locations, EstateCard> locationsToEstateCard =
            Map.of(Locations.VISITATION_VILLA, EstateCard.VISITATION_VILLA, Locations.HAUNTED_HOUSE,
                    EstateCard.HAUNTED_HOUSE, Locations.MANIC_MANOR, EstateCard.MANIC_MANOR,
                    Locations.CALAMITY_CASTLE, EstateCard.CALAMITY_CASTLE, Locations.PERIL_PALACE,
                    EstateCard.PERIL_PALACE);

    public static final int SIZE = 24;

    private final Set<Building> rooms = new HashSet<>();
    private final Tile[][] board = new Tile[SIZE][SIZE];

    /***
     * Creates a new board for the game to operate on.
     *
     */
    public Board() {
        // fill the board with all hallway tiles
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                board[x][y] = new Tile(x, y, TileType.HALLWAY, Locations.BOARD);
            }
        }

        // add room tiles (overwriting hallway tiles)
        // HAUNTED_HOUSE
        var hauntedHouse = new Building(2, 2, 6, 6, Locations.HAUNTED_HOUSE);
        hauntedHouse.addDoor(3, 6);
        hauntedHouse.addDoor(6, 5);
        rooms.add(hauntedHouse);

        // MANIC_MANOR
        var manicManor = new Building(2, 17, 6, 21, Locations.MANIC_MANOR);
        hauntedHouse.addDoor(5, 17);
        hauntedHouse.addDoor(6, 20);
        rooms.add(manicManor);

        // VISITATION_VILLA
        var visitationVilla = new Building(10, 9, 13, 14, Locations.VISITATION_VILLA);
        visitationVilla.addDoor(10, 12);
        visitationVilla.addDoor(12, 9);
        visitationVilla.addDoor(13, 11);
        visitationVilla.addDoor(11, 14);
        rooms.add(visitationVilla);

        // CALAMITY_CASTLE
        var calamityCastle = new Building(17, 2, 21, 6, Locations.CALAMITY_CASTLE);
        calamityCastle.addDoor(17, 3);
        calamityCastle.addDoor(18, 6);
        rooms.add(calamityCastle);

        //PERIL_PALACE
        var perilPlace = new Building(17, 17, 21, 21, Locations.PERIL_PALACE);
        perilPlace.addDoor(17, 18);
        perilPlace.addDoor(20, 17);
        rooms.add(perilPlace);

        rooms.forEach(r -> {
            // floor and walls
            for (int x = r.getX1(); x <= r.getX2(); x++) {
                for (int y = r.getY1(); y <= r.getY2(); y++) {
                    if (x == r.getX1() || x == r.getX2() || y == r.getY1() || y == r.getY2()) {
                        board[x][y] = new Tile(x, y, TileType.WALL, r.getLocations());
                    } else {
                        board[x][y] = new Tile(x, y, TileType.ROOM, r.getLocations());
                    }
                }
            }
            // doors (overwriting walls)
            r.getDoors().forEach(v -> board[v.getKey()][v.getValue()] = new Tile(v.getKey(), v.getValue(), TileType.DOOR, r.getLocations()));
        });

        // four grey areas  (overwriting hallway tiles)
        Set<Building> greyAreas = new HashSet<>();
        greyAreas.add(new Building(11, 5, 12, 6, Locations.GREY_AREA));
        greyAreas.add(new Building(5, 11, 6, 12, Locations.GREY_AREA));
        greyAreas.add(new Building(11, 17, 12, 18, Locations.GREY_AREA));
        greyAreas.add(new Building(17, 11, 18, 12, Locations.GREY_AREA));

        greyAreas.forEach(r -> {
            for (int x = r.getX1(); x <= r.getX2(); x++) {
                for (int y = r.getY1(); y <= r.getY2(); y++) {
                    board[x][y] = new Tile(x, y, TileType.GREY_AREA, r.getLocations());
                }
            }
        });
    }

    /***
     * Returns a Tile object from the specified coordinates of the board
     * @param x Integer - Represents X axis
     * @param y Integer - Represents Y axis
     * @return Tile - Tile found at X and Y coordinates
     */
    public Tile inspectTile(int x, int y) {
        if (x < 0 || y < 0 || x >= SIZE || y >= SIZE) {
            return null;
        }
        return board[x][y];
    }

    /***
     * Returns a string representation of the current game board
     * @return String - The board
     */
    public String toString() {
        //I could remake this using streams, but that can be done later.
        String Sboard = "";
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Sboard = Sboard.concat(board[x][y].tileType().toString());
            }
            Sboard = Sboard.concat("\n");
        }
        return Sboard;
    }

    /***
     * Returns the tile of a given directional input based off of a given tile
     * @param from Tile - Tile to measure direction from
     * @param d Direction - direction to get the next tile from
     * @return Tile - Tile object given by the direction
     */
    public Tile getDirectionTile(Tile from, Direction d) {
        switch (d) {
            case NORTH -> {
                return inspectTile(from.x() - 1, from.y());
            }
            case SOUTH -> {
                return inspectTile(from.x() + 1, from.y());
            }
            case EAST -> {
                return inspectTile(from.x(), from.y() + 1);
            }
            case WEST -> {
                return inspectTile(from.x(), from.y() - 1);
            }
            default -> throw new IllegalArgumentException();
        }
    }

    public Map<String, Map.Entry<Integer, Integer>> getRoomNamesAndPositions() {
        return rooms.stream().collect(Collectors.toMap(r -> r.locations.toString(), r -> Map.entry(r.x1, r.y1)));
    }

    private static class Building {
        private final int x1;
        private final int y1;
        private final int x2;
        private final int y2;

        private final Locations locations;

        // Use Map.Entry<Integer, Integer> to simulator a 2d vector
        private final Set<Map.Entry<Integer, Integer>> doors = new HashSet<>();

        public Building(int x1, int y1, int x2, int y2, Locations locations) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
            this.locations = locations;
        }

        public void addDoor(int x, int y) {
            doors.add(Map.entry(x, y));
        }

        public int getX1() {
            return x1;
        }

        public int getY1() {
            return y1;
        }

        public int getX2() {
            return x2;
        }

        public int getY2() {
            return y2;
        }

        public Locations getLocations() {
            return locations;
        }

        public Set<Map.Entry<Integer, Integer>> getDoors() {
            return doors;
        }

    }
}
