package net.swen225.hobbydetectives;

import java.awt.*;

public enum TileType {
    HALLWAY("-", true, Color.WHITE),
    ROOM(" ", false, Color.WHITE), // Players are supposed to just stay on the door, not to go inside the room
    WALL("#", false, Color.GRAY),
    DOOR("@", true, Color.YELLOW),
    GREY_AREA("x", false, Color.GRAY);   // For all other areas that's inaccessible

    private final String stringRepresentation;
    private final boolean accessible;   // Decides whether players can enter/stay on the tile
    private final Color color;

    TileType(String stringRepresentation, boolean accessible, Color color) {
        this.stringRepresentation = stringRepresentation;
        this.accessible = accessible;
        this.color = color;
    }

    @Override
    public String toString() {
        return stringRepresentation;
    }

    public boolean accessible() {
        return accessible;
    }

    public Color color() {
        return color;
    }

}
