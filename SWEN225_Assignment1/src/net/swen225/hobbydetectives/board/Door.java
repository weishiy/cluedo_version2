package net.swen225.hobbydetectives.board;

public record Door(int x, int y) {

    public boolean covers(int x, int y) {
        return this.x == x && this.y == y;
    }

}
