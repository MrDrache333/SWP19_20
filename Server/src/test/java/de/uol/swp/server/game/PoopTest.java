package de.uol.swp.server.game;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.game.card.parser.components.CardAction.response.ChooseCardResponse;
import de.uol.swp.common.game.messages.CountdownRefreshMessage;
import de.uol.swp.common.game.messages.PoopBreakMessage;
import de.uol.swp.common.game.messages.StartPoopBreakMessage;
import de.uol.swp.common.game.phase.Phase;
import de.uol.swp.common.game.request.PoopBreakRequest;
import de.uol.swp.common.lobby.message.CreateLobbyMessage;
import de.uol.swp.common.lobby.request.AddBotRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.lobby.LobbyManagement;
import de.uol.swp.server.message.StartGameInternalMessage;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import de.uol.swp.server.usermanagement.store.UserStore;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testklasse für dem PoopTimer
 */
public class PoopTest {

    static final User defaultOwner = new UserDTO("test1", "test1", "test1@test.de");
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
     * @author Fenja, Ferit
     * @since Sprint 10
     */
    static void init() {
        try {
            gameID = lobbyManagement.createLobby("Test", "", defaultOwner);
            chatManagement.createChat(gameID.toString());
            AddBotRequest addBotRequest = new AddBotRequest(gameID);
            bus.post(addBotRequest);
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
     * Testet den Pooptimer, indem eine PoopBreakRequest erstellt wird und abgefragt wird,
     * ob eine StartPoopBreakMessage gesendet wird.
     *
     * @author Fenja, Ferit
     * @since Sprint 10
     */
    @Test
    void poopMessageTest() {
        try {
            Playground playground = gameManagement.getGame(gameID).orElseThrow(() -> new NoSuchElementException("Spiel nicht existent")).getPlayground();
            PoopBreakRequest poopBreakRequest = new PoopBreakRequest(defaultOwner, gameID);
            bus.post(poopBreakRequest);
            assertTrue(event instanceof StartPoopBreakMessage);
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }
}
