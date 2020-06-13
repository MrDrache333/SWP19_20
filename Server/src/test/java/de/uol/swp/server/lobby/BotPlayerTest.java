package de.uol.swp.server.lobby;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.lobby.request.AddBotRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.game.GameManagement;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Testklasse des Botplayers
 *
 * @author Ferit
 * @since Sprint 8
 */
class BotPlayerTest {

    static final User lobbyOwner = new UserDTO("Marco", "Marco", "Marco@Grawunder.com");
    static final String defaultLobbyName = "Lobby";
    static final String defaultLobbyPassword = "Lobby";
    final EventBus bus = new EventBus();
    final UserManagement userManagement = new UserManagement(new MainMemoryBasedUserStore());
    final LobbyManagement lobbyManagement = new LobbyManagement();
    final LobbyService lobbyService = new LobbyService(lobbyManagement, new AuthenticationService(bus, userManagement, lobbyManagement), new ChatManagement(), bus);
    private UUID lobbyID;
    final GameManagement gameManagement = new GameManagement(new ChatManagement(), lobbyManagement);
    private final CountDownLatch lock = new CountDownLatch(1);
    private Object event;

    /**
     * Definiert den Umgang mit Dead-Events
     *
     * @author Ferit
     * @since Sprint 8
     */
    @Subscribe
    void handle(DeadEvent e) {
        this.event = e.getEvent();
        System.out.print(e.getEvent());
        lock.countDown();
    }

    /**
     * Registriert den EventBus
     *
     * @author Ferit
     * @since Sprint 8
     */
    @BeforeEach
    void registerBus() {
        event = null;
        bus.register(this);
    }

    /**
     * Deregistriert den EventBus
     *
     * @author Ferit
     * @since Sprint 8
     */
    @AfterEach
    void deregisterBus() {
        bus.unregister(this);
    }

    /**
     * Vor jedem Test wird eine Lobby erstellt.
     *
     * @author Julia, Marvin
     * @since Sprint 3
     */
    @BeforeEach
    void createLobby() {
        lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, lobbyOwner);
    }

    /**
     * Löscht eine Lobby
     *
     * @author Ferit
     * @since Sprint 8
     */
    @AfterEach
    void dropLobby() {
        lobbyManagement.dropLobby(lobbyID);
    }

    /**
     * Erzeugt einen BotPlayer
     *
     * @author Ferit
     * @since Sprint 8
     */
    @Test
    void createBotPlayertest() {
        AddBotRequest newReq = new AddBotRequest(lobbyID);
        bus.post(newReq);
        assertTrue(lobbyManagement.getLobby(lobbyID).get().getPlayers() == 2);
    }

    /**
     * Prüft ob der Bot bereit ist
     *
     * @author Ferit
     * @since Sprint 8
     */
    @Test
    void botIsReadyTest() {
        AddBotRequest newReq = new AddBotRequest(lobbyID);
        bus.post(newReq);

        ArrayList<User> theUser = new ArrayList<>();
        for (User user : lobbyManagement.getLobby(lobbyID).get().getUsers()) {
            theUser.add(user);
        }
        User theBotPlayer;
        if (theUser.get(0).getIsBot() == true) {
            theBotPlayer = theUser.get(0);
        } else {
            theBotPlayer = theUser.get(1);
        }
        assertTrue(lobbyManagement.getLobby(lobbyID).get().getReadyStatus(theBotPlayer) == true);
    }
    // TODO: Weitere Tests implementieren, wenn die Botlogik ausgebaut wird.
}