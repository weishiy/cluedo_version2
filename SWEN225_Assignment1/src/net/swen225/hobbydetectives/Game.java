package net.swen225.hobbydetectives;

import net.swen225.hobbydetectives.board.Board;
import net.swen225.hobbydetectives.board.TextBasedBoardRenderer;
import net.swen225.hobbydetectives.board.model.BoardRenderer;
import net.swen225.hobbydetectives.card.CardTriple;
import net.swen225.hobbydetectives.card.CharacterCard;
import net.swen225.hobbydetectives.card.EstateCard;
import net.swen225.hobbydetectives.card.model.Card;
import net.swen225.hobbydetectives.card.WeaponCard;
import net.swen225.hobbydetectives.player.Player;
import net.swen225.hobbydetectives.player.PlayerTurn;
import net.swen225.hobbydetectives.refutation.Prompt;

import java.util.*;
import java.util.stream.Collectors;

public class Game {

    private final Board board; // game board
    private final BoardRenderer boardRenderer;
    private final Prompt prompt;
    private CardTriple solution;
    private final List<Player> playerList = new ArrayList<>();
    private final LinkedList<Player> playerQueue = new LinkedList<>();

    private boolean running = false;

    public Board board() {
        return board;
    }

    /**
     * Returns the current prompt.
     *
     * @return The current prompt.
     */
    public Prompt prompt() {
        return prompt;
    }

    /**
     * Main constructor, set up for game's need, but not finished yet.
     */
    public Game() {
        playerList.add(new Player(CharacterCard.LUCILLA, 11, 1));
        playerList.add(new Player(CharacterCard.BERT, 1, 9));
        playerList.add(new Player(CharacterCard.MALINA, 9, 22));
        playerList.add(new Player(CharacterCard.PERCY, 22, 14));

        generateSolution();
        dispatchRemainingCards();

        board = new Board();
        
        initPlayerQueue();
        boardRenderer = new TextBasedBoardRenderer(board, Set.copyOf(playerList));
        prompt = new Prompt();

        startGame();
    }

    private void initPlayerQueue() {
        int randomPosition = (int) (Math.random() * 4);
        int listPosition = randomPosition;
        while (listPosition < playerList.size()) {
            playerQueue.add(playerList.get(listPosition));
            listPosition++;
        }
        listPosition = 0;
        while (listPosition != randomPosition) {
            playerQueue.add(playerList.get(listPosition));
            listPosition++;
        }
    }

    private void dispatchRemainingCards() {
        var remainingCards = new ArrayList<Card>();
        remainingCards.addAll(Arrays.stream(CharacterCard.values()).filter(c -> c != solution.character()).toList());
        remainingCards.addAll(Arrays.stream(EstateCard.values()).filter(c -> c != solution.estate()).toList());
        remainingCards.addAll(Arrays.stream(WeaponCard.values()).filter(c -> c != solution.weapon()).toList());
        Collections.shuffle(remainingCards);

        var playerIndex = 0;
        for (var nextCard : remainingCards) {
            playerList.get(playerIndex).addCard(nextCard);
            playerIndex++;
            playerIndex = playerIndex % playerList.size();
        }
    }

    private void generateSolution() {
        var allCharacterCards = Arrays.stream(CharacterCard.values()).collect(Collectors.toCollection(ArrayList::new));
        var randomCharacterCardIndex = (new Random()).nextInt(CharacterCard.values().length);
        var randomCharacterCard = allCharacterCards.get(randomCharacterCardIndex);

        var allEstateCards = Arrays.stream(EstateCard.values()).collect(Collectors.toCollection(ArrayList::new));
        var randomEstateCardIndex = (new Random()).nextInt(EstateCard.values().length);
        var randomEstateCard = allEstateCards.get(randomEstateCardIndex);

        var allWeaponCards = Arrays.stream(WeaponCard.values()).collect(Collectors.toCollection(ArrayList::new));
        var randomWeaponCardIndex = (new Random()).nextInt(WeaponCard.values().length);
        var randomWeaponCard = allWeaponCards.get(randomWeaponCardIndex);

        solution = new CardTriple(randomCharacterCard, randomEstateCard, randomWeaponCard);
    }

    private void startGame() {
        running = true;
        while (running && !playerQueue.isEmpty()) {
            // Get one player out from the top of the queue
            var currentPlayer = playerQueue.poll();
            var turn = new PlayerTurn(this, currentPlayer, Collections.unmodifiableList(playerQueue));
            turn.run();
            // Add the currentPlayer to the end of the queue
            playerQueue.offer(currentPlayer);
        }

        var winner = playerList.stream().filter(Player::isWinner).findFirst().orElse(null);
        if (winner != null) {
            System.out.println("Winner is: " + winner.characterCard().toString());
        } else {
            System.out.println("No winner");
            System.out.println("Solution is: " + solution);
        }
        System.out.println("Game ended.");
    }

    public Player findPlayer(CharacterCard card) {
        return playerList.stream().filter(p -> p.characterCard() == card).findFirst().orElse(null);
    }

    public BoardRenderer boardRenderer() {
        return boardRenderer;
    }

    public CardTriple solution() {
        return solution;
    }

    public void endGame() {
        running = false;
    }

    /**
     * Main function for running the game
     *
     * @param args String - unused arguments
     */
    public static void main(String[] args) {
        new Game();
    }

}
