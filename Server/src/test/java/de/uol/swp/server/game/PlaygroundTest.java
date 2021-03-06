package de.uol.swp.server.game;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.parser.components.CardPack;
import de.uol.swp.common.game.exception.GamePhaseException;
import de.uol.swp.common.game.phase.Phase;
import de.uol.swp.common.game.request.BuyCardRequest;
import de.uol.swp.common.game.request.GameGiveUpRequest;
import de.uol.swp.common.game.request.PlayCardRequest;
import de.uol.swp.common.game.request.SkipPhaseRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.game.player.Player;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.message.StartGameInternalMessage;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import de.uol.swp.server.usermanagement.store.UserStore;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse für den Playground
 *
 * @author Julia
 * @since Sprint 5
 */
@SuppressWarnings("UnstableApiUsage")
public class PlaygroundTest {

    static final User defaultOwner = new UserDTO("test1", "test1", "test1@test.de");
    static final User secondPlayer = new UserDTO("test2", "test2", "test2@test2.de");
    static final User thirdPlayer = new UserDTO("test3", "test3", "test3@test3.de");
    static final ChatManagement chatManagement = new ChatManagement();
    static final LobbyManagement lobbyManagement = new LobbyManagement();
    static final GameManagement gameManagement = new GameManagement(chatManagement, lobbyManagement);
    static final UserStore userStore = new MainMemoryBasedUserStore();
    static final EventBus bus = new EventBus();
    static final UserManagement userManagement = new UserManagement(userStore);
    static final AuthenticationService authService = new AuthenticationService(bus, userManagement, lobbyManagement);
    static final GameService gameService = new GameService(bus, gameManagement, authService);
    private final CountDownLatch lock = new CountDownLatch(1);
    private static final ArrayList<Short> chosenCards = new ArrayList<>();

    static UUID gameID;
    private Object event;

    /**
     * Initialisiert die benötigten Objekte/Parameter
     *
     * @author Julia
     * @since Sprint 5
     */
    @BeforeAll
    static void init() {
        try {
            gameID = lobbyManagement.createLobby("Test", "", defaultOwner);
            chatManagement.createChat(gameID.toString());
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(secondPlayer);
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(thirdPlayer);
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).setChosenCards(chosenCards);
            bus.post(new StartGameInternalMessage(gameID));
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Bei Auftreten eines DeadEvents wird dieses ausgegeben und der CountDownLatch wird um eins verringert
     *
     * @param e das DeadEvent
     * @author Marco
     * @since Start
     */
    @Subscribe
    void handle(DeadEvent e) {
        this.event = e.getEvent();
        System.out.print(e.getEvent());
        lock.countDown();
    }

    /**
     * Setzt vor jedem Test das aktuelle Event auf null und registriert diese Testklasse auf dem Eventbus
     *
     * @author Marco
     * @since Start
     */
    @BeforeEach
    void registerBus() {
        init();
        event = null;
        bus.register(this);
    }

    /**
     * Meldet diese Testklasse nach jedem Test vom Eventbus ab
     *
     * @author Marco
     * @since Start
     */
    @AfterEach
    void deregisterBus() {
        bus.unregister(this);
    }

    /**
     * Testet ob actual- und nextPlayer korrekt aktualisiert werden
     *
     * @author Julia
     * @since Sprint 5
     */
    @Test
    void testNewTurn() {
        Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
        int actual = playground.getPlayers().indexOf(playground.getActualPlayer());
        int next = playground.getPlayers().indexOf(playground.getNextPlayer());
        //Player an erster Position der Liste beginnt
        assertEquals(0, actual);
        assertEquals(1, next);
        assertEquals(1, playground.getPlayerTurns().get(playground.getActualPlayer()));

        playground.setActualPhase(Phase.Type.ClearPhase);
        playground.newTurn();
        actual = playground.getPlayers().indexOf(playground.getActualPlayer());
        next = playground.getPlayers().indexOf(playground.getNextPlayer());
        assertEquals(1, actual);
        assertEquals(2, next);
        assertEquals(1, playground.getPlayerTurns().get(playground.getActualPlayer()));

        playground.setActualPhase(Phase.Type.ClearPhase);
        playground.newTurn();
        actual = playground.getPlayers().indexOf(playground.getActualPlayer());
        next = playground.getPlayers().indexOf(playground.getNextPlayer());
        assertEquals(2, actual);
        assertEquals(0, next);
        assertEquals(1, playground.getPlayerTurns().get(playground.getActualPlayer()));

        playground.setActualPhase(Phase.Type.ClearPhase);
        playground.newTurn();
        assertEquals(2, playground.getPlayerTurns().get(playground.getActualPlayer()));
    }

    /**
     * Testet die checkForActionCard - Methode
     *
     * @author Julia
     * @since Sprint 5
     */
    @Test
    void testCheckForActionCard() {
        Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
        //Bei Spielbeginn hat der Spieler keine Aktionskarten auf der Hand
        assertFalse(playground.checkForActionCard());
        Card actionCard = playground.getCardsPackField().getCards().getActionCards().get(0);
        playground.getActualPlayer().getPlayerDeck().getHand().add(actionCard);
        assertTrue(playground.checkForActionCard());
    }

    /**
     * Testet, ob korrekt zur nächsten Phase gewechselt wird
     *
     * @author Julia
     * @since Sprint 5
     */
    @Test
    void testNextPhase() {
        Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
        playground.setActualPhase(Phase.Type.ActionPhase);
        playground.nextPhase();
        assertEquals(Phase.Type.BuyPhase, playground.getActualPhase());
        playground.nextPhase();
        if (playground.checkForActionCard()) {
            assertEquals(Phase.Type.ActionPhase, playground.getActualPhase());
        } else {
            assertEquals(Phase.Type.BuyPhase, playground.getActualPhase());
        }

        playground.setActualPhase(Phase.Type.ClearPhase);
        assertThrows(GamePhaseException.class, playground::nextPhase);
    }

    /**
     * Testet, ob Requests ankommen und alles richtig durchlaufen wird und ein Spieler entfernt worden ist.
     *
     * @author Ferit
     * @since Sprint 6
     */
    @Test
    void playerGaveUpTest() {
        UUID spielID = gameID;
        GameGiveUpRequest testRequest = new GameGiveUpRequest((UserDTO) secondPlayer, spielID);
        bus.post(testRequest);
        assertEquals(2, gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground().getPlayers().size());
    }

    /**
     * Testet, ob der spezifizierte Spieler der Aufgeben will, nach Aufgabe noch im Game befindet.
     *
     * @author Ferit
     * @since Sprint 6
     */
    @Test
    void specificPlayerGaveUpTest() {
        GameGiveUpRequest testRequest = new GameGiveUpRequest((UserDTO) secondPlayer, gameID);
        bus.post(testRequest);
        assertFalse(gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground().getPlayers().contains(secondPlayer.getUsername()));
    }

    /**
     * Testet, ob der mit den meisten Punkten gewinnt
     *
     * @author Julia
     * @since Sprint 6
     */
    @Test
    void calculateWinnerHighestScoreTest() {
        Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
        CardPack cardsPackField = playground.getCardsPackField();
        Card card = cardsPackField.getCards().getValueCards().get(2);
        playground.getActualPlayer().getPlayerDeck().getCardsDeck().add(card);
        List<String> winners = playground.calculateWinners();
        assertEquals(1, winners.size());
        assertTrue(winners.contains(playground.getActualPlayer().getPlayerName()));
    }

    /**
     * Testet, ob bei Punktegleichstand der mit den wenigsten Zügen gewinnt
     *
     * @author Julia
     * @since Sprint 6
     */
    @Test
    void calculateWinnerFewestTurnsTest() {
        Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
        List<String> winners = playground.calculateWinners();
        assertEquals(2, winners.size());
        assertFalse(winners.contains(playground.getActualPlayer().getPlayerName()));
        playground.setActualPhase(Phase.Type.ClearPhase);
        playground.newTurn();
        winners = playground.calculateWinners();
        assertEquals(1, winners.size());
        assertTrue(winners.contains(playground.getNextPlayer().getPlayerName()));
    }

    /**
     * Testet ob die korrekte Anzahl an Karten auf dem Nachziehstapel gesendet wird
     *
     * @author Julia
     * @since Sprint 7
     */
    @Test
    void cardsDeckSizeTest() {
        Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
        int size = playground.sendCardsDeckSize();
        assertEquals(5, size);
        playground.getCompositePhase().executeClearPhase(playground.getActualPlayer());
        size = playground.sendCardsDeckSize();
        assertEquals(0, size);
        playground.getCompositePhase().executeClearPhase(playground.getActualPlayer());
        size = playground.sendCardsDeckSize();
        assertEquals(5, size);
    }
    /**
     * Testet, ob ein beendetes Spiel gelöscht wird.
     * @author Ferit
     * @since Sprint 11
     */
    @Test
    void dropFinishedGameTest() {
        gameService.dropFinishedGame(gameID);
        assertFalse(gameManagement.getGame(gameID).isPresent());
    }
    /**
     * SkipPhaseRequestTest
     * @author Ferit
     * @since Sprint 11
     */
    @Test
    void onSkipPhaseRequestTest() {
        Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
        Player oldActualPlayer = playground.getActualPlayer();
        gameService.onSkipPhaseRequest(new SkipPhaseRequest(playground.getActualPlayer().getTheUserInThePlayer(), gameID));
        assertNotEquals(playground.getActualPlayer(), oldActualPlayer);
    }

    /**
     * OnBuyCardRequestTest
     * @author Ferit
     * @since Sprint 11
     */
    @Test
    void onBuyCardRequestTest() {
        if (gameManagement.getGame(gameID).isPresent()) {
            gameManagement.getGame(gameID).get().getPlayground().playerGaveUp(gameID, (UserDTO) thirdPlayer, true);
            if (gameManagement.getGame(gameID).get().getPlayground().getActualPlayer().getPlayerDeck().actualMoneyFromPlayer() >= 1) {
                if (gameManagement.getGame(gameID).get().getPlayground().getActualPlayer().getAvailableActions() >= 1) {
                    gameManagement.getGame(gameID).get().getPlayground().nextPhase();
                }
                gameService.onBuyCardRequest(new BuyCardRequest(gameID, gameManagement.getGame(gameID).get().getPlayground().getActualPlayer().getTheUserInThePlayer(), (short) 1));

            }
            assertEquals(1, gameManagement.getGame(gameID).get().getPlayground().getNextPlayer().getPlayerDeck().getDiscardPile().get(0).getId());
        } else {
            throw new NoSuchElementException("Spiel nicht vorhanden");
        }
    }

    /**
     * OnPlayCardTest
     * @author Ferit
     * @since Sprint 11
     */
    @Test
    void onPlayCardTest() {
        if (gameManagement.getGame(gameID).isPresent()) {
            gameManagement.getGame(gameID).get().getPlayground().setActualPhase(Phase.Type.ActionPhase);
            gameManagement.getGame(gameID).get().getPlayground().getActualPlayer().getPlayerDeck().getHand().add(gameManagement.getGame(gameID).get().getPlayground().getCardsPackField().getCards().getCardForId((short) 9));
            gameService.onPlayCardRequest(new PlayCardRequest(gameID, gameManagement.getGame(gameID).get().getPlayground().getActualPlayer().getTheUserInThePlayer(), (short) 9));
            assertEquals(2, gameManagement.getGame(gameID).get().getPlayground().getActualPlayer().getAvailableBuys());
            assertEquals(2, gameManagement.getGame(gameID).get().getPlayground().getActualPlayer().getAdditionalMoney());
            assertEquals(Phase.Type.BuyPhase, gameManagement.getGame(gameID).get().getPlayground().getActualPhase());
        } else {
            throw new NoSuchElementException("Spiel nicht vorhanden");
        }
    }
}