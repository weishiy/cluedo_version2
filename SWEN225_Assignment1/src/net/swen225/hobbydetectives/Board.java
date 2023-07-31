package net.swen225.hobbydetectives;

public class Board {

    private final Tile[][] board = new Tile[25][25];

    /***
     * Creates a new board for the game to operate on.
     * @param players Variable - Player - Accepts a variable number of players to the board
     */
    public Board(Player... players){
        setBoard();
        for(Player p:players)
            setPlayer(p, p.x(), p.y());
    }

    /***
     * Creates the full board representation and plots spaces between rooms
     */
    private void setBoard(){
        //the basics on setting up a room and a door
        addRoom(3, 3, 6, 6, Locations.TEST_ROOM);
        setDoor(3, 4, Locations.TEST_ROOM);
        for(int x = 0; x < 25; x++)
            for(int y = 0; y < 25; y++)
                if(board[x][y] == null)
                    board[x][y] = new Tile(x, y, "_", Locations.BOARD);
    }

    /***
     * Creates a room on the board
     * @param x1 Integer - Represents top left X coordinate
     * @param y1 Integer - Represents top left Y coordinate
     * @param x2 Integer - Represents bottom right X coordinate
     * @param y2 Integer - Represents bottom right Y coordinate
     * @param room Locations - Location enum representation
     */
    private void addRoom(int x1, int y1, int x2, int y2, Locations room){
        for (int x = x1; x < x2; x++) {
            board[x][y1] = new Tile(x, y1, "#", room);
            board[x][y2] = new Tile(x, y2, "#", room);
        }
        for (int y = y1; y < y2; y++) {
            board[x1][y] = new Tile(x1, y, "#", room);
            board[x2][y] = new Tile(x2, y, "#", room);
        }
    }

    /***
     * Creates a new door Tile at specified coordinates
     * @param x Integer - Represents X axis
     * @param y Integer - Represents Y axis
     * @param room Location - Represents connected room
     */
    private void setDoor(int x, int y, Locations room){
        board[x][y] = new Tile(x, y, "@", room);
    }

    /***
     * Returns a Tile object from the specified coordinates of the board
     * @param x Integer - Represents X axis
     * @param y Integer - Represents Y axis
     * @return Tile - Tile found at X and Y coordinates
     */
    private Tile inspectTile(int x, int y){
        return board[x][y];
    }

    private void setPlayer(Player player, int x, int y){
        board[x][y] = new Tile(x, y, "*", player.getCurrentRoom());
    }

    /***
     * Returns a string representation of the current game board
     * @return String - The board
     */
    public String toString(){
        //I could remake this using streams, but that can be done later.
        String Sboard = "";
        for(int x = 0; x < 25; x++){
            for(int y = 0; y < 25; y++){
                Sboard = Sboard.concat(board[x][y].value());
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
    public Tile getDirectionTile(Tile from, Direction d){
        switch(d){
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
            default ->
                throw new IllegalArgumentException();
        }
    }

}
