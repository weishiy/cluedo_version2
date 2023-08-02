package net.swen225.hobbydetectives;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import GUI.GameGUI;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game {
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

    playerList.add(new Player("Bert", Color.YELLOW, 9, 1));
    playerList.add(new Player("Lucilla", Color.GREEN, 1, 11));
    playerList.add(new Player("Percy", Color.RED, 15, 22));
    playerList.add(new Player("Malina", Color.BLUE, 22, 9));
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
}
