package de.uol.swp.server.lobby;

import de.uol.swp.common.lobby.Lobby;
import de.uol.swp.common.user.User;
import de.uol.swp.common.user.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testklasse des LobbyManagement
 *
 * @author Julia
 * @since Sprint 3
 */
class LobbyManagementTest {
    static final User defaultLobbyOwner = new UserDTO("Owner", "Test", "123@test.de");
    static final User secondUser = new UserDTO("Test", "Test", "1234@test.de");
    static final String defaultLobbyName = "Lobby";
    static final String defaultLobbyPassword = "Lobby";
    final LobbyManagement lobbyManagement = new LobbyManagement();
    private UUID lobbyID;

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
        assertFalse(lobbyManagement.isUserIngame(defaultLobbyOwner));
        lobbyID = lobbyManagement.createLobby(defaultLobbyName, defaultLobbyPassword, defaultLobbyOwner);
        assertFalse(lobbyManagement.isUserIngame(defaultLobbyOwner));
        Optional<Lobby> lobby = lobbyManagement.getLobby(lobbyID);
        lobby.get().setInGame(true);
        assertTrue(lobbyManagement.isUserIngame(defaultLobbyOwner));
    }
}