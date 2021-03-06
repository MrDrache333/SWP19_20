package de.uol.swp.server.lobby;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.lobby.exception.KickPlayerException;
import de.uol.swp.common.lobby.exception.SetMaxPlayerException;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse des LobbyManagement
 *
 * @author Julia
 * @since Sprint 3
 */
@SuppressWarnings("UnstableApiUsage")
class LobbyManagementTest {
    static final User defaultLobbyOwner = new UserDTO("Owner", "Test", "123@test.de");
    static final User secondUser = new UserDTO("Test", "Test", "1234@test.de");
    static final String defaultLobbyName = "Lobby";
    static final String defaultLobbyPassword = "Lobby";
    final LobbyManagement lobbyManagement = new LobbyManagement();
    private EventBus bus = new EventBus();
    private UUID lobbyID;
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
        lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, defaultLobbyOwner);
    }

    /**
     * Es wird getestet ob ein Fehler auftritt, wenn eine Lobby erstellt wird.
     *
     * @author Julia, Marvin
     * @since Sprint 3
     */
    @Test
    void createLobbyTest() {
        Optional<Lobby> lobby = lobbyManagement.getLobby(lobbyID);
        assertTrue(lobby.isPresent());
    }

    /**
     * Es wird das Löschen der Lobby getestet und angenommen, dass sie dann leer ist
     *
     * @author Julia
     * @since Sprint 3
     */
    @Test
    void dropLobbyTest() {
        lobbyManagement.dropLobby(lobbyID);
        Optional<Lobby> lobby = lobbyManagement.getLobby(lobbyID);
        assertTrue(lobby.isEmpty());
    }

    /**
     * Es wird getestet was passiert wenn die Lobby gelöscht wird und die Lobby nicht existiert hat.
     *
     * @author Julia, Marvin
     * @since Sprint 3
     */
    @Test
    void dropLobbyFailedTest() {
        //Lobby does not exist
        assertThrows(IllegalArgumentException.class, () -> lobbyManagement.dropLobby(UUID.randomUUID()));
    }

    /**
     * Es wird getestet was passiert wenn man eine Lobby abfragt. Dabei wird einmal eine normale Lobby abgefragt
     * und eine die nicht existiert.
     *
     * @author Julia, Marvin
     * @since Sprint 3
     */
    @Test
    void getLobbyTest() {
        Optional<Lobby> lobby = lobbyManagement.getLobby(lobbyID);
        assertTrue(lobby.isPresent());

        lobby = lobbyManagement.getLobby(UUID.randomUUID());
        assertTrue(lobby.isEmpty());
    }

    /**
     * Es wird getestet was passiert wenn nur eine Person in der Lobby ist und dieser wieder die Lobby verlässt.
     *
     * @author Julia
     * @since Sprint 3
     */
    @Test
    void leaveLobbyTest() {
        lobbyManagement.leaveLobby(lobbyID, defaultLobbyOwner);
        Optional<Lobby> lobby = lobbyManagement.getLobby(lobbyID);
        assertTrue(lobby.isEmpty());
    }

    /**
     * Es wird getestet ob die Anzahl der bestehenden Lobbies richtig ist.
     *
     * @author Julia
     * @since Sprint 3
     */
    @Test
    void getLobbiesTest() {
        assertEquals(1, lobbyManagement.getLobbies().size());
        UUID lobbyID2 = lobbyManagement.createLobby("Lobby2", "", new UserDTO("", "", ""));
        assertEquals(2, lobbyManagement.getLobbies().size());
        lobbyManagement.dropLobby(lobbyID);
        lobbyManagement.dropLobby(lobbyID2);
        assertEquals(0, lobbyManagement.getLobbies().size());
    }

    /**
     * Es wir getestet ob der User im Spiel ist.
     *
     * @author Darian
     * @since Sprint 7
     */
    @Test
    void isUserIngameTest() {
        try {
            assertFalse(lobbyManagement.isUserIngame(defaultLobbyOwner));
            lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, defaultLobbyOwner);
            assertFalse(lobbyManagement.isUserIngame(defaultLobbyOwner));
            Optional<Lobby> lobby = lobbyManagement.getLobby(lobbyID);
            lobby.orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).setInGame(true);
            assertTrue(lobbyManagement.isUserIngame(defaultLobbyOwner));
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Es wird getestet, ob der Owner mit seinen neuen Daten in den Lobbies aktualisiert wird.
     *
     * @author Ferit
     * @since Sprint8
     */
    @Test
    void updateLobbiesTest() {
        try {
            UserDTO oldUser = (UserDTO) defaultLobbyOwner;
            UserDTO newUser = new UserDTO("newOwner", "test", "test123@web.de");
            lobbyManagement.updateLobbies(newUser, oldUser);
            assertTrue(lobbyManagement.getLobby(lobbyID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).getOwner().getUsername().equals(newUser.getUsername()));
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Es wird getestet, ob ein User erfolgreich gekickt werden kann.
     *
     * @author Ferit
     * @since Sprint8
     */
    @Test
    void kickUserTest() {
        lobbyManagement.getLobby(lobbyID).get().joinUser(secondUser);
        lobbyManagement.kickUser(lobbyID, secondUser, defaultLobbyOwner);
        assertTrue(lobbyManagement.getLobby(lobbyID).isPresent());
        assertTrue(lobbyManagement.getLobby(lobbyID).get().getUsers().contains(defaultLobbyOwner) && lobbyManagement.getLobby(lobbyID).get().getPlayers() == 1);
    }
    /**
     * Es wird getestet, ob ein User nicht erfolgreich gekickt werden kann.
     *
     * @author Ferit
     * @since Sprint8
     */
    @Test
    void kickUserTestException() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            lobbyManagement.getLobby(lobbyID).get().joinUser(secondUser);
            lobbyManagement.kickUser(lobbyID, secondUser, secondUser);
            assertTrue(lobbyManagement.getLobby(lobbyID).isPresent());
            assertTrue(lobbyManagement.getLobby(lobbyID).get().getUsers().contains(defaultLobbyOwner) && lobbyManagement.getLobby(lobbyID).get().getPlayers() == 1);

        });
        assertTrue(exception instanceof KickPlayerException);

        Exception exception2 = assertThrows(RuntimeException.class, () -> {
            lobbyManagement.dropLobby(lobbyID);
            lobbyManagement.kickUser(lobbyID, secondUser, secondUser);
            assertFalse(lobbyManagement.getLobby(lobbyID).isPresent());
        });
        assertTrue(exception2 instanceof KickPlayerException);

           }

    /**
     * Es wird getestet, ob der LobbyOwner richtig geholt wird.
     *
     * @author Ferit
     * @since Sprint8
     */
    @Test
    void getLobbyOwnerTest() {
        lobbyManagement.leaveLobby(lobbyID, defaultLobbyOwner);
        assertTrue(lobbyManagement.getLobbyOwner(lobbyID).isEmpty());
        lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, defaultLobbyOwner);
        assertTrue(lobbyManagement.getLobbyOwner(lobbyID).get().equals(defaultLobbyOwner));
    }

    /**
     * Es wird die setMaxPlayer getestet.
     *
     * @author Ferit
     * @since Sprint8
     */
    @Test
    void setMaxPlayerTest() {
        try {
            lobbyManagement.setMaxPlayer(lobbyID, defaultLobbyOwner, 3);
            assertTrue(lobbyManagement.getLobby(lobbyID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).getMaxPlayer() == 3);
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }

    /**
     * Es wird die setMaxPlayer getestet, hier aber nur die Exception möglichkeiten.
     *
     * @author Ferit
     * @since Sprint8
     */
    @Test
    void setMaxPlayerTestException() {
        try {
            Exception exception = assertThrows(RuntimeException.class, () -> {
                lobbyManagement.getLobby(lobbyID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(secondUser);
                User thirdUser = new UserDTO("abc", "abc123", "abc@web.de");
                lobbyManagement.getLobby(lobbyID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(thirdUser);
                lobbyManagement.setMaxPlayer(lobbyID, defaultLobbyOwner, 2);
            });
            assertTrue(exception instanceof SetMaxPlayerException);

            Exception exception2 = assertThrows(RuntimeException.class, () -> {
                lobbyManagement.getLobby(lobbyID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(secondUser);
                User thirdUser = new UserDTO("abc", "abc123", "abc@web.de");
                lobbyManagement.getLobby(lobbyID).orElseThrow(() -> new NoSuchElementException("Lobby nicht existent")).joinUser(thirdUser);
                lobbyManagement.setMaxPlayer(lobbyID, secondUser, 3);
            });
            assertTrue(exception2 instanceof SetMaxPlayerException);
        } catch (NoSuchElementException exception) {
            Assertions.fail(exception.getMessage());
        }
    }
}