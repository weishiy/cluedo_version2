package net.swen225.hobbydetectives;

import java.util.*;

import GUI.GameGUI;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class Game {

  private List<Player> turnOrder = new ArrayList<Player>();

  private Prompt prompt;

  /**
   * Maps the CharacterCard to the corresponding player.
   */
  private Map<CharacterCard, Player> characterCardToPlayer;

  /**
   * Gets map of CharacterCards to players.
   * 
   * @return this map.
   */
  public Map<CharacterCard, Player> characterCardToPlayer() {
    return Collections.unmodifiableMap(characterCardToPlayer);
  }

  /**
   * Returns the current prompt.
   * 
   * @return The current prompt.
   */
  public Prompt prompt() {
    return prompt;
  }


  public Board board() {
    return board;
  }

  /**
   * Returns the 3 next players who are after the current one in turn order.
   * 
   * @return <code>Stream</code> of the players to come.
   */

  /*todo
   * I feel as if there is an easier way to do this,
   * I proposed a bubble sort method and attaching a specific ID onto each player to identify
   * their placement.
   *
   * Have a look and tell me what you think, I suspect it might be easier
   *
   */
  public Iterator<Player> getNextPlayers() {
    // TODO: Stub
    return null;
  }

  private final Board board; // game board
  private final List<Player> playerList = new ArrayList<>();
  private final GameGUI gameGUI;


  /**
   * Main constructor, set up for game's need, but not finished yet.
   */
  public Game() {
    board = new Board();
    // print board for debugging purpose
    System.out.print(board);

    //rearranged player creation to respect turn order via turnID
    playerList.add(new Player("Lucilla", Color.GREEN, 1, 11, 1));
    playerList.add(new Player("Bert", Color.YELLOW, 9, 1, 2));
    playerList.add(new Player("Malina", Color.BLUE, 22, 9, 3));
    playerList.add(new Player("Percy", Color.RED, 15, 22, 4));

    playerList.forEach(p -> p.setCurrentTileLocation(board.inspectTile(p.x(), p.y())));
    gameGUI = new GameGUI(this);

    SwingUtilities.invokeLater(() -> {
      gameGUI.setVisible(true);
    });



  }

  /**
   * Main function for running the game
   * 
   * @param args
   */

  public static void main(String[] args) {
    Game board = new Game();
  }

  public Board getBoard() {
    return board;
  }

  public List<Player> getPlayerList() {
    return playerList;
  }

  void createGameStartingPoint(List<Player> playerList, int playerAmount){

  }

  private List<Player> sortTurnOrder(){
    return turnOrder.stream().sorted(Comparator.comparingInt(Player::turnID)).toList();
  }

  /***
   * Creates a prompt to ask how many users are playing
   * @return Integer - how many people are playing (between 2-4)
   * @throws IllegalAccessException - Preformed when an invalid input it given
   */
  private int getCurrentPlayerCount() throws IllegalAccessException {
    String response = prompt.prompt("How many people are playing?", Set.of("2", "3", "4"));
    String[] promptResponse = response.split("::");
    if(promptResponse[0].equals("ERROR")){
      throw new IllegalAccessException("Invalid prompt response");
    }
    return Integer.parseInt(promptResponse[1]);
  }


}
