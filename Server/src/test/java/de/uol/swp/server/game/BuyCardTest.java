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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BuyCardTest {
    static final User defaultOwner = new UserDTO("test1", "test1", "test1@test.de");
    static final User secondPlayer = new UserDTO("test2", "test2", "test2@test2.de");
    static final User thirdPlayer = new UserDTO("test3", "test3", "test3@test3.de");
    static final EventBus bus = new EventBus();
    static final Card card = new ValueCard("provinz", (short) 10);
    static final ChatManagement chatManagement = new ChatManagement();
    static final LobbyManagement lobbyManagement = new LobbyManagement();
    static final GameManagement gameManagement = new GameManagement(chatManagement, lobbyManagement);
    static final AuthenticationService authenticationService = new AuthenticationService(bus, new UserManagement(new MainMemoryBasedUserStore()));
    static final GameService gameService = new GameService(bus, gameManagement, authenticationService);
    
    static UUID gameID;
    private final CountDownLatch lock = new CountDownLatch(1);
    private Object event;

    void init() {
        gameID = lobbyManagement.createLobby("Test", "", defaultOwner);
        chatManagement.createChat(gameID.toString());
        lobbyManagement.getLobby(gameID).get().joinUser(secondPlayer);
        lobbyManagement.getLobby(gameID).get().joinUser(thirdPlayer);
        bus.post(new StartGameInternalMessage(gameID));
    }

    /**
     * Setzt vor jedem Test das aktuelle Event auf null und registriert diese Testklasse auf dem Eventbus
     *
     * @author Paula
     * @since Sprint6
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
     * @author Paula
     * @since Sprint6
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
     * @since Sprint6
     */
    @Subscribe
    void handle(DeadEvent e) {
        this.event = e.getEvent();
        System.out.print(e.getEvent());
        lock.countDown();
    }

    @AfterEach
    void afterEach() {
        gameManagement.deleteGame(gameID);
        lobbyManagement.dropLobby(gameID);
        chatManagement.deleteChat(gameID.toString());
    }

    /**
     * Testet, ob sich die Anzahl der Karten auf dem Playground nach dem Kauf aktualisiert.
     *
     * @author Paula
     * @since Sprint6
     */
    @Test
    void testIfCardOnPlayGroundIsActualAfterBuyingACard() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
        int cardsOnPlaygoundAfterBuying = playground.getCompositePhase().executeBuyPhase(playground.getActualPlayer(), (short) 10);
        assertTrue(Playground.getCardField().get(card.getId()).equals(9));
    }

    /**
     * Testet, ob eine gekaufte Karte dem Ablagestapel des Spielers hinzugefügt wird
     *
     * @author Paula
     * @since Sprint6
     */

    @Test
    void testIfCardIsAddedToDiscardPile() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
        int CardsOnDiscardPile = playground.getActualPlayer().getPlayerDeck().getDiscardPile().size();
        int BuyingCard = playground.getCompositePhase().executeBuyPhase(playground.getActualPlayer(), (short) 10);
        assertEquals(CardsOnDiscardPile + 1, playground.getActualPlayer().getPlayerDeck().getDiscardPile().size());

    }

    /**
     * Test, ob Geld auch abgezogen wird
     *
     * @author Paula
     * @since Sprint6
     */
    @Test
    void testIfMoneyIsSubstracted() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
        int moneyPlayer = playground.getActualPlayer().getPlayerDeck().actualMoneyFromPlayer();
        int BuyingCard = playground.getCompositePhase().executeBuyPhase(playground.getActualPlayer(), (short) 10);
        assertEquals(moneyPlayer - 2, playground.getActualPlayer().getPlayerDeck().actualMoneyFromPlayer());
    }


}