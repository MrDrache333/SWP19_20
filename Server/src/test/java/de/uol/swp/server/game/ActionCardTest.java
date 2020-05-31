package de.uol.swp.server.game;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.game.card.Card;
import de.uol.swp.common.game.phase.Phase;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.message.StartGameInternalMessage;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import de.uol.swp.server.usermanagement.store.UserStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    static UUID id;
    static UUID gameID;
    private final CountDownLatch lock = new CountDownLatch(1);
    private Object event;

    @BeforeAll
    static void init() {
        gameID = lobbyManagement.createLobby("Test", "", defaultOwner);
        chatManagement.createChat(gameID.toString());
        lobbyManagement.getLobby(gameID).get().joinUser(secondPlayer);
        lobbyManagement.getLobby(gameID).get().joinUser(thirdPlayer);
        bus.post(new StartGameInternalMessage(gameID));
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

    @Test
    void testHolzfäller() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
        playground.setActualPhase(Phase.Type.ActionPhase);
        playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getCardForId((short) 9));
        playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 9);
        assertEquals(2, playground.getActualPlayer().getAvailableBuys());
        assertEquals(2, playground.getActualPlayer().getAdditionalMoney());
        assertEquals(Phase.Type.Buyphase, playground.getActualPhase());
    }

    @Test
    void testJahrmarkt() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
        playground.setActualPhase(Phase.Type.ActionPhase);
        playground.getActualPlayer().getPlayerDeck().getHand().add(playground.getCardsPackField().getCards().getCardForId((short) 27));
        playground.getCompositePhase().executeActionPhase(playground.getActualPlayer(), (short) 27);
        assertEquals(2, playground.getActualPlayer().getAvailableBuys());
        assertEquals(2, playground.getActualPlayer().getAvailableActions());
        assertEquals(2, playground.getActualPlayer().getAdditionalMoney());
        if (playground.checkForActionCard()) {
            assertEquals(Phase.Type.ActionPhase, playground.getActualPhase());
        } else {
            assertEquals(Phase.Type.Buyphase, playground.getActualPhase());
        }
    }

    @Test
    void testDorf() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
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
            assertEquals(Phase.Type.Buyphase, playground.getActualPhase());
        }
    }

    @Test
    void testSchmiede() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
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
        assertEquals(Phase.Type.Buyphase, playground.getActualPhase());
    }

    @Test
    void testLaboratorium() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
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
            assertEquals(Phase.Type.Buyphase, playground.getActualPhase());
        }
    }

    @Test
    void testMarkt() {
        Playground playground = gameManagement.getGame(gameID).get().getPlayground();
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
            assertEquals(Phase.Type.Buyphase, playground.getActualPhase());
        }
    }
}
