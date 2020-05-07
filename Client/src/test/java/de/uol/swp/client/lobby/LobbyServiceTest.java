package de.uol.swp.client.lobby;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.client.user.UserService;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.dto.LobbyDTO;
import de.uol.swp.common.lobby.request.LobbyJoinUserRequest;
import de.uol.swp.common.lobby.request.LobbyLeaveUserRequest;
import de.uol.swp.common.lobby.request.RetrieveAllOnlineLobbiesRequest;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import de.uol.swp.common.user.request.LogoutRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Der Lobby Service Test
 *
 * @author Julia
 * @since Sprint3
 */
class LobbyServiceTest {
    /**
     * Standard Benutzer, Standard Owner und Standard Lobby zum Testen
     *
     * @author Julia
     * @since Sprint3
     */
    User defaultUser = new UserDTO("Marco", "test", "marco@test.de");
    User defaultOwner = new UserDTO("Owner", "test", "123@test.de");
    Lobby defaultLobby = new LobbyDTO("TestLobby", defaultOwner, UUID.randomUUID(), "test");

    /**
     * Der zu verwendete EventBus
     *
     * @author Julia
     * @since Sprint3
     */

    EventBus bus = new EventBus();
    CountDownLatch lock = new CountDownLatch(1);
    Object event;


    /**
     * Methode zum Behandeln von auf dem Bus aufgetretene Dead-Events.
     *
     * @param e Das aufgetretene Dead-Event
     * @author Julia
     * @since Sprint3
     */
    @Subscribe
    void handle(DeadEvent e) {
        this.event = e.getEvent();
        System.out.print(e.getEvent());
        lock.countDown();
    }

    /**
     * Eventbus initialisieren
     *
     * @author Julia
     * @since Sprint3
     */
    @BeforeEach
    void registerBus() {
        event = null;
        bus.register(this);
    }

    /**
     * Klasse vom EventBus deregistrieren
     *
     * @author Julia
     * @since Sprint3
     */
    @AfterEach
    void deregisterBus() {
        bus.unregister(this);
    }

    /**
     * Überprüfen, ob alle Lobbys angegeben werden
     *
     * @throws InterruptedException
     * @author Julia
     * @since Sprint3
     */

    @Test
    void retrieveAllLobbiesTest() throws InterruptedException {
        LobbyService lobbyService = new LobbyService(bus);
        lobbyService.retrieveAllLobbies();

        lock.await(1000, TimeUnit.MILLISECONDS);

        assertTrue(event instanceof RetrieveAllOnlineLobbiesRequest);
    }

    /**
     * Hilfsmethode für Lobby beitreten
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Julia, Marvin
     * @since Sprint3
     */
    private void joinLobby() throws InterruptedException {
        LobbyService userService = new LobbyService(bus);
        userService.joinLobby(defaultLobby.getLobbyID(), new UserDTO(defaultUser.getUsername(), defaultUser.getPassword(), defaultUser.getEMail()));
        lock.await(1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Versuch einer Lobby beizutreten
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Julia
     * @since Sprint3
     */
    @Test
    void joinLobbyTest() throws InterruptedException {
        joinLobby();

        assertTrue(event instanceof LobbyJoinUserRequest);

        LobbyJoinUserRequest lobbyJoinUserRequest = (LobbyJoinUserRequest) event;
        assertEquals(defaultLobby.getLobbyID(), lobbyJoinUserRequest.getLobbyID());
        assertEquals(defaultUser, lobbyJoinUserRequest.getUser());
    }

    /**
     * Versuch Lobby zu verlassen
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Julia
     * @since Sprint3
     */
    @Test
    void leaveLobbyTest() throws InterruptedException {
        joinLobby();

        event = null;

        LobbyService lobbyService = new LobbyService(bus);
        lobbyService.leaveLobby(defaultLobby.getLobbyID(), new UserDTO(defaultUser.getUsername(), defaultUser.getPassword(), defaultUser.getEMail()));

        lock.await(1000, TimeUnit.MILLISECONDS);

        assertTrue(event instanceof LobbyLeaveUserRequest);

        LobbyLeaveUserRequest lobbyLeaveUserRequest = (LobbyLeaveUserRequest) event;

        assertEquals(defaultLobby.getLobbyID(), lobbyLeaveUserRequest.getLobbyID());
        assertEquals(defaultUser, lobbyLeaveUserRequest.getUser());
    }

    /**
     * Überprüfung, ob User beim Logout aus allen Lobbys entfernt wird
     *
     * @throws InterruptedException Die evtl. auftretene Fehlermeldung
     * @author Julia
     * @since Sprint3
     */
    @Test
    void leaveAllLobbiesOnLogoutTest() throws InterruptedException {
        joinLobby();

        event = null;

        UserService userService = new UserService(bus);
        userService.logout(defaultUser);


        lock.await(1000, TimeUnit.MILLISECONDS);

        assertTrue(event instanceof LogoutRequest);
    }
}
