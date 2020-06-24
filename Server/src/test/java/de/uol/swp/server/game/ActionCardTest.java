package de.uol.swp.server.game;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.phase.Phase;
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

import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testet Aktionskarten, die keine Spielerantwort benötigen
 *
 * @author Julia
 * @since Sprint 8
 */
@SuppressWarnings("UnstableApiUsage")
public class ActionCardTest {

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
    static UUID gameID;
    private final CountDownLatch lock = new CountDownLatch(1);
    private Object event;

    /**
     * Initialisiert alle benötigten Objekte/Parameter
     *
     * @author Julia
     * @since Sprint 8
     */
    @BeforeAll
    static void init() {
        try {
            gameID = lobbyManagement.createLobby("Test", "", defaultOwner);
            chatManagement.createChat(gameID.toString());
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht gefunden")).joinUser(secondPlayer);
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht gefunden")).joinUser(thirdPlayer);
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
     * Testet die Karte Holzfäller
     *
     * @author Julia
     * @since Sprint 8
     */
    @Test
    void testHolzfaeller() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht gefunden")).getPlayground();
            playground.setActualPhase(Phase.Type.ActionPhase);
            playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getCardForId((short) 9));
            playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 9);
            assertEquals(2, playground.getActualPlayer().getAvailableBuys());
            assertEquals(2, playground.getActualPlayer().getAdditionalMoney());
            assertEquals(Phase.Type.BuyPhase, playground.getActualPhase());
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet die Karte Jahrmarkt
     *
     * @author Julia
     * @since Sprint 8
     */
    @Test
    void testJahrmarkt() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht gefunden")).getPlayground();
            playground.setActualPhase(Phase.Type.ActionPhase);
            playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getCardForId((short) 27));
            playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 27);
            assertEquals(2, playground.getActualPlayer().getAvailableBuys());
            assertEquals(2, playground.getActualPlayer().getAvailableActions());
            assertEquals(2, playground.getActualPlayer().getAdditionalMoney());
            if (playground.checkForActionCard()) {
                assertEquals(Phase.Type.ActionPhase, playground.getActualPhase());
            } else {
                assertEquals(Phase.Type.BuyPhase, playground.getActualPhase());
            }
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet die Karte Dorf
     *
     * @author Julia
     * @since Sprint 8
     */
    @Test
    void testDorf() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht gefunden")).getPlayground();
            playground.setActualPhase(Phase.Type.ActionPhase);
            playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getCardForId((short) 8));
            int handSize = playground.getActualPlayer().getPlayerDeck().getHand().size();
            Card card = playground.getActualPlayer().getPlayerDeck().getCardsDeck().get(0);
            playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 8);
            assertEquals(2, playground.getActualPlayer().getAvailableActions());
            assertEquals(handSize, playground.getActualPlayer().getPlayerDeck().getHand().size());
            assertTrue(playground.getActualPlayer().getPlayerDeck().getHand().contains(card));
            if (playground.checkForActionCard()) {
                assertEquals(Phase.Type.ActionPhase, playground.getActualPhase());
            } else {
                assertEquals(Phase.Type.BuyPhase, playground.getActualPhase());
            }
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet die Karte Schmiede
     *
     * @author Julia
     * @since Sprint 8
     */
    @Test
    void testSchmiede() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht gefunden")).getPlayground();
            playground.setActualPhase(Phase.Type.ActionPhase);
            playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getCardForId((short) 14));
            int handSize = playground.getActualPlayer().getPlayerDeck().getHand().size();
            Card card1 = playground.getActualPlayer().getPlayerDeck().getCardsDeck().get(0);
            Card card2 = playground.getActualPlayer().getPlayerDeck().getCardsDeck().get(1);
            Card card3 = playground.getActualPlayer().getPlayerDeck().getCardsDeck().get(2);
            playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 14);
            assertEquals(handSize + 2, playground.getActualPlayer().getPlayerDeck().getHand().size());
            assertTrue(playground.getActualPlayer().getPlayerDeck().getHand().contains(card1));
            assertTrue(playground.getActualPlayer().getPlayerDeck().getHand().contains(card2));
            assertTrue(playground.getActualPlayer().getPlayerDeck().getHand().contains(card3));
            assertEquals(Phase.Type.BuyPhase, playground.getActualPhase());
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet die Karte Labaratorium
     *
     * @author Julia
     * @since Sprint 8
     */
    @Test
    void testLaboratorium() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht gefunden")).getPlayground();
            playground.setActualPhase(Phase.Type.ActionPhase);
            playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getCardForId((short) 23));
            int handSize = playground.getActualPlayer().getPlayerDeck().getHand().size();
            Card card1 = playground.getActualPlayer().getPlayerDeck().getCardsDeck().get(0);
            Card card2 = playground.getActualPlayer().getPlayerDeck().getCardsDeck().get(1);
            playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 23);
            assertEquals(handSize + 1, playground.getActualPlayer().getPlayerDeck().getHand().size());
            assertEquals(1, playground.getActualPlayer().getAvailableActions());
            assertTrue(playground.getActualPlayer().getPlayerDeck().getHand().contains(card1));
            assertTrue(playground.getActualPlayer().getPlayerDeck().getHand().contains(card2));
            if (playground.checkForActionCard()) {
                assertEquals(Phase.Type.ActionPhase, playground.getActualPhase());
            } else {
                assertEquals(Phase.Type.BuyPhase, playground.getActualPhase());
            }
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet die Karte Markt
     *
     * @author Julia
     * @since Sprint 8
     */
    @Test
    void testMarkt() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht gefunden")).getPlayground();
            playground.setActualPhase(Phase.Type.ActionPhase);
            playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getCardForId((short) 11));
            int handSize = playground.getActualPlayer().getPlayerDeck().getHand().size();
            Card card1 = playground.getActualPlayer().getPlayerDeck().getCardsDeck().get(0);
            playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 11);
            assertEquals(handSize, playground.getActualPlayer().getPlayerDeck().getHand().size());
            assertEquals(1, playground.getActualPlayer().getAvailableActions());
            assertEquals(2, playground.getActualPlayer().getAvailableBuys());
            assertEquals(1, playground.getActualPlayer().getAdditionalMoney());
            assertTrue(playground.getActualPlayer().getPlayerDeck().getHand().contains(card1));
            if (playground.checkForActionCard()) {
                assertEquals(Phase.Type.ActionPhase, playground.getActualPhase());
            } else {
                assertEquals(Phase.Type.BuyPhase, playground.getActualPhase());
            }
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet die Karte Burggraben
     *
     * @author Julia
     * @since Sprint 10
     */
    @Test
    void testBurggraben() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
        playground.setActualPhase(Phase.Type.ActionPhase);
        playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getCardForId((short) 7));
        int handSize = playground.getActualPlayer().getPlayerDeck().getHand().size();
        playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 7);
        assertEquals(handSize + 1, playground.getActualPlayer().getPlayerDeck().getHand().size());
        if (playground.checkForActionCard()) {
            assertEquals(Phase.Type.ActionPhase, playground.getActualPhase());
        } else {
            assertEquals(Phase.Type.BuyPhase, playground.getActualPhase());
        }
    }

    /**
     * Testet die Karte Hexe
     *
     * @author Julia
     * @since Sprint 10
     */
    @Test
    void testHexe() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
        playground.setActualPhase(Phase.Type.ActionPhase);
        playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getCardForId((short) 31));
        int handSize = playground.getActualPlayer().getPlayerDeck().getHand().size();
        Card reactionCard = playground.getCardsPackField().getCards().getCardForId((short) 7);
        playground.getNextPlayer().getPlayerDeck().getHand().add(reactionCard);
        int handSizeNextPlayer = playground.getNextPlayer().getPlayerDeck().getHand().size();
        playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 31);
        assertEquals(handSize + 1, playground.getActualPlayer().getPlayerDeck().getHand().size());
        assertEquals(handSizeNextPlayer, playground.getNextPlayer().getPlayerDeck().getHand().size());
        for (Player p : playground.getPlayers()) {
            if (!p.equals(playground.getActualPlayer()) && !p.equals(playground.getNextPlayer())) {
                assertEquals(38, p.getPlayerDeck().getHand().get(p.getPlayerDeck().getHand().size() - 1).getId());
            }
        }
        if (playground.checkForActionCard()) {
            assertEquals(Phase.Type.ActionPhase, playground.getActualPhase());
        } else {
            assertEquals(Phase.Type.BuyPhase, playground.getActualPhase());
        }
    }

    /**
     * Testet die Karte Ratsversammlung
     *
     * @author Julia
     * @since Sprint 10
     */
    @Test
    void testRatsversammlung() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
        playground.setActualPhase(Phase.Type.ActionPhase);
        playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getCardForId((short) 28));
        int handSize = playground.getActualPlayer().getPlayerDeck().getHand().size();
        playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 28);
        assertEquals(handSize + 3, playground.getActualPlayer().getPlayerDeck().getHand().size());
        assertEquals(2, playground.getActualPlayer().getAvailableBuys());
        for (Player p : playground.getPlayers()) {
            if (!p.equals(playground.getActualPlayer())) {
                assertEquals(6, p.getPlayerDeck().getHand().size());
            }
        }
        if (playground.checkForActionCard()) {
            assertEquals(Phase.Type.ActionPhase, playground.getActualPhase());
        } else {
            assertEquals(Phase.Type.BuyPhase, playground.getActualPhase());
        }
    }
}
