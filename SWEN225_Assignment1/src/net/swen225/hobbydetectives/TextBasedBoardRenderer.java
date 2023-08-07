package net.swen225.hobbydetectives;

import java.util.*;
import java.util.stream.Collectors;

// ANSI Colour codes are used to colour the output text. 
// Add ;1 between the last number and the m to make the colour brighter, e.g. Black to Grey
/*
BLACK	\u001B[30m	BLACK_BACKGROUND	\u001B[40m
RED	    \u001B[31m	RED_BACKGROUND	    \u001B[41m
GREEN	\u001B[32m	GREEN_BACKGROUND	\u001B[42m
YELLOW	\u001B[33m	YELLOW_BACKGROUND	\u001B[43m
BLUE	\u001B[34m	BLUE_BACKGROUND	    \u001B[44m
PURPLE	\u001B[35m	PURPLE_BACKGROUND	\u001B[45m
CYAN	\u001B[36m	CYAN_BACKGROUND	    \u001B[46m
WHITE	\u001B[37m	WHITE_BACKGROUND	\u001B[47m

RESET FORMATTING                        \u001B[0m
*/

public class TextBasedBoardRenderer implements BoardRenderer {

    private final Board board;
    private final Set<Player> players = new HashSet<>();
    private final String[][] tiles;

    public TextBasedBoardRenderer(Board board, Set<Player> players) {
        this.board = board;
        this.tiles = new String[board.getHeight()][board.getWidth()];
        this.players.addAll(players);
    }

    public void render() {
        updateTiles();
        print();
    }

    private void updateTiles() {
        for (var y = 0; y < board.getHeight(); y++) {       // for each row
            for (var x = 0; x < board.getWidth(); x++) {    // for each cell
                tiles[y][x] = "\u001B[35;1m" + " - " + "\u001B[0m"; // Magenta
            }
        }

        board.getRooms().forEach(r -> {
            // floor and walls
            for (var y = r.y1(); y <= r.y2(); y++) {
                for (var x = r.x1(); x <= r.x2(); x++) {
                    if (x == r.x1() || x == r.x2() || y == r.y1() || y == r.y2()) {
                        tiles[y][x] = "\u001B[30;1m" + "[#]" + "\u001B[0m"; // Grey
                    } else {
                        tiles[y][x] = "   "; // White
                    }
                }
            }
            // doors (overwriting walls)
            r.doors().forEach(d -> tiles[d.getY()][d.getX()] = "\u001B[33m" + "[ ]" + "\u001B[0m"); // Yellow

            // room name (NOTE: room name spans several tiles)
            var centerY = (r.y1() + r.y2()) / 2;
            var minX = r.x1() + 1;
            var maxX = r.x2() - 1;
            var length = maxX - minX;
            var estateName = r.estateCard().toString();
            for (var deltaX = 0; deltaX <= length; deltaX++) {
                var x = minX + deltaX;
                // for each tile, get 3 chars from estate name
                var startingCharIndex = deltaX * 3;
                if (startingCharIndex < estateName.length()) {
                    var endingCharIndex = Math.min(startingCharIndex + 3, estateName.length());
                    tiles[centerY][x] = "\u001B[36m" + estateName.substring(startingCharIndex, endingCharIndex) + "\u001B[0m"; // Cyan
                }
            }
        });

        board.getGreyAreas().forEach(r -> {
            for (int y = r.y1(); y <= r.y2(); y++) {
                for (int x = r.x1(); x <= r.x2(); x++) {
                    tiles[y][x] = "\u001B[30;1m" + "[x]"  + "\u001B[0m"; // Grey
                }
            }
        });

        // Fill tiles with first characters of players (joined together if there're multiple players at the same
        // location), e,g " B " for Bert, "BM " for Bert and Malina, "BML" for Bert, Malina and Lucilla,
        // "ALL" for all 4 players
        // gather players by their location
        var buckets = new HashMap<String, Set<Player>>();
        players.stream()
               .filter(Player::active)
               .forEach(p -> {
            var coordinates = "(" + p.x() + ", " + p.y() + ")";
            if (!buckets.containsKey(coordinates)) {
                buckets.put(coordinates, new HashSet<>());
            }
            var playersAtCurrentCoordinates = buckets.get(coordinates);
            playersAtCurrentCoordinates.add(p);
            buckets.put(coordinates, playersAtCurrentCoordinates);
        });
        // join player names
        buckets.values().forEach(b -> {
            var onePlayer = b.iterator().next();
            var firstCharactersJoined = b.stream().map(TextBasedBoardRenderer::firstCharacterOfCharacterName).collect(Collectors.joining(""));
            tiles[onePlayer.y()][onePlayer.x()] = switch (firstCharactersJoined.length()) {
                case 1 -> " " + firstCharactersJoined + " ";
                case 2 -> firstCharactersJoined + " ";
                case 3 -> firstCharactersJoined;
                default -> " " + "ALL" + " ";  // In case there're more than 3 players at the same location, display "ALL"
            };
        });
    }

    private static String firstCharacterOfCharacterName(Player p) {
        return p.characterCard().toString().substring(0, 1).toUpperCase();
    }

    private void print() {
        for (var y = 0; y < board.getHeight(); y++) {       // for each row
            for (var x = 0; x < board.getWidth(); x++) {    // for each cell
                System.out.print(tiles[y][x]);
            }
            System.out.println();
        }
    }

}
