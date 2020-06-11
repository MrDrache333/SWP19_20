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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings("UnstableApiUsage")
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

    @Subscribe
    void handle(DeadEvent e) {
        this.event = e.getEvent();
        System.out.print(e.getEvent());
        lock.countDown();
    }

    @BeforeEach
    void registerBus() {
        event = null;
        bus.register(this);
    }

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

    @AfterEach
    void dropLobby() {
        lobbyManagement.dropLobby(lobbyID);
    }

    @Test
    void createBotPlayertest() {
        if (lobbyManagement.getLobby(lobbyID).isPresent()) {
            AddBotRequest newReq = new AddBotRequest(lobbyID);
            bus.post(newReq);
            assertEquals(2, lobbyManagement.getLobby(lobbyID).get().getPlayers());
        }
    }

    @Test
    void botIsReadyTest() {
        if (lobbyManagement.getLobby(lobbyID).isPresent()) {
            AddBotRequest newReq = new AddBotRequest(lobbyID);
            bus.post(newReq);

            ArrayList<User> theUser;
            theUser = new ArrayList<>(lobbyManagement.getLobby(lobbyID).get().getUsers());

            User theBotPlayer;
            if (theUser.get(0).getIsBot()) {
                theBotPlayer = theUser.get(0);
            } else {
                theBotPlayer = theUser.get(1);
            }
            assertTrue(lobbyManagement.getLobby(lobbyID).get().getReadyStatus(theBotPlayer));
        }
    }


    // TODO: Weitere Tests implementieren, wenn die Botlogik ausgebaut wird.
}