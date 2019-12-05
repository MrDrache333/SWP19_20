package de.uol.swp.client.lobby;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.request.LeaveAllLobbiesOnLogoutRequest;
import de.uol.swp.common.lobby.request.LobbyJoinUserRequest;
import de.uol.swp.common.lobby.request.LobbyLeaveUserRequest;
import de.uol.swp.common.lobby.request.RetrieveAllOnlineLobbiesRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.dto.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LobbyServiceTest {

    User defaultUser = new UserDTO("Marco", "test", "marco@test.de");
    User defaultOwner = new UserDTO("Owner", "test", "123@test.de");
    Lobby defaultLobby = new LobbyDTO("TestLobby", defaultOwner, UUID.randomUUID());

    EventBus bus = new EventBus();
    CountDownLatch lock = new CountDownLatch(1);
    Object event;

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

    @Test
    void retrieveAllLobbiesTest() throws InterruptedException {
        LobbyService lobbyService = new LobbyService(bus);
        lobbyService.retrieveAllLobbies();

        lock.await(1000, TimeUnit.MILLISECONDS);

        assertTrue(event instanceof RetrieveAllOnlineLobbiesRequest);
    }

    private void joinLobby() throws InterruptedException {
        LobbyService userService = new LobbyService(bus);
        userService.joinLobby(defaultLobby.getName(), defaultUser, defaultLobby.getLobbyID());
        lock.await(1000, TimeUnit.MILLISECONDS);
    }

    @Test
    void joinLobbyTest() throws InterruptedException {
        joinLobby();

        assertTrue(event instanceof LobbyJoinUserRequest);

        LobbyJoinUserRequest lobbyJoinUserRequest = (LobbyJoinUserRequest) event;
        assertEquals(defaultLobby.getLobbyID(), lobbyJoinUserRequest.getLobbyID());
        assertEquals(defaultLobby.getName(), lobbyJoinUserRequest.getName());
        assertEquals(defaultUser, lobbyJoinUserRequest.getUser());
    }

    @Test
    void leaveLobbyTest() throws InterruptedException {
        joinLobby();

        event = null;

        LobbyService lobbyService = new LobbyService(bus);
        lobbyService.leaveLobby(defaultLobby.getName(), defaultUser, defaultLobby.getLobbyID());

        lock.await(1000, TimeUnit.MILLISECONDS);

        assertTrue(event instanceof LobbyLeaveUserRequest);

        LobbyLeaveUserRequest lobbyLeaveUserRequest = (LobbyLeaveUserRequest) event;

        assertEquals(defaultLobby.getLobbyID(), lobbyLeaveUserRequest.getLobbyID());
        assertEquals(defaultLobby.getName(), lobbyLeaveUserRequest.getName());
        assertEquals(defaultUser, lobbyLeaveUserRequest.getUser());
    }

    @Test
    void leaveAllLobbiesOnLogoutTest() throws InterruptedException {
        joinLobby();

        event = null;

        LobbyService lobbyService = new LobbyService(bus);
        lobbyService.leaveAllLobbiesOnLogout(defaultUser);

        lock.await(1000, TimeUnit.MILLISECONDS);

        assertTrue(event instanceof LeaveAllLobbiesOnLogoutRequest);

        LeaveAllLobbiesOnLogoutRequest leaveAllLobbiesOnLogoutRequest = (LeaveAllLobbiesOnLogoutRequest) event;

        assertEquals(defaultUser, leaveAllLobbiesOnLogoutRequest.getUser());
    }
}
