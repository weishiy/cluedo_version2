package net.swen225.hobbydetectives;

import net.swen225.hobbydetectives.board.Board;
import net.swen225.hobbydetectives.card.CardTriple;
import net.swen225.hobbydetectives.card.CharacterCard;
import net.swen225.hobbydetectives.card.EstateCard;
import net.swen225.hobbydetectives.card.WeaponCard;
import net.swen225.hobbydetectives.card.model.Card;
import net.swen225.hobbydetectives.player.Player;
import net.swen225.hobbydetectives.player.PlayerTurn;
import net.swen225.hobbydetectives.ui.bean.BoardBeanBuilder;
import net.swen225.hobbydetectives.ui.bean.PauseMessageBean;
import net.swen225.hobbydetectives.ui.view.ConsoleBasedGameUI;
import net.swen225.hobbydetectives.ui.view.GameUI;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class Game {

    private final Board board; // game board
    private final CardTriple solution;
    private final LinkedList<Player> playerQueue = new LinkedList<>();

    private final GameUI ui;
    /**
     * Main constructor, set up for game's need, but not finished yet.
     */
    public Game() {
        initPlayerQueue();
        board = new Board(Set.copyOf(playerQueue));
        solution = generateSolution();
        dispatchRemainingCards();

        ui = new ConsoleBasedGameUI();
    }

    private void initPlayerQueue() {
        var playerList = List.of(new Player(CharacterCard.LUCILLA, 11, 1),
                new Player(CharacterCard.BERT, 1, 9),
                new Player(CharacterCard.MALINA, 9, 22),
                new Player(CharacterCard.PERCY, 22, 14));

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
            playerQueue.get(playerIndex).addCard(nextCard);
            playerIndex++;
            playerIndex = playerIndex % playerQueue.size();
        }
    }

    private CardTriple generateSolution() {
        var allCharacterCards = Arrays.stream(CharacterCard.values()).collect(Collectors.toCollection(ArrayList::new));
        var randomCharacterCardIndex = (new Random()).nextInt(CharacterCard.values().length);
        var randomCharacterCard = allCharacterCards.get(randomCharacterCardIndex);

        var allEstateCards = Arrays.stream(EstateCard.values()).collect(Collectors.toCollection(ArrayList::new));
        var randomEstateCardIndex = (new Random()).nextInt(EstateCard.values().length);
        var randomEstateCard = allEstateCards.get(randomEstateCardIndex);

        var allWeaponCards = Arrays.stream(WeaponCard.values()).collect(Collectors.toCollection(ArrayList::new));
        var randomWeaponCardIndex = (new Random()).nextInt(WeaponCard.values().length);
        var randomWeaponCard = allWeaponCards.get(randomWeaponCardIndex);

        return new CardTriple(randomCharacterCard, randomEstateCard, randomWeaponCard);
    }

    private void startGame() throws ExecutionException, InterruptedException {
        while (!hasWinner() && hasActivePlayer()) {
            // Get one player out from the top of the queue
            var nextPlayer = playerQueue.remove();
            // Only active player gets playing
            if (nextPlayer.active()) {
                promptAndWaitForChangingPlayer(nextPlayer);
                var turn = new PlayerTurn(this, nextPlayer, Collections.unmodifiableList(playerQueue));
                turn.run();
            }
            // Add the player to the end of the queue
            playerQueue.offer(nextPlayer);
        }
        renderGameResult();
    }

    private void renderGameResult() throws InterruptedException, ExecutionException {
        renderGameWithSensitiveDataHidden();

        var resultMessage = new PauseMessageBean();
        var winner = playerQueue.stream().filter(Player::isWinner).findFirst().orElse(null);
        if (winner != null) {
            resultMessage.messageText("Winner is: " + winner.characterCard().toString());
        } else {
            resultMessage.messageText("No winner. Solution is: " + solution);
        }
        var future = ui.render(resultMessage);
        // Wait for user to confirm the result
        future.get();
    }

    private void renderGameWithSensitiveDataHidden() {
        ui.render(new BoardBeanBuilder().withFalsyDefaults().withBoard(board).withPlayers(board.players()).build());
    }

    private void promptAndWaitForChangingPlayer(Player nextPlayer) throws InterruptedException, ExecutionException {
        renderGameWithSensitiveDataHidden();
        var changingPlayerMessage = new PauseMessageBean();
        changingPlayerMessage.messageText("Next player is " + nextPlayer.characterCard().toString());
        var future = ui.render(changingPlayerMessage);
        future.get();
    }

    private boolean hasWinner() {
        return playerQueue.stream().anyMatch(Player::isWinner);
    }

    private boolean hasActivePlayer() {
        return playerQueue.stream().anyMatch(Player::active);
    }

    public Player findPlayer(CharacterCard card) {
        return playerQueue.stream().filter(p -> p.characterCard() == card).findFirst().orElse(null);
    }

    public CardTriple solution() {
        return solution;
    }

    public Board board() {
        return board;
    }

    public GameUI ui() {
        return ui;
    }

    /**
     * Main function for running the game
     *
     * @param args String - unused arguments
     */
    public static void main(String[] args) throws Exception {
        var game = new Game();
        game.startGame();
    }

}
