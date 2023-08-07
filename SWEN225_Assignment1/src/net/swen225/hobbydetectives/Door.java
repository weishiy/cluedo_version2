package net.swen225.hobbydetectives;

public class Door {

    private final int x;
    private final int y;

    public Door(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean covers(int x, int y) {
        return this.x == x && this.y == y;
    }

}
