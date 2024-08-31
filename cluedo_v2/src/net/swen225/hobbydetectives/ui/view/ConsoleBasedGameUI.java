package net.swen225.hobbydetectives.ui.view;

import net.swen225.hobbydetectives.card.model.Card;
import net.swen225.hobbydetectives.player.Player;
import net.swen225.hobbydetectives.ui.bean.BoardBean;
import net.swen225.hobbydetectives.ui.bean.ChooseCardBean;
import net.swen225.hobbydetectives.ui.bean.PauseMessageBean;
import net.swen225.hobbydetectives.ui.controller.Controller;
import net.swen225.hobbydetectives.ui.controller.MovementActions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ConsoleBasedGameUI implements GameUI {

    private volatile Controller controller;

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void render(BoardBean boardBean) {
        var board = boardBean.board();
        var tiles = new String[board.getHeight()][board.getWidth()];
        var players = Set.copyOf(boardBean.players());
        var currentPlayer = boardBean.currentPlayer();
        var stepsLeft = boardBean.stepsLeft();

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
            r.doors().forEach(d -> tiles[d.y()][d.x()] = "\u001B[33m" + "[ ]" + "\u001B[0m"); // Yellow

            // room name (NOTE: room name spans several tiles)
            var centerY = (r.y1() + r.y2()) / 2;
            var minX = r.x1() + 1;
            var maxX = r.x2() - 1;
            var length = maxX - minX;
            var estateName = r.estateCard().toString() + "    ";
            if (r.estateCard().toString().equals("Visitation Villa")) {estateName = "Visitation Villa  ";}
            for (var deltaX = 0; deltaX <= (length * 2); deltaX++) {
                //var x = minX + deltaX;
                // for each tile, get 3 chars from estate name
                var startingCharIndex = deltaX * 3;
                if (startingCharIndex < estateName.length()) {
                    var endingCharIndex = Math.min(startingCharIndex + 3, estateName.length());
                    tiles[centerY + (deltaX / 3)][minX + (deltaX % 3)] = "\u001B[36m" + estateName.substring(startingCharIndex, endingCharIndex) + "\u001B[0m"; // Cyan
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

        // Fill tiles with first characters of players (joined together if there are multiple players at the same
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
            var firstCharactersJoined = b.stream().map(ConsoleBasedGameUI::firstCharacterOfCharacterName).collect(Collectors.joining(""));
            tiles[onePlayer.y()][onePlayer.x()] = switch (firstCharactersJoined.length()) {
                case 1 -> " " + firstCharactersJoined + " ";
                case 2 -> firstCharactersJoined + " ";
                case 3 -> firstCharactersJoined;
                default -> " " + "ALL" + " ";  // In case there are more than 3 players at the same location, display "ALL"
            };
        });

        // Print board
        for (var y = 0; y < board.getHeight(); y++) {       // for each row
            for (var x = 0; x < board.getWidth(); x++) {    // for each cell
                System.out.print(tiles[y][x]);
            }
            System.out.println();
        }

        // Print player info and controls
        if (currentPlayer != null) {
            System.out.println("You are: " + currentPlayer.characterCard().toString());
            System.out.println("Your cards: ");
            System.out.println("    " + currentPlayer.hand().stream().map(Card::toString).collect(Collectors.joining(", ")));

            System.out.println("You've " + stepsLeft + " steps left. ");

            var allowedActions = new HashMap<String, MovementActions>();
            if (boardBean.canMoveUp()) {
                allowedActions.put("U", MovementActions.UP);
            }
            if (boardBean.canMoveDown()) {
                allowedActions.put("D", MovementActions.DOWN);
            }
            if (boardBean.canMoveLeft()) {
                allowedActions.put("L", MovementActions.LEFT);
            }
            if (boardBean.canMoveRight()) {
                allowedActions.put("R", MovementActions.RIGHT);
            }
            if (boardBean.canGuess()) {
                allowedActions.put("G", MovementActions.GUESS);
            }
            allowedActions.put("A", MovementActions.ACCUSE);
            allowedActions.put("E", MovementActions.END_TURN);

            new Thread(() -> {
                var validInput = false;
                while (!validInput) {
                    System.out.println("Choose any of below to continue: ");
                    allowedActions.forEach((k, v) -> System.out.println(k + " - " + v.toString()));

                    var s = new Scanner(System.in);
                    var userInput = s.nextLine();

                    if (allowedActions.containsKey(userInput) && controller != null) {
                        controller.process(allowedActions.get(userInput));
                        validInput = true;
                    } else {
                        System.out.println("Invalid input: " + userInput);
                    }
                }
            }).start();
        }
    }

    @Override
    public Card render(ChooseCardBean chooseCardBean) {
        System.out.println(chooseCardBean.promptText());

        while (true) {
            chooseCardBean.cards().forEach(System.out::println);

            var scan = new Scanner(System.in);
            var userInput = scan.nextLine();
            var card = chooseCardBean.cards().stream().filter(c -> c.toString().equals(userInput)).findFirst().orElse(null);

            if (card != null) {
                return card;
            }
        }
    }

    @Override
    public void render(PauseMessageBean pauseMessageBean) {
        System.out.println(pauseMessageBean.messageText());
        System.out.println("Press Enter key to roll...");

        (new Scanner(System.in)).nextLine();
    }

    private static String firstCharacterOfCharacterName(Player p) {
        return p.characterCard().toString().substring(0, 1).toUpperCase();
    }

}
