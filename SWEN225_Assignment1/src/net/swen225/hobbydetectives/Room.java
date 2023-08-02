package net.swen225.hobbydetectives;

import java.util.*;

public class Room {
    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;

    private final Locations locations;

    // Use Map.Entry<Integer, Integer> to simulator a 2d vector
    private final Set<Map.Entry<Integer, Integer>> doors = new HashSet<>();

    public Room(int x1, int y1, int x2, int y2, Locations locations) {
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
