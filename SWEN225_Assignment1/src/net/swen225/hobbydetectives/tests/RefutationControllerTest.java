package net.swen225.hobbydetectives.tests;

import static org.junit.Assert.assertEquals;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import net.swen225.hobbydetectives.Board;
import net.swen225.hobbydetectives.Card;
import net.swen225.hobbydetectives.CardTriple;
import net.swen225.hobbydetectives.CharacterCard;
import net.swen225.hobbydetectives.EstateCard;
import net.swen225.hobbydetectives.Game;
import net.swen225.hobbydetectives.Locations;
import net.swen225.hobbydetectives.Player;
import net.swen225.hobbydetectives.Prompt;
import net.swen225.hobbydetectives.RefutationController;
import net.swen225.hobbydetectives.WeaponCard;

/**
 * Tests RefutationController
 * 
 * This version doesn't assume any behaviour of dependencies (Game, Prompt, Board, Player) so
 * emulates their behaviour using mock objects. It does assume the existence and correctness of the
 * enumerations: Locations, CharacterCard, EstateCard, and WeaponCard, and the behaviour of Card and
 * CardTriple.
 */
class RefutationControllerTest {

  /** All possible cards */
  @SuppressWarnings("unused")
  private static final Set<Card> deck =
      Stream.of(CharacterCard.values(), WeaponCard.values(), EstateCard.values())
          .<Card>flatMap(e -> Stream.of(e)).collect(Collectors.toSet());

  /** Information for generating players */
  private static enum MockPlayerInfo {
    LUCILLA("Lucilla", CharacterCard.LUCILLA), BERT("Bert", CharacterCard.BERT), MALINA("Malina",
        CharacterCard.MALINA), PERCY("Percy", CharacterCard.PERCY);

    public final String name;
    public final CharacterCard card;

    private MockPlayerInfo(String name, CharacterCard card) {
      this.name = name;
      this.card = card;
    }


  }

  /** Creates uniquely identifiable String representing these cards */
  private static String logCards(Collection<? extends Card> cards) {
    return cards.stream().map(Card::toString).sorted().collect(Collectors.joining(":"));
  }

  private static String logCards(Card... cards) {
    return logCards(List.of(cards));
  }

  /**
   * Data used to construct tests.
   */
  private static class TestData {
    /** Collects recorded actions */
    private final List<List<String>> log = new ArrayList<>();

    private final Game game;
    private final Board board;
    private final Prompt prompt;
    private final Player guesser;
    private final List<Player> refuters;

    private final Map<CharacterCard, Player> characterCardToPlayer = new HashMap<>();

    /**
     * Initialises data with given arguments.
     * 
     * @param players the player information to take place in the test, in refutation order.
     * @param hands the hands belonging to the players. The index of each set should correspond to
     *        the index of each player in <code>players</code>.
     * @param guesserInfo the guesser information.
     * @param guesserLocation the location of the guesser.
     * @param promptResponses whenever mock <code>prompt</code> asks for a card, it takes from this
     *        list.
     */
    public TestData(List<MockPlayerInfo> players, List<Set<? extends Card>> hands,
        MockPlayerInfo guesserInfo, Locations guesserLocation,
        List<? extends Card> promptResponses) {
      // Creates and manages players
      guesser = makePlayer(guesserInfo, guesserLocation);
      characterCardToPlayer.put(guesserInfo.card, guesser);

      // Creates players that refute, and add them to <code>characterCardToPlayer</code>
      refuters = IntStream.range(0, players.size()).mapToObj(i -> {
        MockPlayerInfo mpi = players.get(i);
        Set<? extends Card> hand = hands.get(i);

        Player p = makePlayer(mpi, hand.toArray(new Card[0]));
        characterCardToPlayer.put(mpi.card, p);

        return p;
      }).toList();

      // board tracks teleportations
      board = new Board() {

      };

      game = makeGame();
      prompt = makePrompt(promptResponses);

    }

    /**
     * Generates a player connected to this card.
     * 
     * @param location where this player is.
     * @param hand the cards in this player's hand.
     * 
     * @return a new player.
     */
    public Player makePlayer(MockPlayerInfo info, Locations location, Card... hand) {
      return new Player(info.name, null, null, hand) {
        @Override
        public String toString() {
          return info.name;
        }

        @Override
        public Locations getCurrentRoom() {
          return location;
        }

        @Override
        public void teleport(Locations location) {
          log.add(List.of("teleport", info.name, location.toString()));
        }
      };
    }

    public Player makePlayer(MockPlayerInfo info, Card... cards) {
      return makePlayer(info, null, cards);
    }

    /** Prompts log actions made. */
    private Prompt makePrompt(List<? extends Card> promptResponse) {
      Deque<? extends Card> _promptResponse = new ArrayDeque<>(promptResponse);
      return new Prompt(null) {

        @Override
        public void changePlayer(Player player) {
          log.add(List.of("changePlayer", player.toString()));
        }

        @SuppressWarnings("unchecked") // the test writer should ensure <code>promptResponse</code>
                                       // contains the correct type of <code>Card</code>
        @Override
        public <R extends Card> R promptCard(String question, Set<R> acceptableCards) {
          log.add(List.of("promptCard", question, logCards(acceptableCards)));

          if (_promptResponse.isEmpty()) {
            throw new AssertionError("Requesting more elements than test provided.");
          }
          return (R) _promptResponse.pop();
        }

        @Override
        public void display(String toDisplay) {
          log.add(List.of("display", toDisplay));
        }

        @Override
        public void displayCardTriple(String toDisplay, CardTriple triple) {
          log.add(List.of("displayCardTriple", toDisplay, logCards(triple.toSet())));
        }

        @Override
        public void displayCard(String toDisplay, Card card) {
          log.add(List.of("displayCard", toDisplay, card.toString()));
        }
      };
    }

    /** Contains references to static data. */
    private Game makeGame() {
      return new Game() {

        @Override
        public Prompt prompt() {
          return TestData.this.prompt;
        }

        @Override
        public Board board() {
          return TestData.this.board;
        }

        @Override
        public Iterator<Player> getNextPlayers() {
          return TestData.this.refuters.iterator();
        }

        @Override
        public Map<CharacterCard, Player> characterCardToPlayer() {
          return TestData.this.characterCardToPlayer;
        }
      };
    }
  }


  // Tests 0-4 don't have successful guesses
  @Test
  void test_01() {
    /* @formatter:off
     * Hands:
     * Lucilla: PERCY, KNIFE, VISITATION_VILLA,
     * Bert: LUCILLA, BERT, BROOM, PERIL_PALACE,
     * Malina:SCISSORS, MANIC_MANOR, CALAMITY_CASTLE
     * Percy: MALINA, SHOVEL, IPAD, HAUNTED_HOUSE,
     * 
     * Bert enters haunted house, and guesses PERCY, SHOVEL, HAUNTED_HOUSE.
     * Percy is teleported to the haunted house
     * (optional) the Shovel is moved to the haunted house
     * Malina can't refute.
     * Percy must refute either SHOVEL or HAUNTED_HOUSE. He chooses HAUNTED_HOUSE.
     * 
     * @formatter:on
     */

    List<List<String>> expectedLog = List.of(
        // Asking guess
        List.of("promptCard", "Guess the murder weapon:", logCards(WeaponCard.values())),
        List.of("promptCard", "Guess the killer:", logCards(CharacterCard.values())),
        List.of("teleport", "Percy", "HAUNTED_HOUSE"),
        // Asking refutation
        List.of("changePlayer", "Percy"),
        List.of("promptCard", "Select card to refute with:",
            logCards(WeaponCard.SHOVEL, EstateCard.HAUNTED_HOUSE)),
        // Showing refutation
        List.of("changePlayer", "Bert"),
        List.of("displayCard", "Your guess was refuted:", EstateCard.HAUNTED_HOUSE.toString()));

    TestData info = new TestData(
        // Player info
        List.of(MockPlayerInfo.MALINA, MockPlayerInfo.PERCY, MockPlayerInfo.LUCILLA),
        // Player hands
        List.of(Set.of(WeaponCard.SCISSORS, EstateCard.MANIC_MANOR, EstateCard.CALAMITY_CASTLE),
            Set.of(CharacterCard.MALINA, WeaponCard.SHOVEL, WeaponCard.IPAD,
                EstateCard.HAUNTED_HOUSE),
            Set.of(CharacterCard.PERCY, WeaponCard.KNIFE, EstateCard.VISITATION_VILLA)),
        // Guesser
        MockPlayerInfo.BERT,
        // Estate of guesser
        Locations.HAUNTED_HOUSE,
        // Prompt responses
        List.of(
            // Bert makes his guess
            WeaponCard.SHOVEL, CharacterCard.PERCY,
            // Percy refutes
            EstateCard.HAUNTED_HOUSE));

    RefutationController rc = new RefutationController(info.game, info.guesser);
    assertEquals(expectedLog.toString(), info.log.toString());
    assertEquals(Optional.empty(), rc.unrefutedGuess());
  }

  @Test
  void test_02() {
    /*
     * @formatter:off
     * Hands:
     * Lucilla: CharacterCard.LUCILLA, WeaponCard.SCISSORS, WeaponCard.KNIFE, WeaponCard.SHOVEL,
     * Bert: EstateCard.VISITATION_VILLA, EstateCard.PERIL_PALACE, EstateCard.CALAMITY_CASTLE,  
     * Malina: CharacterCard.BERT, CharacterCard.MALINA, CharacterCard.PERCY, EstateCard.HAUNTED_HOUSE
     * Percy: EstateCard.MANIC_MANOR, WeaponCard.BROOM, WeaponCard.IPAD
     * 
     * Malina enters Locations.CALAMITY_CASTLE
     * Malina guesses WeaponCard.SHOVEL, CharacterCard.LUCILLA
     * Teleport Lucilla to CALAMITY_CASTLE
     * Percy can't refute
     * Lucilla must refute CharacterCard.LUCILLA, WeaponCard.SHOVEL
     * Lucilla refutes CharacterCard.LUCILLA
     * 
     * @formatter:on
     */


    String expectedLog = List.of(
        // asking guess
        List.of("promptCard", "Guess the murder weapon:", logCards(WeaponCard.values())),
        List.of("promptCard", "Guess the killer:", logCards(CharacterCard.values())),
        List.of("teleport", "Lucilla", "CALAMITY_CASTLE"),

        // asking refute
        List.of("changePlayer", "Lucilla"), //
        List.of("promptCard", "Select card to refute with:",
            logCards(CharacterCard.LUCILLA, WeaponCard.SHOVEL)),
        // sending refute
        List.of("changePlayer", "Malina"), //
        List.of("displayCard", "Your guess was refuted:", CharacterCard.LUCILLA)).toString();

    TestData data = new TestData(
        // Player info
        List.of(MockPlayerInfo.PERCY, MockPlayerInfo.LUCILLA, MockPlayerInfo.BERT),
        // Player hands
        List.of(Set.of(EstateCard.MANIC_MANOR, WeaponCard.BROOM, WeaponCard.IPAD),
            Set.of(CharacterCard.LUCILLA, WeaponCard.SCISSORS, WeaponCard.KNIFE, WeaponCard.SHOVEL),
            Set.of(EstateCard.VISITATION_VILLA, EstateCard.PERIL_PALACE,
                EstateCard.CALAMITY_CASTLE)),
        // Guesser
        MockPlayerInfo.MALINA, Locations.CALAMITY_CASTLE,
        // Prompt responses
        List.of(WeaponCard.SHOVEL, CharacterCard.LUCILLA, CharacterCard.LUCILLA));

    RefutationController rc = new RefutationController(data.game, data.guesser);

    assertEquals(expectedLog, data.log.toString());
    assertEquals(Optional.empty(), rc.unrefutedGuess());
  }

  @Test
  // Tests specific case of player must refute 1 card
  void test_03() {
    /*
     * @formatter:off
     * Hands:
     * Lucilla: WeaponCard.SCISSORS, WeaponCard.KNIFE, WeaponCard.SHOVEL, WeaponCard.IPAD
     * Bert: CharacterCard.MALINA, CharacterCard.PERCY, EstateCard.VISITATION_VILLA, EstateCard.HAUNTED_HOUSE,
     * Malina: CharacterCard.LUCILLA, EstateCard.PERIL_PALACE, EstateCard.CALAMITY_CASTLE, 
     * Percy: CharacterCard.BERT, EstateCard.MANIC_MANOR, WeaponCard.BROOM
     * 
     * Bert enters (Location) Locations.VISITATION_VILLA
     * Bert guesses (Weapon, Character) WeaponCard.KNIFE, CharacterCard.LUCILLA
     * Teleport Lucilla to VISITATION_VILLA
     * Malina must refute CharacterCard.LUCILLA
     * The display is different in this case.
     * 
     * 
     * @formatter:on
     */


    String expectedLog = List.of(
        // asking guess
        List.of("promptCard", "Guess the murder weapon:", logCards(WeaponCard.values())),
        List.of("promptCard", "Guess the killer:", logCards(CharacterCard.values())),
        List.of("teleport", "Lucilla", "VISITATION_VILLA"),

        // saying what you must refute
        List.of("changePlayer", "Malina"), //
        List.of("displayCard", "You must refute with:", CharacterCard.LUCILLA),
        // sending refute
        List.of("changePlayer", "Bert"), //
        List.of("displayCard", "Your guess was refuted:", CharacterCard.LUCILLA)).toString();

    TestData data = new TestData(
        // Player info
        List.of(MockPlayerInfo.MALINA, MockPlayerInfo.PERCY, MockPlayerInfo.LUCILLA),
        // Player hands
        List.of(Set.of(CharacterCard.LUCILLA, EstateCard.PERIL_PALACE, EstateCard.CALAMITY_CASTLE),
            Set.of(CharacterCard.BERT, EstateCard.MANIC_MANOR, WeaponCard.BROOM),
            Set.of(WeaponCard.SCISSORS, WeaponCard.KNIFE, WeaponCard.SHOVEL, WeaponCard.IPAD)),
        // Guesser
        MockPlayerInfo.BERT, Locations.VISITATION_VILLA,
        // Prompt responses
        List.of(WeaponCard.KNIFE, CharacterCard.LUCILLA));

    RefutationController rc = new RefutationController(data.game, data.guesser);

    assertEquals(expectedLog, data.log.toString());
    assertEquals(Optional.empty(), rc.unrefutedGuess());
  }

  @Test
  void test_04() {
    /*
     * @formatter:off
     * Hands:
     * Lucilla: CharacterCard.LUCILLA, WeaponCard.BROOM, WeaponCard.SHOVEL, 
     * Bert: CharacterCard.BERT, EstateCard.HAUNTED_HOUSE, EstateCard.MANIC_MANOR, EstateCard.PERIL_PALACE, 
     * Malina: CharacterCard.MALINA, EstateCard.VISITATION_VILLA, EstateCard.CALAMITY_CASTLE, WeaponCard.SCISSORS  
     * Percy: CharacterCard.PERCY, WeaponCard.KNIFE,WeaponCard.IPAD
     * 
     * Percy guesses
     * Percy enters Locations.PERIL_PALACE
     * Percy guesses: WeaponCard.IPAD, CharacterCard.BERT
     * Teleport Bert to Locations.PERIL_PALACE
     * 
     * Lucilla can't refute
     * Bert must refute CharacterCard.BERT, EstateCard.PERIL_PALACE
     * Bert chooses EstateCard.PERIL_PALACE
     * 
     * 
     * @formatter:on
     */

    String expectedLog = List.of(
        // asking guess
        List.of("promptCard", "Guess the murder weapon:", logCards(WeaponCard.values())),
        List.of("promptCard", "Guess the killer:", logCards(CharacterCard.values())),
        List.of("teleport", "Bert", "PERIL_PALACE"),

        // saying what you must refute
        List.of("changePlayer", "Bert"), //
        List.of("promptCard", "Select card to refute with:",
            logCards(CharacterCard.BERT, EstateCard.PERIL_PALACE)),
        // sending refute
        List.of("changePlayer", "Percy"), //
        List.of("displayCard", "Your guess was refuted:", EstateCard.PERIL_PALACE)).toString();

    TestData data = new TestData(
        // Player info
        List.of(MockPlayerInfo.LUCILLA, MockPlayerInfo.BERT, MockPlayerInfo.MALINA),
        // Player hands
        List.of(Set.of(CharacterCard.LUCILLA, WeaponCard.BROOM, WeaponCard.SHOVEL),
            Set.of(CharacterCard.BERT, EstateCard.HAUNTED_HOUSE, EstateCard.MANIC_MANOR,
                EstateCard.PERIL_PALACE),
            Set.of(CharacterCard.MALINA, EstateCard.VISITATION_VILLA, EstateCard.CALAMITY_CASTLE,
                WeaponCard.SCISSORS)),
        // Guesser
        MockPlayerInfo.PERCY, Locations.PERIL_PALACE,
        // Prompt responses
        List.of(WeaponCard.IPAD, CharacterCard.BERT, EstateCard.PERIL_PALACE));

    RefutationController rc = new RefutationController(data.game, data.guesser);

    assertEquals(expectedLog, data.log.toString());
    assertEquals(Optional.empty(), rc.unrefutedGuess());

  }

  @Test
  void test_05() {
    /*
     * @formatter:off
     * Solution: CharacterCard.MALINA, EstateCard.HAUNTED_HOUSE, WeaponCard.KNIFE
     * 
     * Hands:
     * Lucilla: CharacterCard.LUCILLA, CharacterCard.PERCY, WeaponCard.SHOVEL, 
     * Bert: CharacterCard.BERT, EstateCard.MANIC_MANOR, EstateCard.PERIL_PALACE,
     * Malina: EstateCard.CALAMITY_CASTLE, WeaponCard.IPAD, WeaponCard.SCISSORS
     * Percy: EstateCard.VISITATION_VILLA, WeaponCard.BROOM, 
     * 
     * Bert guesses
     * Bert enters Locations.MANIC_MANOR
     * Bert guesses: WeaponCard.KNIFE, CharacterCard.BERT
     * Teleport Bert to Locations.MANIC_MANOR
     * ?Should we check for superfluous teleportation?
     * 
     * Malina can't refute
     * Percy can't refute
     * Lucilla can't refute
     * 
     * @formatter:on
     */

    String expectedLog = List.of(
        // asking guess
        List.of("promptCard", "Guess the murder weapon:", logCards(WeaponCard.values())),
        List.of("promptCard", "Guess the killer:", logCards(CharacterCard.values())),
        List.of("teleport", "Bert", "MANIC_MANOR"),
        // telling unrefuted
        List.of("changePlayer", "Bert"), //
        List.of("displayCardTriple", "Your guess wasn't refuted:",
            logCards(WeaponCard.KNIFE, CharacterCard.BERT, EstateCard.MANIC_MANOR))

    ).toString();

    TestData data = new TestData(
        // Player info
        List.of(MockPlayerInfo.MALINA, MockPlayerInfo.PERCY, MockPlayerInfo.LUCILLA),
        // Player hands
        List.of(Set.of(EstateCard.CALAMITY_CASTLE, WeaponCard.IPAD, WeaponCard.SCISSORS),
            Set.of(EstateCard.VISITATION_VILLA, WeaponCard.BROOM),
            Set.of(CharacterCard.LUCILLA, CharacterCard.PERCY, WeaponCard.SHOVEL)),
        // Guesser
        MockPlayerInfo.BERT, Locations.MANIC_MANOR,
        // Prompt responses
        List.of(WeaponCard.KNIFE, CharacterCard.BERT));

    RefutationController rc = new RefutationController(data.game, data.guesser);
    assertEquals(expectedLog, data.log.toString());
    assertEquals(
        Optional.of(new CardTriple(CharacterCard.BERT, EstateCard.MANIC_MANOR, WeaponCard.KNIFE)),
        rc.unrefutedGuess());
  }

  @Test
  void test_06() {
    /*
     * @formatter:off
     * Solution: CharacterCard.LUCILLA, EstateCard.VISITATION_VILLA, WeaponCard.BROOM
     * 
     * Hands:
     * Lucilla: EstateCard.CALAMITY_CASTLE, WeaponCard.SCISSORS
     * Bert: CharacterCard.MALINA, CharacterCard.PERCY, EstateCard.PERIL_PALACE
     * Malina: WeaponCard.KNIFE, WeaponCard.SHOVEL, WeaponCard.IPAD
     * Percy: CharacterCard.BERT, EstateCard.HAUNTED_HOUSE, EstateCard.MANIC_MANOR
     * 
     * Percy guesses
     * Locations.PERIL_PALACE
     * Guesses WeaponCard.SCISSORS
     * Guesses CharacterCard.MALINA
     * 
     * Teleports Malina to Peril Palace.
     * 
     * Lucilla must refute with WeaponCard.SCISSORS
     * 
     * @formatter:on
     */

    String expectedLog = List.of(
        // Asking guess
        List.of("promptCard", "Guess the murder weapon:", logCards(WeaponCard.values())),
        List.of("promptCard", "Guess the killer:", logCards(CharacterCard.values())),
        // Teleport
        List.of("teleport", "Malina", Locations.PERIL_PALACE),
        // Asking refute
        List.of("changePlayer", "Lucilla"),
        List.of("displayCard", "You must refute with:", WeaponCard.SCISSORS),
        // Telling refute
        List.of("changePlayer", "Percy"),
        List.of("displayCard", "Your guess was refuted:", WeaponCard.SCISSORS)).toString();

    TestData data = new TestData(
        // Player info
        List.of(MockPlayerInfo.LUCILLA, MockPlayerInfo.BERT, MockPlayerInfo.MALINA),
        // Player hands
        List.of(Set.of(EstateCard.CALAMITY_CASTLE, WeaponCard.SCISSORS),
            Set.of(CharacterCard.MALINA, CharacterCard.PERCY, EstateCard.PERIL_PALACE),
            Set.of(WeaponCard.KNIFE, WeaponCard.SHOVEL, WeaponCard.IPAD)),
        // Guesser info
        MockPlayerInfo.PERCY, Locations.PERIL_PALACE,
        // Prompt responses
        List.of(WeaponCard.SCISSORS, CharacterCard.MALINA));

    RefutationController rc = new RefutationController(data.game, data.guesser);
    assertEquals(expectedLog, data.log.toString());
    assertEquals(Optional.empty(), rc.unrefutedGuess());

  }

  @Test
  void test_07() {
    /*
     * @formatter:off
     * Solution: CharacterCard.BERT, EstateCard.PERIL_PALACE, WeaponCard.BROOM
     * 
     * Hands:
     * Lucilla: CharacterCard.LUCILLA, EstateCard.HAUNTED_HOUSE, WeaponCard.SCISSORS, WeaponCard.KNIFE
     * Bert: CharacterCard.MALINA, CharacterCard.PERCY, WeaponCard.IPAD
     * Malina: EstateCard.VISITATION_VILLA, EstateCard.MANIC_MANOR, EstateCard.CALAMITY_CASTLE, WeaponCard.SHOVEL
     * 
     * Lucilla guessing
     * Locations.PERIL_PALACE
     * Guesses WeaponCard.IPAD
     * Guesses CharacterCard.BERT
     * 
     * Bert teleports to Locations.PERIL_PALACE
     * 
     * Bert must refute WeaponCard.IPAD
     * 
     * @formatter:on
     */

    String expectedLog = List.of(
        // Asking guess
        List.of("promptCard", "Guess the murder weapon:", logCards(WeaponCard.values())),
        List.of("promptCard", "Guess the killer:", logCards(CharacterCard.values())),
        // Teleport
        List.of("teleport", "Bert", Locations.PERIL_PALACE),
        // Ask refutation
        List.of("changePlayer", "Bert"),
        List.of("displayCard", "You must refute with:", WeaponCard.IPAD),
        // Display refutation
        List.of("changePlayer", "Lucilla"),
        List.of("displayCard", "Your guess was refuted:", WeaponCard.IPAD)).toString();

    TestData data = new TestData(
        // Player info and hands
        List.of(MockPlayerInfo.BERT, MockPlayerInfo.MALINA),
        List.of(Set.of(CharacterCard.MALINA, CharacterCard.PERCY, WeaponCard.IPAD),
            Set.of(EstateCard.VISITATION_VILLA, EstateCard.MANIC_MANOR, EstateCard.CALAMITY_CASTLE,
                WeaponCard.SHOVEL)),
        // Guesser info and location
        MockPlayerInfo.LUCILLA, Locations.PERIL_PALACE,
        // Prompt responses
        List.of(WeaponCard.IPAD, CharacterCard.BERT));

    var rc = new RefutationController(data.game, data.guesser);
    assertEquals(expectedLog, data.log.toString());
    assertEquals(Optional.empty(), rc.unrefutedGuess());

  }

  @Test
  void test_08() {
    /*
     * @formatter:off
     * Solution: CharacterCard.BERT, EstateCard.CALAMITY_CASTLE, WeaponCard.KNIFE
     * 
     * Hands:
     * LUCILLA: CharacterCard.PERCY, EstateCard.VISITATION_VILLA, WeaponCard.IPAD
     * MALINA: CharacterCard.LUCILLA, WeaponCard.BROOM, WeaponCard.SCISSORS, WeaponCard.SHOVEL
     * PERCY: CharacterCard.MALINA, EstateCard.HAUNTED_HOUSE, EstateCard.MANIC_MANOR, EstateCard.PERIL_PALACE
     * 
     * Percy is guessing
     * Enters EstateCard.HAUNTED_HOUSE
     * Guesses WeaponCard.KNIFE
     * Guesses CharacterCard.MALINA
     * 
     * Teleport malina to Haunted House
     * 
     * Lucilla can't refute
     * Malina can't refute
     * 
     * @formatter:on
     */

    String expectedLog = List.of(
        // Asking guess
        List.of("promptCard", "Guess the murder weapon:", logCards(WeaponCard.values())),
        List.of("promptCard", "Guess the killer:", logCards(CharacterCard.values())),
        List.of("teleport", "Malina", "HAUNTED_HOUSE"),
        // Giving unrefutation
        List.of("changePlayer", "Percy"),
        //
        List.of("displayCardTriple", "Your guess wasn't refuted:",
            logCards(EstateCard.HAUNTED_HOUSE, WeaponCard.KNIFE, CharacterCard.MALINA)))
        .toString();

    TestData data = new TestData(
        // Players
        List.of(MockPlayerInfo.LUCILLA, MockPlayerInfo.MALINA),
        List.of(Set.of(CharacterCard.PERCY, EstateCard.VISITATION_VILLA, WeaponCard.IPAD),
            Set.of(CharacterCard.LUCILLA, WeaponCard.BROOM, WeaponCard.SCISSORS,
                WeaponCard.SHOVEL)),
        // Guesser
        MockPlayerInfo.PERCY, Locations.HAUNTED_HOUSE,
        // Prompt responses
        List.of(WeaponCard.KNIFE, CharacterCard.MALINA));
    RefutationController rc = new RefutationController(data.game, data.guesser);
    assertEquals(expectedLog, data.log.toString());
    assertEquals(
        Optional
            .of(new CardTriple(CharacterCard.MALINA, EstateCard.HAUNTED_HOUSE, WeaponCard.KNIFE)),
        rc.unrefutedGuess());
  }
}
