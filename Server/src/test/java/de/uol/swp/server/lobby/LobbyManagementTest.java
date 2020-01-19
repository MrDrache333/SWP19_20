package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class LobbyManagementTest {
    static final User defaultLobbyOwner = new UserDTO("Owner", "Test", "123@test.de");
    static final String defaultLobbyName = "Lobby";
    static final String defaultLobbyPassword = "Lobby";
    final LobbyManagement lobbyManagement = new LobbyManagement();

    /**
     * Vor jedem Test wird eine Lobby erstellt.
     *
     * @author Julia
     * @since Sprint 3
     */
    @BeforeEach
    void createLobby() {
        lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, defaultLobbyOwner);
    }

    /**
     * Es wird getestet ob ein Fehler auftritt, wenn eine Lobby erstellt wird.
     *
     * @author Julia
     * @since Sprint 3
     */
    @Test
    void createLobbyTest() {
        Optional<Lobby> lobby = lobbyManagement.getLobby(defaultLobbyName);
        assertTrue(lobby.isPresent());
    }

    /**
     * Es wird getestet ob ein Fehler auftritt, wenn eine Lobby erstellt wird.
     *
     * @author Julia
     * @since Sprint 3
     */
    @Test
    void createLobbyFailedTest() {
        //Lobby name already exists
        assertThrows(IllegalArgumentException.class, () -> lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, defaultLobbyOwner));
    }

    /**
     * Es wird das Löschen der Lobby getestet und angenommen, dass sie dann leer ist
     *
     * @author Julia
     * @since Sprint 3
     */
    @Test
    void dropLobbyTest() {
        lobbyManagement.dropLobby(defaultLobbyName);
        Optional<Lobby> lobby = lobbyManagement.getLobby(defaultLobbyName);
        assertTrue(lobby.isEmpty());
    }

    /**
     * Es wird getestet was passiert wenn die Lobby gelöscht wird und die Lobby nicht existiert hat.
     *
     * @author Julia
     * @since Sprint 3
     */
    @Test
    void dropLobbyFailedTest() {
        //Lobby does not exist
        assertThrows(IllegalArgumentException.class, () -> lobbyManagement.dropLobby("Test"));
    }

    /**
     * Es wird getestet was passiert wenn man eine Lobby abfragt. Dabei wird einmal eine normale Lobby abgefragt
     * und eine die nicht existiert.
     *
     * @author Julia
     * @since Sprint 3
     */
    @Test
    void getLobbyTest() {
        Optional<Lobby> lobby = lobbyManagement.getLobby(defaultLobbyName);
        assertTrue(lobby.isPresent());

        lobby = lobbyManagement.getLobby("Test");
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
        lobbyManagement.leaveLobby(defaultLobbyName, defaultLobbyOwner);
        Optional<Lobby> lobby = lobbyManagement.getLobby(defaultLobbyName);
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
        lobbyManagement.createLobby("Lobby2","", new UserDTO("", "", ""));
        assertEquals(2, lobbyManagement.getLobbies().size());
        lobbyManagement.dropLobby(defaultLobbyName);
        lobbyManagement.dropLobby("Lobby2");
        assertEquals(0, lobbyManagement.getLobbies().size());
    }
}