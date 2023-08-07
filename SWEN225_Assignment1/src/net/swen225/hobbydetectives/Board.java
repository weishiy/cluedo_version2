package net.swen225.hobbydetectives;


import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// Board's width, height, layout are hardcoded for now for simplicity, can be changed later if needed.
public class Board {

    private final int width = 24;
    private final int height = 24;

    private final Set<Estate> rooms = new HashSet<>();
    private final Set<Estate> greyAreas = new HashSet<>();

    /***
     * Creates a new board for the game to operate on.
     *
     */
    public Board() {
        // 5 rooms
        rooms.add(new Estate(2, 2, 6, 6, EstateCard.HAUNTED_HOUSE, Set.of(new Door(6, 3), new Door(3, 6))));
        rooms.add(new Estate(17, 2, 21, 6, EstateCard.CALAMITY_CASTLE, Set.of(new Door(17, 5), new Door(20, 6))));
        rooms.add(new Estate(9, 10, 14, 13, EstateCard.VISITATION_VILLA, Set.of(new Door(12, 10), new Door(14, 11), new Door(9, 12), new Door(11, 13))));
        rooms.add(new Estate(2, 17, 6, 21, EstateCard.MANIC_MANOR, Set.of(new Door(3, 17), new Door(6, 18))));
        rooms.add(new Estate(17, 17, 21, 21, EstateCard.PERIL_PALACE, Set.of(new Door(18, 17), new Door(17, 20))));

        // four grey areas
        greyAreas.add(new Estate(11, 5, 12, 6));
        greyAreas.add(new Estate(5, 11, 6, 12));
        greyAreas.add(new Estate(11, 17, 12, 18));
        greyAreas.add(new Estate(17, 11, 18, 12));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Set<Estate> getRooms() {
        return Collections.unmodifiableSet(rooms);
    }

    public Set<Estate> getGreyAreas() {
        return Collections.unmodifiableSet(greyAreas);
    }

    public Estate getEstateAt(int x, int y) {
        return rooms.stream()
                .filter(r -> r.covers(x, y))
                .findFirst()
                .orElse(null);
    }

    public boolean canEnter(int x, int y) {
        // Out of border
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }

        // Rooms, except for doors, can not be entered
        if (rooms.stream().anyMatch(r -> r.covers(x, y) && r.doors().stream().noneMatch(d -> d.covers(x, y)))) {
            return false;
        }

        // Grey areas can not be entered
        if(greyAreas.stream().anyMatch(r -> r.covers(x, y))) {
            return false;
        }

        return true;
    }

}
