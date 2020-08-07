package de.uol.swp.server.game;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.card.ValueCard;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.message.StartGameInternalMessage;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testklasse der BuyCard
 *
 * @author Paula
 * @since Sprint 6
 */
@SuppressWarnings("UnstableApiUsage")
public class BuyCardTest {
    static final User defaultOwner = new UserDTO("test1", "test1", "test1@test.de");
    static final User secondPlayer = new UserDTO("test2", "test2", "test2@test2.de");
    static final User thirdPlayer = new UserDTO("test3", "test3", "test3@test3.de");
    static final EventBus bus = new EventBus();
    static final Card card = new ValueCard("provinz", (short) 10, (short) 2, (short) 2);
    static final ChatManagement chatManagement = new ChatManagement();
    static final LobbyManagement lobbyManagement = new LobbyManagement();
    static final GameManagement gameManagement = new GameManagement(chatManagement, lobbyManagement);
    static final AuthenticationService authenticationService = new AuthenticationService(bus, new UserManagement(new MainMemoryBasedUserStore()), lobbyManagement);
    static final GameService gameService = new GameService(bus, gameManagement, authenticationService);
    private final ArrayList<Short> chosenCards = new ArrayList<>();

    static UUID gameID;
    private final CountDownLatch lock = new CountDownLatch(1);

    /**
     * Initialisiert die benötigten Objekte/Parameter
     *
     * @author Paula
     * @since Sprint 6
     */
    void init() {
        try {
            gameID = lobbyManagement.createLobby("Test", "", defaultOwner);
            chatManagement.createChat(gameID.toString());
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(secondPlayer);
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(thirdPlayer);
            chosenCards.add((short) 10);
            lobbyManagement.getLobby(gameID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).setChosenCards(chosenCards);
            bus.post(new StartGameInternalMessage(gameID));
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Löscht das Spiel, die Lobby und den Chat nach jedem Testdurchlauf
     *
     * @author Paula
     * @since Sprint 6
     */
    @AfterEach
    void afterEach() {
        gameManagement.deleteGame(gameID);
        lobbyManagement.dropLobby(gameID);
        chatManagement.deleteChat(gameID.toString());
    }

    /**
     * Setzt vor jedem Test das aktuelle Event auf null und registriert diese Testklasse auf dem Eventbus
     *
     * @author Paula
     * @since Sprint 6
     */
    @BeforeEach
    void registerBus() {
        bus.register(this);
        init();
    }

    /**
     * Meldet diese Testklasse nach jedem Test vom Eventbus ab
     *
     * @author Paula
     * @since Sprint 6
     */
    @AfterEach
    void deregisterBus() {
        bus.unregister(this);
    }

    /**
     * Bei Auftreten eines DeadEvents wird dieses ausgegeben und der CountDownLatch wird um eins verringert
     *
     * @param e das DeadEvent
     * @author Paula
     * @since Sprint 6
     */
    @Subscribe
    void handle(DeadEvent e) {
        System.out.print(e.getEvent());
        lock.countDown();
    }

    /**
     * Testet, ob eine gekaufte Karte dem Ablagestapel des Spielers hinzugefügt wird
     *
     * @author Paula, Ferit
     * @since Sprint 6
     */
    @Test
    void testIfCardIsAddedToDiscardPile() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            playground.getActualPlayer().setAvailableBuys(2);
            playground.getCompositePhase().executeBuyPhase(playground.getActualPlayer(), (short) 10);
            assertEquals(3, playground.getActualPlayer().getPlayerDeck().getDiscardPile().size());
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Testet, ob sich die Anzahl der Karten auf dem Playground nach dem Kauf aktualisiert.
     *
     * @author Paula, Ferit
     * @since Sprint 6
     */
    @Test
    void testIfCardOnPlayGroundIsActualAfterBuyingACard() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            playground.getCompositePhase().executeBuyPhase(playground.getActualPlayer(), (short) 10);
            assertTrue(playground.getCardField().get(card.getId()).equals(9));
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }
}