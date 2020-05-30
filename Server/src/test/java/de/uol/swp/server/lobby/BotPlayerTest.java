package de.uol.swp.server.lobby;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.lobby.request.AddBotRequest;
import de.uol.swp.common.lobby.request.UpdateLobbyReadyStatusRequest;
import de.uol.swp.common.message.RequestMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.server.chat.ChatManagement;
import de.uol.swp.server.game.Game;
import de.uol.swp.server.game.GameManagement;
import de.uol.swp.server.game.player.bot.BotPlayer;
import de.uol.swp.server.usermanagement.AuthenticationService;
import de.uol.swp.server.usermanagement.UserManagement;
import de.uol.swp.server.usermanagement.store.MainMemoryBasedUserStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.message.UpdatedLobbyReadyStatusMessage;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

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
    private CountDownLatch lock = new CountDownLatch(1);
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
        AddBotRequest newReq = new AddBotRequest(lobbyID);
        bus.post(newReq);
        assertTrue(lobbyManagement.getLobby(lobbyID).get().getPlayers() == 2);
    }

    @Test
    void botIsReadyTest() {
        AddBotRequest newReq = new AddBotRequest(lobbyID);
        bus.post(newReq);
        ArrayList<User> theUser = new ArrayList<>();
        for (User user : lobbyManagement.getLobby(lobbyID).get().getUsers()) {
            theUser.add(user);
        }
        User theBotPlayer = theUser.get(1);
        assertTrue(lobbyManagement.getLobby(lobbyID).get().getReadyStatus(theBotPlayer) == true);
    }

    // TODO: Weitere Tests implementieren, wenn die Botlogik ausgebaut wird.
}