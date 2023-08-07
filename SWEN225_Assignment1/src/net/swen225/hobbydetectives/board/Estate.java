package net.swen225.hobbydetectives.board;

import net.swen225.hobbydetectives.card.EstateCard;

import java.util.*;

public class Estate {

    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;

    private final EstateCard estateCard;

    // Use Map.Entry<Integer, Integer> to simulator a 2d vector
    private final Set<Door> doors = new HashSet<>();

    public Estate(int x1, int y1, int x2, int y2) {
        this(x1, y1, x2, y2, null, Collections.emptySet());
    }

    public Estate(int x1, int y1, int x2, int y2, EstateCard estateCard, Set<Door> doors) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.estateCard = estateCard;
        doors.forEach(d -> {
            if (!isOnWall(d.getX(), d.getY()) || isAtCorner(d.getX(), d.getY())) {
                throw new IllegalArgumentException("Door must be on the wall and not at the corner");
            }
        });
        this.doors.addAll(doors);
    }

    private boolean isOnWall(int x, int y) {
        return x == x1 || x == x2 || y == y1 || y == y2;
    }

    private boolean isAtCorner(int x, int y) {
        return (x == x1 && y == y1)
                || (x == x1 && y == y2)
                || (x == x2 && y == y1)
                || (x == x2 && y == y2);
    }

    public int x1() {
        return x1;
    }

    public int y1() {
        return y1;
    }

    public int x2() {
        return x2;
    }

    public int y2() {
        return y2;
    }

    public EstateCard estateCard() {
        return estateCard;
    }

    public Set<Door> doors() {
        return Collections.unmodifiableSet(doors);
    }

    public boolean covers(int x, int y) {
        return x >= x1 && x <= x2 && y >= y1 && y <= y2;
    }

}
